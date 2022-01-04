package com.sso.panelapi.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sso.base.entity.BaseStaffinfo;
import com.sso.base.vo.BaseStaffinfoVO;
import com.sso.base.vo.StaffInfoVO;
import com.sso.base.wrapper.BaseStaffinfoWrapper;
import com.sso.dingding.entity.DingAttendance;
import com.sso.dingding.util.AttendanceUtil;
import com.sso.mapper.UserLoginMapper;
import com.sso.panelapi.config.MsgConfig;
import com.sso.panelapi.entity.MachineMainfo;
import com.sso.panelapi.service.IMachineMainfoService;
import com.sso.panelapi.service.UserLoginService;
import com.sso.panelapi.vo.CompanyVO;
import com.sso.panelapi.vo.LogOutVO;
import com.sso.supervise.entity.ExecuteState;
import com.sso.supervise.entity.SuperviseBoxinfo;
import com.sso.supervise.entity.SuperviseExecute;
import com.sso.supervise.service.IBaseStaffinfoService;
import com.sso.supervise.service.IExecuteStateService;
import com.sso.supervise.service.ISuperviseBoxInfoService;
import com.sso.supervise.service.ISuperviseExecuteService;
import com.sso.system.entity.SaUser;
import com.sso.system.service.SaIUserService;
import com.sso.utils.R;
import com.sso.utils.UpdateStateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.tool.ChatType;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.message.feign.ImMessageClient;
import org.springblade.system.feign.ICheckClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/plapi")
@Api(value = "人员表_blade_user", tags = "登录接口")
@CrossOrigin
@Slf4j
public class UserLoginController {
    private static final String LOGIN_KEY = "key.value.login.";
    @Autowired
    private UserLoginService iUserLoginService;
    @Autowired
    private IExecuteStateService stateService;
    @Autowired
    private IBaseStaffinfoService iBaseStaffinfoService;
    @Autowired
    private ISuperviseExecuteService iSuperviseExecuteService;
    @Autowired
    private ISuperviseBoxInfoService iSuperviseBoxinfoService;
    @Autowired
    private SaIUserService iUserService;
    @Autowired
    private IMachineMainfoService mainfoService;
    @Autowired
    private ImMessageClient messageClient;
    @Autowired
    private ICheckClient checkClient;
    @Autowired
    private UserLoginMapper userLoginMapper;

    @GetMapping("/login")
    @ResponseBody
    @ApiOperation(value = "工号登录", notes = "传入工号和密码和设备Id(maId)")
    public R AjaxLogin(SaUser user, Integer maId) {
        /**
         * 密码账号登录  只会传密码账号
         * app 扫码登录 传过来的是 useriD
         */
        String loginType = null;
        String idCard = user.getJobNum();//刷卡登录时,把输入框的值赋给idCard
        BaseStaffinfoVO staffinfo = null;
        /*先查询刷卡登录,如果有用户则登录成功,否则查其他的*/
        if (!StringUtil.isEmpty(idCard)) {
            BaseStaffinfo baseStaffinfo =
                    iBaseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("id_card", idCard).eq("is_used", 1));
            if (baseStaffinfo != null) {
                loginType = "使用刷卡登录";
                staffinfo = BaseStaffinfoWrapper.build().entityVO(baseStaffinfo);
            }
        }

        if (staffinfo == null) {//不是刷卡登录,校验扫码的呢过路
            if (user.getId() != null) { //扫码登录过来的参数 只有 id
                SaUser qcodeUser = iUserService.getById(user.getId());
                //user = qcodeUser;
                loginType = "使用扫码登录";
                staffinfo = iUserLoginService.loginByPrintChat(qcodeUser.getId());
            }
        }
        Integer executeStateId = null; // 初始化状态表的Id
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> aides = new HashMap<>();
        byte flag = -1;
        CompanyVO companyInfoVO = null;

