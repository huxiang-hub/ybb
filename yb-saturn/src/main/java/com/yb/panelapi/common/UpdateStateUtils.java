package com.yb.panelapi.common;

import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.panelapi.user.utils.R;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * 更新执行状态表通用方法
 *
 * @author by SUMMER
 * @date 2020/3/20.
 */
@Component
@Slf4j
public class UpdateStateUtils {

    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private SuperviseExecuteMapper executeMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;

    //单位毫秒
    public static long TIMEUNIT = 1 * 60 * 1000;

    private static UpdateStateUtils executeState;

    @PostConstruct
    public void init() {
        executeState = this;
        executeState.stateMapper = this.stateMapper;
        executeState.executeMapper = this.executeMapper;
        executeState.superviseBoxinfoMapper = this.superviseBoxinfoMapper;
    }

//    public static int updateExecute(ExecuteState state) {
//        //根据开始时间排序，查询出最后一条数据。
//        ExecuteState state1 = executeState.stateMapper.getExecuteState(state.getMaId(), state.getSdId());
//        state1.setEndAt(new Date());
//        state1.setDuration(DateUtil.durationTime(state1.getStartAt()));
//        //更新时间最近的一条数据
//        executeState.stateMapper.updateById(state1);
//        //保存新的数据
//        return executeState.stateMapper.insert(state);
//    }


    /****
     * 需要插入状态表信息，并且更新当前状态表，并且需要按照顺序来进行执行。
     * @param newState
     * @return
     */
    public static R updateSupervise(ExecuteState newState, ExecuteInfo executeInfo) {
        if (newState == null)
            return R.error("执行状态表错误，无法执行对应数据更新");
        //插入工控机的当前执行操作【必须执行插入操作，然后进行ID的查询】
        executeState.stateMapper.insert(newState);
        Date currTime = new Date();
        if (newState.getMaId() == null) {
            return null;
        }
        //查询当前状态表--根据执行表中的设备maId
        SuperviseExecute currExecute = executeState.executeMapper.getExecuteOrder(newState.getMaId());
        Integer oldesId = currExecute.getEsId();//记录历史数据的es_id以免更新后被修改
        /**
         *  更新Supervise中的数据ex_id
         */
        currExecute.setWfId(newState.getWfId());//设定排产的班次id信息wfId主键信息
        currExecute.setWbId(newState.getWbId());
        currExecute.setSdId(newState.getSdId());
        currExecute.setUsIds(newState.getTeamId());
        if (newState.getUsId() != null) {
            currExecute.setOperator(newState.getUsId());
        }
        currExecute.setExeStatus(newState.getStatus());//设定当前状态信息
        currExecute.setEvent(newState.getEvent());//设定当前时间信息
        //判断执行表单数据不为空的时候才会更新对应执行单id信息
        if (executeInfo != null && executeInfo.getId() != null)
            currExecute.setExId(executeInfo.getId());//记录最后更新执行表的exid
        currExecute.setEsId(newState.getId());//记录最后更新执行表的esid
        currExecute.setUpdateAt(currTime);//记录更新时间
        //开始接单时初始化实时状态表计数信息
        if (newState.getEvent().equals(GlobalConstant.ProType.ACCEPT_EVENT.getType())) {
            SuperviseBoxinfo superviseBoxinfo = executeState.superviseBoxinfoMapper.getBoxInfoByMid(newState.getMaId());
            if (superviseBoxinfo != null) {
                int startNum = superviseBoxinfo.getNumberOfDay() == null ? 0 : superviseBoxinfo.getNumberOfDay();
                currExecute.setEndNum(startNum);
                currExecute.setStartNum(startNum);
                currExecute.setCurrNum(0);
            }
        }

        //正式生产的时候初始化实时状态表readyNum
        if (newState.getEvent().equals(GlobalConstant.ProType.PRODUCT_EVENT.getType())) {
            SuperviseBoxinfo superviseBoxinfo = executeState.superviseBoxinfoMapper.getBoxInfoByMid(newState.getMaId());
            if (superviseBoxinfo != null) {
                int readyNum = superviseBoxinfo.getNumberOfDay() == null ? 0 : superviseBoxinfo.getNumberOfDay();
                currExecute.setReadyNum(currExecute.getCurrNum());
                currExecute.setReadyTime(currTime);
            }
        }
        if (GlobalConstant.ProType.ACCEPT_EVENT.getType().equals(newState.getEvent())) {
            currExecute.setStartTime(currTime);
        }
        //对于当前订单的状态进行更新 更新状态表
        executeState.executeMapper.update(currExecute);

        //找到上一条数据;如果当前状态没有esId，就无法返回历史执行记录 更新是数据
        ExecuteState oldState = (oldesId != null) ? executeState.stateMapper.selectById(oldesId) : null;
        if (oldState == null) {
            return R.error("没有找到执行状态数据");
        }
        //更新执行状态表数据
        oldState.setEndAt(currTime);
        oldState.setDuration(DateUtil.calLastedTime(currTime.getTime(), oldState.getStartAt()));
        if (executeState.stateMapper.updateById(oldState) <= 0) {
            return R.error("更新执行状态数据失败");
        }
        return R.ok(DateUtil.calLastedTime(currTime.getTime(), oldState.getStartAt()));
    }


