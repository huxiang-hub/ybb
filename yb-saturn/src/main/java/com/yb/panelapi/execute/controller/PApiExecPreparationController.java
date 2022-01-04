package com.yb.panelapi.execute.controller;

import com.alibaba.fastjson.JSON;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecutePreparation;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecutePreparationService;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.vo.ExecuteStateVO;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.panelapi.common.SendMsgUtils;
import com.yb.panelapi.common.TempEntity;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseExecuteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.tool.ChatType;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "执行表状态_yb_execute_state", tags = "助理操作接口")
@RequestMapping("/plapi")
public class PApiExecPreparationController {
    @Autowired
    private IExecutePreparationService executePreparationService;
    @Autowired
    private ISuperviseExecuteService iSuperviseExecuteService;
    @Autowired
    private IExecuteStateService iExecuteStateService;
    @Autowired
    private IOrderWorkbatchService orderWorkbatchService;

    /**
     * done 换版准备
     */
    @ApiOperation(value = "换膜保养准备_yb_execute_preparation,yb_execute_state",
            tags = "前端传进一个对象，必要参数,usId   maId ")
    @PostMapping("/startMaintenance")
    @ResponseBody
    public R startMaintenance(@RequestBody ExecuteStateVO stateVO) {

        if (stateVO.getMaId() == null) {
            return R.fail("maId==>null");
        }
        SuperviseExecute executeOrder = iSuperviseExecuteService.getExecuteOrder(stateVO.getMaId());
        Integer sdId = executeOrder.getSdId();
        Integer wdId = executeOrder.getWbId();
        Integer wfId = stateVO.getWfId();
        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(wdId);
        Integer odId = null;
        if (orderWorkbatch != null) {
            odId = orderWorkbatch.getOdId();
        }
        Date time = new Date();//当前时间
        if (stateVO.getFlag() == 1) { // 保养开始
            ExecuteState state = new ExecuteState();
            state.setCreateAt(time);
            state.setStartAt(time);
            state.setEndAt(null);
            state.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
            state.setEvent(GlobalConstant.ProType.MAINTAIN_EVENT.getType());
            state.setUsId(stateVO.getUsId());
            state.setMaId(stateVO.getMaId());
            state.setSdId(sdId);
            state.setOdId(odId);
            state.setWbId(wdId);
            state.setWfId(wfId);
            state.setTeamId(executeOrder.getUsIds());
            //插入状态表中操作数据

            iExecuteStateService.save(state);
            //把yb_execute_state表和yb_execut_preparation的外键关联 id  ===  es_Id
            //在yb_supervise_execute 取出 currNum
            //返回值错误 应该返回crrnum
            Integer currNum = iSuperviseExecuteService.getCurrNum(stateVO.getMaId());
            ExecutePreparation preparation = new ExecutePreparation();
            preparation.setExId(state.getId()); //TODO EXID需要更换exId对象内容 wyn
            preparation.setTestPaper(currNum);
            preparation.setUpdateAt(time);
            preparation.setReadyType(GlobalConstant.ProType.MAINTAIN_EVENT.getType()); //保养
            preparation.setStartAt(time);
            preparation.setCreateAt(time);
            // me  executePreparationService.savePreparationo(preparation);
            executePreparationService.save(preparation);
            /**
             * 更新生产实施表的状态
             */
            executeOrder.setEsId(state.getId());
            executeOrder.setUpdateAt(time);
            executeOrder.setStartTime(time);
            executeOrder.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
            executeOrder.setEvent(GlobalConstant.ProType.MAINTAIN_EVENT.getType());
            executeOrder.setUsIds(stateVO.getTeamId());
            iSuperviseExecuteService.updateById(executeOrder);
        } else { //保养结束
            // 获取当前的状态IDyb_supervise_state 的es_id
            Integer esId = executeOrder.getEsId();
            //获取状态表
            ExecuteState state = iExecuteStateService.getExecuteStateByEsId(esId);
            //更新状态表的结束时间和 持续时长
            /*Double duration = Double.valueOf(
                    (time.getTime() - state.getStartAt().getTime()));*/
            double duration = DateUtil.calLastedTime(time.getTime(), state.getStartAt());
            state.setEndAt(time);
            state.setDuration(duration);
            iExecuteStateService.updataExecuteState(state);
            //更新准备生产状态表
            //获取状态表
            ExecutePreparation preparation = executePreparationService.getExecutePreparationById(state.getId());
            preparation.setFinishAt(time);
            preparation.setUpdateAt(time);
            preparation.setExId(esId); //TODO EXID需要更换exId对象内容 wyn
            executePreparationService.updataExecutePreparation(preparation);
            // 给当前所有登录机台有用户发送保养消息
            TempEntity temp = new TempEntity();
            temp.setStartTime(state.getStartAt());
            temp.setDuration(duration);
            SendMsgUtils.sendToUser(executeOrder.getUsIds(), JSON.toJSONString(temp),
                    ChatType.MAC_MAINTAIN.getType());
        }

        return R.success("执行成功");
    }

