//package com.yb.panelapi.execute.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.yb.base.entity.BaseStaffinfo;
//import com.yb.base.service.IBaseStaffinfoService;
//import com.yb.base.vo.BaseStaffinfoVO;
//import com.yb.common.CreateQcodeUtil;
//import com.yb.execute.entity.ExecuteState;
//import com.yb.execute.service.IExecuteStateService;
//import com.yb.panelapi.common.UpdateStateUtils;
//import com.yb.panelapi.user.service.UserLoginService;
//import com.yb.panelapi.user.utils.QrCode;
//import com.yb.panelapi.user.utils.R;
//import com.yb.supervise.entity.SuperviseExecute;
//import com.yb.supervise.service.ISuperviseExecuteService;
//import com.yb.system.user.entity.SaUser;
//import com.yb.system.user.service.SaIUserService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springblade.common.constant.GlobalConstant;
//import org.springblade.common.tool.ChatType;
//import org.springblade.message.feign.ImMessageClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpSession;
//import java.sql.SQLException;
//import java.util.*;
//import java.util.concurrent.Callable;
//import java.util.concurrent.TimeUnit;
//
///**
// * qinbo MwymS9uT
// */
//@RequestMapping("/plapi")
//@Api(value = "执行表状态_yb_execute_state", tags = "助理操作接口")
//@RestController
//public class AddAideController {
//    /**
//     * 存储个人信息
//     */
//    Map<String,Object> result = new HashMap();
//    @Autowired
//    private IExecuteStateService iExecuteStateService;
//    @Autowired
//    private ISuperviseExecuteService superviseExecuteService;
//    @Autowired
//    private UserLoginService userLoginService;
//    @Autowired
//    private ImMessageClient messageClient;
//    @Autowired
//    private SaIUserService saIUserService;
//    @Autowired
//    private IBaseStaffinfoService staffinfoService;
//    @GetMapping("/addaide")
//    @ResponseBody
//    @ApiOperation(value = "手动添加助理", tags = "yb_execute_state,yb_base_staffinfo" +
//            "(接收参数 机长工号，用户对象)")
//    public R addAide(String leaderId, String jobNum, String password,Integer userId) throws SQLException {
//        SaUser saUser = new SaUser();
//        saUser.setJobNum(jobNum);
//        saUser.setPassword(password);
//        int leaderDeptId; //接收机长的部门
//        int teamDeptId; //接收助理的部门
//        int jobs;//当前登录的职位
//        /**
//         * 扫码添加助理进来的密码已经被加密处理过，所以如不做处理就会二次加密，助理会一直登陆失败
//         */
//        BaseStaffinfo staffAide = null;
//        if (userId!=null) {
//            Map<String,Object> map = new HashMap<>(); //
//            map.put("user_id",userId);
//            staffAide = staffinfoService.getBaseMapper().selectByMap(map).get(0);//null
//        }else{
//            //查找登录助理信息
//            staffAide = userLoginService.loginByJobNum(saUser);
//        }
//        if(staffAide==null){
//            //助理登录失败
//            return R.error(500, "助理登录失败！");
//        }
//        //找出机长
//        BaseStaffinfo staffLeader = staffinfoService.getLeaderByLeaderId(leaderId);   //2
////        BaseStaffinfo staffAide = iBaseStaffinfoService.getLeaderByLeaderId(saUser.getJobNum());  //2
//        if (staffAide == null) {
//            return R.error("没有该助理！");
//        }
//        if (staffAide.getJobnum().equals(staffLeader.getJobnum())) {
//            return R.error("不能自己添加自己为助理！");
//        }
//        //获取当前操作人的执行状态表信息
//        ExecuteState state = iExecuteStateService.getLeaderAide(staffLeader.getUserId());
//        Map<String,Object> map = new HashMap<>();
//        map.put("operator",staffLeader.getUserId());//当前登录人员的Id
//        map.put("ma_id",state.getMaId());
//        List<SuperviseExecute> sExecutes = superviseExecuteService.getBaseMapper().selectByMap(map);
//        if (sExecutes.isEmpty()) {
//            return R.error();
//        }
//        SuperviseExecute sExecute =sExecutes.get(0);
//        ExecuteState eState = iExecuteStateService.getById(sExecute.getEsId());
//        /*先获取当前操作人，助理的部门和操作人对应的职位*/
//        leaderDeptId = staffLeader.getDpId();
//        teamDeptId = staffAide.getDpId();
//        //jobs = staffLeader.getJobs();
//        System.out.println("leaderDeptId:" + leaderDeptId);
//        if (leaderDeptId != teamDeptId) {
//            //机长和助手不是同一个部门
//            return R.error(500, "助理和机长不在一个部门！");
//        }
//            String aides = sExecute.getUsIds();
//            StringBuffer teamId = new StringBuffer("");
//            if (aides == null || "".equals(aides)) {
//                teamId.append("|" + saUser.getId() + "|");
//            } else {
//                String[] aide = aides.split("\\|");
//                teamId.append(aides);
//                boolean isAdd = false;
//                for (String a : aide) {
//                    //如果为空就跳出当次循环
//                    if(a==null||"".equals(a)){ continue;}
//                    if (a.equals(staffAide.getUserId()+"")) {
//                        isAdd = false;
//                        return R.error("他/她已经是你的助理了！");
//                    } else {
//                        isAdd = true;
//                    }
//                }
//                if (isAdd)teamId.append(staffAide.getUserId() + "|");
//            }
//            //ExecuteState eState = new ExecuteState();
//            eState.setTeamId(teamId.toString());
//            Date time = new Date();
//            //添加成功
//            eState.setCreateAt(time);//更新时间
//            eState.setStartAt(time);
//            UpdateStateUtils.updateSupervise(eState,null);
//            /*助理添加成功 设置助理为上班*/
//            eState.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
//            eState.setEvent(GlobalConstant.ProType.ONWORK_EVENT.getType());
//            eState.setMaId(eState.getMaId());
//            eState.setUsId(staffAide.getUserId());
//            iExecuteStateService.saveState(eState);//助理登录，记录 但是不更新订单状态表
//            // 添加助理成功  给印聊APP 发送消息
//            try {
//                messageClient.sendMsgToUser(String.valueOf(staffAide.getUserId()),
//                        JSON.toJSONString(staffAide),
//                        ChatType.MAC_LOGIN.getType());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return R.ok(staffAide, "添加助理成功！");
//    }
//}