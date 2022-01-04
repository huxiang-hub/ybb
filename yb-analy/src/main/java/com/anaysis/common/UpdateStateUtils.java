package com.anaysis.common;

import com.anaysis.executSupervise.entity.ExecuteState;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.mapper.ExecuteStateMapper;
import com.anaysis.executSupervise.mapper.SuperviseBoxinfoMapper;
import com.anaysis.executSupervise.mapper.SuperviseExecuteMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.core.tool.api.R;
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
     * @param num 设备当前计数
     * @return
     */
    public static R updateSupervise(ExecuteState newState, Integer num) {
        if (newState == null)
            return R.fail("执行状态表错误，无法执行对应数据更新");
        //插入工控机的当前执行操作【必须执行插入操作，然后进行ID的查询】
        executeState.stateMapper.insert(newState);
        Date currTime = new Date();

        //查询当前状态表--根据执行表中的设备maId
        SuperviseExecute currExecute = executeState.executeMapper.getExecutByMaid(newState.getMaId());
        Integer oldesId = currExecute.getEsId();//记录历史数据的es_id以免更新后被修改

        if (!currExecute.getExeStatus().equalsIgnoreCase(newState.getStatus())) {
            currExecute.setStartTime(currTime);//当status状态有变化的时候更新开始时间starttime
        }

        /**
         *  更新Supervise中的数据es_id
         */
        currExecute.setWbId(newState.getWbId());
        currExecute.setSdId(newState.getSdId());
        currExecute.setExeStatus(newState.getStatus());//设定当前状态信息
        currExecute.setEvent(newState.getEvent());//设定当前时间信息
        currExecute.setEsId(newState.getId());//记录最后更新执行表的id
        currExecute.setUpdateAt(currTime);//记录更新时间
        //开始接单时初始化实时状态表计数信息
        SuperviseBoxinfo superviseBoxinfo = executeState.superviseBoxinfoMapper.getBoxInfoByBno(currExecute.getUuid());
//        //设备接单状态才更新当前计数
//        if (currExecute.getSdId() != null) {
//            int endNum = superviseBoxinfo.getNumberOfDay();
//            currExecute.setEndNum(endNum);
//            Integer startNum = currExecute.getStartNum() == null ? 0 : currExecute.getStartNum();
//            currExecute.setCurrNum(endNum - startNum);
//        }

        //对于当前订单的状态进行更新 更新状态表
        if (executeState.executeMapper.updateById(currExecute) <= 0) {
            return R.fail("更新订单实时状态失败");
        }

        //找到上一条数据;如果当前状态没有esId，就无法返回历史执行记录 更新是数据
        ExecuteState oldState = (oldesId != null) ? executeState.stateMapper.selectById(oldesId) : null;
        if (oldState == null) {
            return R.fail("没有找到执行状态数据");
        }
        //更新执行状态表数据
        oldState.setEndAt(currTime);
        oldState.setDuration(DateUtil.calLastedTime(currTime.getTime(), oldState.getStartAt()));
        if (executeState.stateMapper.updateById(oldState) <= 0) {
            return R.fail("更新执行状态数据失败");
        }
        return R.success("ok");
    }


    public static R updateSuperviseExecute(SuperviseBoxinfo superviseBoxinfo,String uuid, String status, Integer num) {
        SuperviseExecute superviseExecute = executeState.executeMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("uuid", uuid));
        superviseExecute.setUpdateAt(new Date());
        //开始接单时初始化实时状态表计数信息
        if (superviseBoxinfo != null) {
            int startNum = superviseBoxinfo.getNumberOfDay();
            superviseExecute.setEndNum(startNum);
            Integer startNumber = superviseExecute.getStartNum() == null ? 0 : superviseExecute.getStartNum();
            Integer curNum = startNum - startNumber;
            if (curNum < 0) {
                //清零后
                curNum = curNum + superviseBoxinfo.getClearNum();
            }
            superviseExecute.setCurrNum(curNum);
        }
        executeState.executeMapper.updateById(superviseExecute);
        return R.success("ok");
    }


    public synchronized static R updateSuperviseExecuteDownStatus(String uuid) {
        SuperviseExecute superviseExecute = executeState.executeMapper.selectOne(new QueryWrapper<SuperviseExecute>().eq("uuid", uuid));
        superviseExecute.setEvent(GlobalConstant.ProType.DOWNTIME_EVENT.getType());
        superviseExecute.setExeStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
        superviseExecute.setUpdateAt(new Date());
        executeState.executeMapper.updateById(superviseExecute);
        return R.success("ok");
    }

}