        int isBlind = (maId == null || maId == -1) ? 0 : 1;
        if (isBlind == 1) {
            companyInfoVO = getCompanyInfo(maId);
            result.put("companyInfoVO", companyInfoVO);
        }
        result.put("isBlind", isBlind);
        //为空说明是账号密码登录
        if (staffinfo == null && !StringUtil.isEmpty(user.getPassword())) {
            staffinfo = iUserLoginService.loginByJobNum(user);
            loginType = "使用用户密码密码登录";
        }
        if (staffinfo != null) {
            String ddId = staffinfo.getDdId();
//            if(StringUtil.isEmpty(ddId)){
//                return R.error("该用户未绑定钉钉id");
//            }
            /**
             * 登录人设置为A1上班，A2下班,设置状态为'A' 设置开始时间，
             * 创建时间,把user_id写入boxinfo,也更新yb_suo***_execute
             */
            if (maId != null && maId != -1) {
                SuperviseExecute execute = iSuperviseExecuteService.getExecuteOrder(maId);
                if (execute != null) {
                    /*//触发解析服务中的停机质检缓存。todo*/
//                    try {
//                        checkClient.exeStatus(maId, execute.getUuid());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    String str = execute.getUsIds();
//                    //如果满足条件 说明 上个登录人是下班，如果不满足就是临时退出,第一次登录 状态表为空也得考虑这个情况
//                    if ((GlobalConstant.ProType.OFFWORK_EVENT.getType().equals(execute.getEvent())
//                            && GlobalConstant.ProType.PERSONNEL_STATUS.getType().equals(execute.getExeStatus()))
//                            || (execute.getEvent() == null && execute.getExeStatus() == null) ||
//                            ("".equals(execute.getEvent()) && "".equals(execute.getExeStatus()))) {
//                    }
                    //初始化一条状态语句
                    flag = updataBoxInfoLoginUserId(maId, staffinfo.getUserId());
                    /**读取出当前登录人员的所有助手**  todo 可能是nullprint*/
                    String userId = "";
                    userId = stateService.getLeaderAide(staffinfo.getUserId()).getTeamId() + "";//获取当前登录人员所有的助手的user_id
                    String[] userIds = userId.split("\\|");
                    for (int i = 1; i < userIds.length; i++) {
                        aides.put("aidesList",
                                iBaseStaffinfoService.getBaseStaffinfoByUsId(Integer.parseInt(userIds[i])));
                    }
                }
            }
            /***
             * 如果上一个操作人员 没有非正常退出 ，新的登录人员如何接盘子上个登录人员的操作步骤
             * todo  待讨论
             */
            result.put("staffinfo", staffinfo);
            result.put("flag", flag);
            result.put("isBlind", isBlind);
            result.put("ades", aides);
            System.err.println(staffinfo.getName() + loginType + "成功");
            DingAttendance dingAttendance = new DingAttendance();
            Calendar cal = Calendar.getInstance();
            // 返回当前系统的UTC时间，具体实现可参看JDK源码
            long userCheckTime = cal.getTimeInMillis();
            dingAttendance.setDeviceId(maId.toString());
            dingAttendance.setDeviceName(companyInfoVO.getMaName());
            dingAttendance.setUserid(ddId);
            dingAttendance.setUserCheckTime(userCheckTime);
            AttendanceUtil.importAttendance(dingAttendance);
            return R.ok(result, "staffinfo：用户登录信息；" +
                    "isBlind=0 该人员没有绑定设备");
        }
        System.err.println("登录是失败");
        return R.error(500, "登录失败");
    }

    /***
     * 手机注册
     * @param phoneNum
     * @return
     */
    @ApiOperation(value = "手机验证码注册")
    @GetMapping("/sginInByPhone")
    public R loginByPhoneCode(String phoneNum) {
        //开始时间
        Date startAt = new Date();
        /**生成一个四位的随机数*/
        StringBuffer code = new StringBuffer("");
        for (int i = 0; i < 5; i++) {
            code.append((int) (Math.random() * 10));
        }
        R r = MsgConfig.sendMessages(phoneNum, code.toString());
        return r;
    }

    /**
     * 登录成功给登录记录登录用户的公共方法
     */
    public byte updataBoxInfoLoginUserId(Integer maId, int userId) {
        /*创建当前时间*/
        byte flag = -1;
        Map<String, Object> aides = new HashMap<String, Object>();
        Date time = new Date();
        ExecuteState state = new ExecuteState();
        state.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
        state.setEvent(GlobalConstant.ProType.ONWORK_EVENT.getType());
        state.setMaId(maId);
        state.setStartAt(time);
        state.setCreateAt(time);
        state.setTeamId("|" + userId + "|");
        state.setUsId(userId);
        Integer jobs = iBaseStaffinfoService.getBaseStaffinfoByUsId(userId).getJobs();
        if (jobs != null && jobs == 1) { //判断JOB是否为机长，是就插入leader_id 不是就不插入
            state.setLeaderId(userId);
        }
        state.setLeaderId(null);
        UpdateStateUtils.updateSupervise(state);

        if (maId == null) { //不能为空否则会取出所有盒子
            return flag = 2;//去设备绑定界面
        }
        SuperviseBoxinfo boxinfo =
                iSuperviseBoxinfoService.getBoxInfoByMid(maId);
        //更新盒子的操作人
        if (boxinfo != null) {
            boxinfo.setUsIds("|" + userId + "|");
            iSuperviseBoxinfoService.updateById(boxinfo);
        }
        return flag;

    }

    /**
     * 登录成功 用maId判断是否是绑定设备，绑定了传给前台公司信息
     */
    public CompanyVO getCompanyInfo(Integer maId) {

        return mainfoService.getCompanyInfoByMaId(maId);

    }

    /***
     * 人脸登录
     * @param account 工号
     * @return
     */
    @ApiOperation(value = "人脸登录")
    @GetMapping("/faceLogin")
    public R faceLogin(String account, Integer maId) {
        /**
         * 密码账号登录  只会传密码账号
         * app 扫码登录 传过来的是 useriD
         */
        BaseStaffinfoVO staffinfo = userLoginMapper.faceLogin(account);
        Map<String, Object> result = new HashMap<>();

        int isBlind = maId == null || maId == -1 ? 0 : 1;
        CompanyVO companyInfo = null;
        if (isBlind == 1) {
            companyInfo = getCompanyInfo(maId);
            if (companyInfo == null) {
                companyInfo = new CompanyVO();
                companyInfo.setFName("合和科技");
            }
            if (Func.isBlank(companyInfo.getFName())) {
                companyInfo.setFName("合和科技");
            }
            result.put("companyInfoVO", companyInfo);
        }
        byte flag = -1;
        if (staffinfo != null) {
            result.put("staffinfo", staffinfo);
            result.put("flag", flag);
            String ddId = staffinfo.getDdId();
//            if(StringUtil.isEmpty(ddId)){
//                return R.error("该用户未绑定钉钉id");
//            }
            DingAttendance dingAttendance = new DingAttendance();
            Calendar cal = Calendar.getInstance();
            // 返回当前系统的UTC时间，具体实现可参看JDK源码
            long userCheckTime = cal.getTimeInMillis();
            dingAttendance.setDeviceId(maId.toString());
            if (companyInfo != null) {
                dingAttendance.setDeviceName(companyInfo.getMaName());
            }
            dingAttendance.setUserid(ddId);
            dingAttendance.setUserCheckTime(userCheckTime);
            AttendanceUtil.importAttendance(dingAttendance);
            return R.ok(result, "登录成功");
        }
        return R.error(500, "登录失败");
    }

    /***
     * 人脸登录详情
     * @param id 员工详情
     * @return
     */
    @ApiOperation(value = "人脸登录")
    @GetMapping("/get")
    public R<StaffInfoVO> get(Integer id) {
        StaffInfoVO staffinfo = userLoginMapper.getStaffInfo(id);
        return R.ok(staffinfo);
    }

    @RequestMapping("/idcardlogin")
    @ApiOperation(value = "工号登录", notes = "传入ipCard")
    public R IdCardLogin(String idCard) {
        BaseStaffinfo baseStaffinfo =
                iBaseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("id_card", idCard));
        if (baseStaffinfo == null) {
            return R.error("用户不存在");
        }
        return R.ok("登录成功");
    }

    /***
     * 人脸机组巡检人员信息
     * @param id 人脸登录人id
     * @return
     */
    @ApiOperation(value = "人脸机组巡检人员信息")
    @GetMapping("/faceClassInfo")
    public R<List<StaffInfoVO>> faceClassInfo(Integer id) {
        List<StaffInfoVO> vos = iBaseStaffinfoService.faceClassInfo(id);
        return R.ok(vos);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "机台退出登录")
    public R logout(@RequestBody LogOutVO logOutVO) {
//        UpdateStateUtils.updateSupervise(state);
        try {
            Integer maId = logOutVO.getMaId();
            Integer usId = logOutVO.getUsId();
            Date time = new Date();
            ExecuteState state = new ExecuteState();
            state.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
            state.setEvent(GlobalConstant.ProType.OFFWORK_EVENT.getType());
            state.setMaId(maId);
            state.setStartAt(time);
            state.setCreateAt(time);
            state.setTeamId("|" + usId + "|");
            state.setUsId(usId);
            Integer jobs = iBaseStaffinfoService.getBaseStaffinfoByUsId(usId).getJobs();
            if (jobs != null && jobs == 1) { //判断JOB是否为机长，是就插入leader_id 不是就不插入
                state.setLeaderId(usId);
            }
            stateService.save(state);
            BaseStaffinfo staffinfo = iUserLoginService.loginByPrintChat(usId);
            if (staffinfo == null) {
                log.error("没找到对应的用户,请检查用户id是否正确");
            }
            MachineMainfo machineMainfo = mainfoService.getById(maId);

            DingAttendance dingAttendance = new DingAttendance();
            Calendar cal = Calendar.getInstance();
            // 返回当前系统的UTC时间，具体实现可参看JDK源码
            long userCheckTime = cal.getTimeInMillis();
            dingAttendance.setDeviceId(maId + "");
            if (machineMainfo == null) {
                log.error("没找到对应设备,请检查设备id是否正确");
            } else {
                dingAttendance.setDeviceName(machineMainfo.getName());
            }
            dingAttendance.setUserid(staffinfo.getDdId());
            dingAttendance.setUserCheckTime(userCheckTime);
            AttendanceUtil.importAttendance(dingAttendance);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("钉钉接口调用失败");
        }
        return R.ok();
    }
}
