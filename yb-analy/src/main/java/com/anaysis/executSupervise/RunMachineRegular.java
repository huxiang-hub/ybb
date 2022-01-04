package com.anaysis.executSupervise;

import com.anaysis.common.DateUtil;
import com.anaysis.common.MachineConstant;
import com.anaysis.common.SpringUtil;
import com.anaysis.executSupervise.entity.ExecuteState;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.entity.SuperviseExerun;
import com.anaysis.executSupervise.entity.SuperviseRunregular;
import com.anaysis.executSupervise.service.*;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

public class RunMachineRegular {

    SuperviseBoxinfoService boxinfoService = (SuperviseBoxinfoService) SpringUtil.getBean(SuperviseBoxinfoService.class);//盒子实时信息
    SuperviseExerunService exerunService = (SuperviseExerunService) SpringUtil.getBean(SuperviseExerunService.class);//执行
    SuperviseRunregularService runregularService = (SuperviseRunregularService) SpringUtil.getBean(SuperviseRunregularService.class);//执行
    SuperviseExecuteService supexecuteService = (SuperviseExecuteService) SpringUtil.getBean(SuperviseExecuteService.class);
    ExecuteStateService executeStateService = (ExecuteStateService) SpringUtil.getBean(ExecuteStateService.class);

    RunMachineRegular() {
        //setInit();
    }

    public void setInit() {
        Date currtime = new Date();
        exerunService.clearByalreadylog();//清理已经完成log提示信息的设备关联信息
        List<SuperviseBoxinfoVo> boxinfoList = boxinfoService.getListByrun();//获取机台的开机状态数据信息
        //获取历史数据由
        List<SuperviseExerun> exerunList = exerunService.getHisexerun();
        if (boxinfoList != null) {

            for (SuperviseBoxinfoVo boxinfo : boxinfoList) {
                if (exerunList == null)
                    saveExerun(boxinfo, currtime);//查询开机状态信息进行缓存数据插入
                else {
                    //判断如果再历史表中，就不需要再进行数据插入操作了，否则就执行
                    for (SuperviseExerun exerun : exerunList) {
                        if (exerun.getUuid().equalsIgnoreCase(boxinfo.getUuid())) {
                            continue;//历史数据包含uuid的设备，就无需再添加
                        } else
                            saveExerun(boxinfo, currtime);
                    }
                }
            }
        }
    }

    /****
     * 检查uuid的对应信息内容
     */
    public void checkByuuid(SuperviseBoxinfo boxinfo, String uuid, String status, Long ctime) {
        Date currtime = new Date(ctime);
        SuperviseBoxinfoVo boxinfoVo = boxinfoService.getBoxVoByuuid(uuid);

        //设备当前状态是运行
        if (MachineConstant.MA_RUN.equalsIgnoreCase(status)) {//转化成1运行进行判断
            //更新对应uuid信息内容
            updateExerun(boxinfoVo, currtime);
        } else {
            //判定历史状态为运行，就清除该条记录
            if (MachineConstant.MA_RUN.equalsIgnoreCase(boxinfo.getStatus())) {
                setExeRunregular(boxinfoVo, currtime);
                exerunService.clearByuuid(uuid);
            }
        }

    }

    /****
     * 保存执行过程的新插入数据。
     * @param boxinfo
     * @param currtime
     */
    private void saveExerun(SuperviseBoxinfoVo boxinfo, Date currtime) {
        SuperviseExerun exerun = new SuperviseExerun();
        exerun.setUuid(boxinfo.getUuid());
        exerun.setMaId(boxinfo.getMaId());
        exerun.setSdId(boxinfo.getSdId());
        exerun.setNumber(boxinfo.getNumber());
        exerun.setStartTime(currtime);
        exerun.setRegular(boxinfo.getKeepRun());//设定规则运行5分钟
        exerun.setStatus(0);//设定初始化值信息
        exerun.setUsIds(boxinfo.getUsIds());//设定用户信息
        exerun.setCreateAt(currtime);
        exerunService.insertByuuid(exerun);
    }

    /****
     * 保存执行过程的新插入数据。
     * @param boxinfo
     * @param currtime
     */
    private void updateExerun(SuperviseBoxinfoVo boxinfo, Date currtime) {
        //先查询缓存数据库保存设备的数据信息
        SuperviseExerun exerun = exerunService.getByuuid(boxinfo.getUuid());
        if (exerun != null) {
            exerun.setUuid(boxinfo.getUuid());
            exerun.setEndTime(currtime);
            int secnum = DateUtil.calLastedTime(currtime.getTime(), exerun.getStartTime());
            exerun.setStayTime(secnum);//停留的秒数差异
            int regulartime = exerun.getRegular() * 60;//规则分钟转化为秒数
            int overTime = secnum - regulartime;//停留时间大于规则时间
            if (overTime > 0)
                exerun.setOverTime(overTime / 60); //超过了才会记录数据，超过多少分钟
            exerun.setUpdateAt(currtime);//设置最后更新时间
            //超过规则时间X分钟，存入log日志表信息内容C1的操作信息
            if (overTime > 0) {
                setExestateC1(boxinfo, currtime);//更新状态表C1和订单实时表信息
                exerun.setStatus(1);//已经记录log表中，设定为1状态。
                setExeRunregular(boxinfo, currtime);//执行新增规则记录表
            }
            exerunService.updateByuuid(exerun);
        } else {
            saveExerun(boxinfo, currtime);
        }
    }


