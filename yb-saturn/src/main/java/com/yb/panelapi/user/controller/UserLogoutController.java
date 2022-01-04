package com.yb.panelapi.user.controller;

import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteStateService;
import com.yb.panelapi.common.UpdateStateUtils;
import com.yb.panelapi.user.utils.R;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import org.springblade.common.constant.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/4/27.
 */
@RestController
@RequestMapping("/plapi")
public class UserLogoutController {

    @Autowired
    private IExecuteStateService stateService;
    @Autowired
    private ISuperviseExecuteService executeService;
    @Autowired
    private ISuperviseBoxinfoService boxinfoService;

    @RequestMapping("/logout")
    public R logout(String usId, Integer maId) {
        Date currTime = new Date();
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        String usIds = executeOrder.getUsIds();
        if (usIds == null || "".equals(usIds)) {
            return R.error();
        }
        String[] split = usIds.split("\\|");
        //最后一个人下班 (|15|16| 分切后第一个会是空格)
        if (split.length <= 2) {
            //执行表新增一条数据
            ExecuteState newState = new ExecuteState();
            newState.setUsId(Integer.valueOf(usId)); //设置下班人
            newState.setMaId(maId);
            newState.setStartAt(currTime);
            newState.setEndAt(currTime);
//            String test  = ("chaojimali");
//            test = test.replace("chaoji","");
            newState.setTeamId(""); //去除当前退出的userId
            newState.setCreateAt(currTime);
            //设置下班状态
            newState.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
            newState.setEvent(GlobalConstant.ProType.OFFWORK_EVENT.getType());
            UpdateStateUtils.updateSupervise(newState,null);
            return R.ok();
        } else {
            List<String> usList = Arrays.asList(split);
            //判断是否存在该用户
            if (usList.contains(usId)) {
                //转换为ArrayLsit调用相关的remove方法
                List<String> arrayList = new ArrayList<>(usList);
                arrayList.remove(usId);
                StringBuilder result = new StringBuilder();
                for (String item : arrayList) {
                    result.append(item + "|");
                }
                //执行表新增一条数据
                ExecuteState newState = new ExecuteState();
                newState.setUsId(Integer.valueOf(usId));
                newState.setMaId(maId);
                newState.setTeamId(result.toString());
                newState.setWbId(executeOrder.getWbId());
                newState.setSdId(executeOrder.getSdId());
                newState.setStartAt(currTime);
                newState.setEndAt(currTime);
                newState.setCreateAt(currTime);
                //设置下班状态
                newState.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
                newState.setEvent(GlobalConstant.ProType.OFFWORK_EVENT.getType());
                //不是最后一个下班不更新实施状态表
                stateService.save(newState);
                executeOrder.setUsIds(result.toString());
                executeService.updateById(executeOrder);
                //UpdateStateUtils.updateSupervise(newState);
                return R.ok();
            } else {
                return R.error("用户已经下班退出");
            }
        }
    }

    @PostMapping("/logoutAll")
    public R logoutAll(Integer maId) {
        String[] usIds = null;
        SuperviseExecute executeOrder = executeService.getExecuteOrder(maId);
        if (executeOrder.getUsIds() == null || "".equals(executeOrder.getUsIds())) {
            return R.error();
        }
        usIds = executeOrder.getUsIds().split("\\|"); //下表为0的元素 为 ""
        //机台剩余最后一个人
            Date currTime = new Date();
            for (int i = 1; i < usIds.length; i++) {
                    logout(usIds[i],maId);

            }
        return R.ok();
    }
}
