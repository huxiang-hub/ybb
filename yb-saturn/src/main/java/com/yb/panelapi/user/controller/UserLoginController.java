package com.yb.panelapi.user.controller;

import com.alibaba.fastjson.JSON;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.CompanyVO;
import com.yb.panelapi.common.UpdateStateUtils;
import com.yb.panelapi.user.configs.MsgConfig;
import com.yb.panelapi.user.mapper.UserLoginMapper;
import com.yb.panelapi.user.service.UserLoginService;
import com.yb.panelapi.user.utils.R;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.service.SaIUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.tool.ChatType;
import org.springblade.message.feign.ImMessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/plapi")
@Api(value = "人员表_blade_user", tags = "登录接口")
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
    private ISuperviseBoxinfoService iSuperviseBoxinfoService;
    @Autowired
    private SaIUserService iUserService;
    @Autowired
    private IMachineMainfoService mainfoService;
    @Autowired
    private ImMessageClient messageClient;
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
        BaseStaffinfoVO staffinfo = null;
        if (user.getId() != null) { //扫码登录过来的参数 只有 id
            SaUser qcodeUser = iUserService.getById(user.getId());
            //user = qcodeUser;
            staffinfo = iUserLoginService.loginByPrintChat(qcodeUser.getId());
        }
        Integer executeStateId = null; // 初始化状态表的Id
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> aides = new HashMap<>();
        byte flag = -1;
        CompanyVO companyInfoVO = null;

        int isBlind = maId == null || maId == -1 ? 0 : 1;
        if (isBlind == 1) {
            companyInfoVO = getCompanyInfo(maId);
            result.put("companyInfoVO", companyInfoVO);
        }
        result.put("isBlind", isBlind);
        //为空说明是账号密码登录
        if (staffinfo == null) {
            staffinfo = iUserLoginService.loginByJobNum(user);
        }
        if (staffinfo != null) {
            /**
             * 登录人设置为A1上班，A2下班,设置状态为'A' 设置开始时间，
             * 创建时间,把user_id写入boxinfo,也更新yb_suo***_execute
             */
            if (maId != null && maId != -1) {
                SuperviseExecute execute = iSuperviseExecuteService.getExecuteOrder(maId);
                if (execute != null) {
                    String str = execute.getUsIds();
                    //如果满足条件 说明 上个登录人是下班，如果不满足就是临时退出,第一次登录 状态表为空也得考虑这个情况
                    if ((GlobalConstant.ProType.OFFWORK_EVENT.getType().equals(execute.getEvent())
                            && GlobalConstant.ProType.PERSONNEL_STATUS.getType().equals(execute.getExeStatus()))
                            || (execute.getEvent() == null && execute.getExeStatus() == null) ||
                            ("".equals(execute.getEvent()) && "".equals(execute.getExeStatus()))) {
                    }
                    // TODO 如果上一个登录人员非A2退出 ，会空指针 暂时解决 将原88行代码放到91行 2020/5/7
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
            try {
                messageClient.sendMsgToUser(String.valueOf(staffinfo.getUserId()),
                        JSON.toJSONString("机台登录成功"), ChatType.MAC_LOGIN.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }


            return R.ok(result, "staffinfo：用户登录信息；" +
                    "isBlind=0 该人员没有绑定设备");
        }
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
        UpdateStateUtils.updateSupervise(state, null);

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
     * @param jobNum 工号
     * @return
     */
    @ApiOperation(value = "人脸登录")
    @GetMapping("/faceLogin")
    public R faceLogin(String jobNum, Integer maId) {
        /**
         * 密码账号登录  只会传密码账号
         * app 扫码登录 传过来的是 useriD
         */
        BaseStaffinfoVO staffinfo = userLoginMapper.faceLogin(jobNum);
        Map<String, Object> result = new HashMap<>();
        byte flag = -1;
        CompanyVO companyInfoVO = null;

        int isBlind = maId == null || maId == -1 ? 0 : 1;
        if (isBlind == 1) {
            companyInfoVO = getCompanyInfo(maId);
            result.put("companyInfoVO", companyInfoVO);
        }
        result.put("isBlind", isBlind);
        if (staffinfo != null) {
            result.put("staffinfo", staffinfo);
            result.put("flag", flag);
            result.put("isBlind", isBlind);
            return R.ok(result, "staffinfo：用户登录信息；" +
                    "isBlind=0 该人员没有绑定设备");
        }
        return R.error(500, "登录失败");
    }
}