    /***
     * 设置C1的执行表操作，并且更新实时订单表信息
     * @param boxinfo
     * @param currtime
     */
    private void setExestateC1(SuperviseBoxinfoVo boxinfo, Date currtime) {
        //判断该状态是否已经有C1操作，若已有则跳过。
        ExecuteState query1 = new ExecuteState();
        query1.setMaId(boxinfo.getMaId());
        query1.setSdId(boxinfo.getSdId());
        List<ExecuteState> executC1 = executeStateService.getExecutC1(query1);
        if (executC1 != null) {//表示获取最后一个c1、b1的状态。
            ExecuteState c1 = null;
            ExecuteState b1 = null;
            //循环状态表查出来的最后一个b1和c1的操作，如果没有c1就写入C1，如果有就跳出不做操作
            for (ExecuteState state : executC1) {
                if (state != null && state.getEvent().equalsIgnoreCase("C1")) {
                    c1 = state;
                } else if (state != null && state.getEvent().equalsIgnoreCase("B1")) {
                    b1 = state;
                }
            }
            if (b1 == null)
                return;
            //判断没有C1的操作，就新增插入C1的执行操作行为
            if (c1 == null) {
                //插入C1的操作
                ExecuteState state = b1;
                state.setStatus("C");
                state.setEvent("C1");
                state.setStartAt(currtime);
                state.setEndAt(null);
                //state.setUsId(b1.getUsId());//系统写入用户信息
                state.setCreateAt(currtime);
                executeStateService.saveState(state);//新增C1的执行表操作
                //更改订单实时状态表信息
                supexecuteService.updateC1Byuuid(boxinfo, state);

                //设定更改B1的结束状态信息
                if (b1 != null) {
                    //插入C1后更新B1的结束操作
                    ExecuteState stateb1 = b1;
                    int duration = DateUtil.calLastedTime(currtime.getTime(), stateb1.getStartAt());
                    stateb1.setEndAt(currtime);
                    stateb1.setDuration(duration);//设定B1的结束时间是多少，并且区间时间间隔多少秒
                    executeStateService.updateState(stateb1);//更新C1的操作
                }
            } else {
                //如果已经存在C1的状态便无需更新
                return;
            }
        }
    }

    /*******
     * 设定执行规则
     * @param boxinfo
     * @param currtime
     */
    private void setExeRunregular(SuperviseBoxinfoVo boxinfo, Date currtime) {
        SuperviseRunregular runregular = runregularService.getByuuid(boxinfo.getUuid());
        if (runregular == null) {//表示超期了，但是log里面还没有记录规则信息，第一次增加到运行规则表中
            //设定数据信息
            runregular = new SuperviseRunregular();
            runregular.setUuid(boxinfo.getUuid());
            runregular.setMaId(boxinfo.getMaId());
            runregular.setSdId(boxinfo.getSdId());
            runregular.setNumber(boxinfo.getNumberOfDay());
            runregular.setStartTime(currtime);
            runregular.setRegular(boxinfo.getKeepRun());
            runregular.setUsIds(boxinfo.getUsIds());
            runregular.setStatus(0);//设置记录信息
            runregular.setIsWaring(0);//设置没有预警信息
            runregular.setTargetDay(DateUtil.toDatestr(currtime, "yyyy-MM-dd"));
            runregular.setTargetHour(currtime.getHours());
            runregular.setTargetMin(currtime.getMinutes());
            runregular.setCreateAt(currtime);
            runregularService.saveRunregular(runregular);
        } else {
            //如果已经存在了，就做更新操作
            updateRunregular(boxinfo, currtime);
        }
    }

    /*****
     * 更新到运行状态转化信息了。
     * @param boxinfo
     * @param currtime
     */
    private void updateRunregular(SuperviseBoxinfoVo boxinfo, Date currtime) {
        SuperviseRunregular runregular = runregularService.getByuuid(boxinfo.getUuid());
        if (runregular != null) {
            runregular.setEndTime(currtime);
            int staytime = DateUtil.calLastedTime(currtime.getTime(), runregular.getStartTime());
            runregular.setStayTime(staytime);
            int overtime = staytime - runregular.getRegular() * 60;
            runregular.setOverTime(overtime / 60);//超过分钟处理
            runregular.setStatus(0);//设定记录
            runregular.setUpdateAt(currtime);//设定更新当前时间
            runregularService.updateRunregular(runregular);
        }
    }
}
