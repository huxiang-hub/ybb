package com.sso.utils;


import com.sso.mapper.ExecuteStateMapper;
import com.sso.mapper.SuperviseExecuteMapper;
import com.sso.supervise.entity.ExecuteState;
import com.sso.supervise.entity.SuperviseExecute;
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
public class UpdateStateUtils {

    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private SuperviseExecuteMapper executeMapper;

    //单位毫秒
    public static long TIMEUNIT = 1 * 60 * 1000;

    private static UpdateStateUtils executeState;

    @PostConstruct
    public void init() {
        executeState = this;
        executeState.stateMapper = this.stateMapper;
        executeState.executeMapper = this.executeMapper;
    }


    /****
     * 需要插入状态表信息，并且更新当前状态表，并且需要按照顺序来进行执行。
     * @param newState
     * @return
     */
    public synchronized static R updateSupervise(ExecuteState newState) {
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
         *  更新Supervise中的数据es_id
         */
        currExecute.setWbId(newState.getWbId());
        currExecute.setSdId(newState.getSdId());
        currExecute.setUsIds(newState.getTeamId());
        currExecute.setOperator(newState.getUsId());
        currExecute.setEsId(newState.getId());//记录最后更新执行表的id
        currExecute.setUpdateAt(currTime);//记录更新时间
        //如为A 只有实时表状态为null和D2才更新为A1
        if (newState.getStatus().equals(GlobalConstant.ProType.PERSONNEL_STATUS.getType())) {
//            //报空指针错误信息 新增加  currExecute.getEvent()判断为空的问题
//            if (currExecute != null && currExecute.getEvent() != null && (GlobalConstant.ProType.REPORT_EVENT.getType().equals(currExecute.getEvent()) || currExecute.getExeStatus() == null)) {
//                //设定当前状态信息
//                currExecute.setExeStatus(newState.getStatus());
//                //设定当前时间信息
//                currExecute.setEvent(newState.getEvent());
//            }
        } else {
            //设定当前状态信息
            currExecute.setExeStatus(newState.getStatus());
            //设定当前时间信息
            currExecute.setEvent(newState.getEvent());
        }
        //对于当前订单的状态进行更新 更新状态表
        if (executeState.executeMapper.updateById(currExecute) <= 0) {
            return R.error("更新订单实时状态失败");
        }


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
}