    /****
     * 上报时修改状态信息
     * @param newState
     * @return
     */
    public static R reportUpdateSupervise(SuperviseExecute currExecute, ExecuteState newState, ExecuteState oldState) {

        if (newState == null)
            return R.error("执行状态表错误，无法执行对应数据更新");
        //插入工控机的当前执行操作【必须执行插入操作，然后进行ID的查询】
        executeState.stateMapper.insert(newState);
        Date currTime = new Date();
        if (newState.getMaId() == null) {
            return null;
        }
        Integer oldEsId = oldState.getId();
        //是否为同一状态信息否则为前订单未上报后又接单
        if (currExecute.getEsId() != null && currExecute.getEsId().equals(oldEsId)) {

            /**
             *  更新Supervise中的数据es_id
             */
            currExecute.setWbId(newState.getWbId());
            currExecute.setSdId(newState.getSdId());
            currExecute.setUsIds(newState.getTeamId());
            currExecute.setOperator(newState.getUsId());
            currExecute.setExeStatus(newState.getStatus());//设定当前状态信息
            currExecute.setEvent(newState.getEvent());//设定当前时间信息
            currExecute.setEsId(newState.getId());//记录最后更新执行表的id
            currExecute.setUpdateAt(currTime);//记录更新时间
            //开始接单时初始化实时状态表计数信息
            if (newState.getStatus().equals("C") || newState.getStatus().equals("B")) {
                SuperviseBoxinfo superviseBoxinfo = executeState.superviseBoxinfoMapper.getBoxInfoByMid(newState.getMaId());
                if (superviseBoxinfo != null) {
                    int startNum = superviseBoxinfo.getNumberOfDay() == null ? 0 : superviseBoxinfo.getNumberOfDay();
                    currExecute.setEndNum(startNum);
                    currExecute.setStartNum(startNum);
                    currExecute.setCurrNum(0);
                }
            }
        }
        //对于当前订单的状态进行更新 更新状态表
        if (executeState.executeMapper.updateById(currExecute) <= 0) {
            return R.error("更新订单实时状态失败");
        }

        //找到上一条数据;如果当前状态没有esId，就无法返回历史执行记录 更新是数据
        if (oldState == null) {
            return R.error("没有找到执行状态数据");
        }
        //更新执行状态表数据
        oldState.setEndAt(currTime);
        oldState.setDuration(DateUtil.calLastedTime(currTime.getTime(), oldState.getStartAt()));
        if (executeState.stateMapper.updateById(oldState) <= 0) {
            return R.error("更新执行状态数据失败");
        }
        return R.ok(DateUtil.calLastedTime(currTime.getTime(), oldState.getStartAt()));
    }
}