    @ApiOperation(value = "换膜保养准备_yb_execute_preparation,yb_execute_state",
            tags = "前端传进一个对象，必要参数,usId   maId   sd_id  flag=1开始0结束  ")
    @PostMapping("/startMembraneChange")
    @ResponseBody
    public R startMembraneChange(@RequestBody ExecuteStateVO stateVO) {
        Date time = new Date();//方法当前时间
        Integer maId = stateVO.getMaId();
        SuperviseExecute executeOrder = iSuperviseExecuteService.getExecuteOrder(maId);
        Integer sdId = executeOrder.getSdId();
        Integer wdId = executeOrder.getWbId();
        Integer wfId = stateVO.getWfId();
        OrderWorkbatch orderWorkbatch = orderWorkbatchService.getById(wdId);
        Integer odId = null;
        if (orderWorkbatch != null) {
            odId = orderWorkbatch.getOdId();
        }

        if (stateVO.getFlag() == 1) { // 换膜开始
            ExecuteState state = new ExecuteState();
            state.setCreateAt(time);
            state.setStartAt(time);
            state.setEndAt(null);
            state.setStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
            state.setEvent(GlobalConstant.ProType.CHANGE_EVENT.getType());
            state.setMaId(maId);
            state.setUsId(stateVO.getUsId());
            state.setTeamId(executeOrder.getUsIds());
            state.setWbId(wdId);
            state.setWfId(wfId);
            state.setSdId(sdId);
            state.setOdId(odId);
            //插入状态表中操作数据
            iExecuteStateService.save(state);
            //把yb_execute_state表和yb_execut_preparation的外键关联 id  ===  es_Id
            //在yb_supervise_execute 取出 currNum
            //返回值错误 应该返回crrnum
            Integer currNum = iSuperviseExecuteService.getCurrNum(maId);
            ExecutePreparation preparation = new ExecutePreparation();
            preparation.setExId(state.getId()); //TODO EXID需要更换exId对象内容 wyn
            preparation.setTestPaper(currNum);
            preparation.setUpdateAt(time);
            preparation.setReadyType(GlobalConstant.ProType.CHANGE_EVENT.getType()); //换膜
            preparation.setStartAt(time);
            preparation.setCreateAt(time);
            executePreparationService.save(preparation);
            /**
             * 更新生产实施表的状态
             */
            /**以防数据被覆盖再覆盖下时间*/
            executeOrder.setEsId(state.getId());
            executeOrder.setExeStatus(GlobalConstant.ProType.BEFOREPRO_STATUS.getType());
            executeOrder.setEvent(GlobalConstant.ProType.CHANGE_EVENT.getType());
            executeOrder.setStartTime(time);
            executeOrder.setUpdateAt(time);
            executeOrder.setUsIds(stateVO.getTeamId());
            iSuperviseExecuteService.updateById(executeOrder);
            // iSuperviseExecuteService.updateStateToSuperviseExecute(executeOrder);
        } else { //换膜结束
            // 获取当前的状态IDyb_supervise_state 的es_id
            Integer esId = executeOrder.getEsId();
            //获取状态表
            ExecuteState state = iExecuteStateService.getExecuteStateByEsId(esId);
            double duration = DateUtil.calLastedTime(time.getTime(), state.getStartAt());
            state.setEndAt(time);
            state.setDuration(duration);
            iExecuteStateService.updateById(state);
            //更新准备生产状态表
            //获取状态表
            ExecutePreparation preparation = executePreparationService.getExecutePreparationById(state.getId());
            preparation.setFinishAt(time);
            preparation.setUpdateAt(time);
            preparation.setExId(esId);//TODO EXID需要更换exId对象内容 wyn
            executePreparationService.updateById(preparation);

            // 给当前所有登录机台有用户发送换膜时长消息
            TempEntity temp = new TempEntity();
            temp.setStartTime(state.getStartAt());
            temp.setDuration(duration);
            SendMsgUtils.sendToUser(executeOrder.getUsIds(), JSON.toJSONString(temp),
                    ChatType.MAC_CHANGE.getType());
        }
        return R.success("执行成功");
    }

    @ApiOperation(value = "获取机器当前的保养状态",
            tags = "传入maId  ")
    @GetMapping("/getMachinStatus")
    @ResponseBody
    public R getMachineStatus(@Param("maId") Integer maId) {
        //通过设备Id找到保养记录表id查出设备现在的状态
        ExecutePreparation preparation =
                executePreparationService.getExecutePreparationById(
                        iSuperviseExecuteService.getExecuteOrder(maId).getEsId());//状态表的Id

        return R.data(preparation, "ok");
    }


}
