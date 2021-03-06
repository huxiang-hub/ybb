package com.yb.timer;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchOrdoeeMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private ExecuteStateMapper executeStateMapper;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;

    static long nd = 1000 * 24 * 60 * 60;
    static long nh = 1000 * 60 * 60;

    /**
     * ?????????????????????????????? ????????????
     */
//    @Scheduled(cron = "0 0 * * * ?")
    public void task() {
        log.info("??????????????????????????????????????????");

        DBIdentifier.setProjectCode("xingyi");
        //realTimeRate();
        log.info("??????????????????????????????????????????");
    }


    private void saveOrdReach(Date nowDate, int targetHour, int targetMin, String targetDay, Integer maId, MachineMainfo machineMainfo, ExecuteInfo executeInfo, Integer currNum, BigDecimal planNum, BigDecimal reachRate, WorkbatchOrdlink workbatchOrdlink, WorkbatchShift workbatchShift) {
        StatisOrdreach statisOrdreach = new StatisOrdreach();
        statisOrdreach.setTargetMin(targetMin);
        statisOrdreach.setTargetHour(targetHour);
        statisOrdreach.setTargetDay(targetDay);
        statisOrdreach.setSdId(executeInfo.getSdId().toString());
        statisOrdreach.setExId(executeInfo.getId());
        statisOrdreach.setMaId(maId);
        statisOrdreach.setMaName(machineMainfo.getName());
        statisOrdreach.setReachRate(reachRate);
        statisOrdreach.setPlanNum(planNum+"");
        statisOrdreach.setRealCount(currNum);
        statisOrdreach.setPdName(workbatchOrdlink.getPdName());
        statisOrdreach.setWbNo(workbatchOrdlink.getWbNo());
        statisOrdreach.setWsId(workbatchShift.getId());
        statisOrdreach.setWsName(workbatchShift.getCkName());
        statisOrdreach.setCreateAt(nowDate);
        statisOrdreachMapper.insert(statisOrdreach);
    }


//    private void realTimeRate() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        // ??????24????????????
//        int targetHour = calendar.get(Calendar.HOUR_OF_DAY);
//        // ???
//        int targetMin = calendar.get(Calendar.MINUTE);
//        //?????????????????????
//        Date qDate = DateTimeUtil.getBeforeByHourTime(1);
//        Date nowDate = new Date();
//        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
//        if (targetMin > 55 || targetMin < 5) {
//            targetMin = 0;
//        }
//        if (targetMin > 40 && targetMin < 50) {
//            targetMin = 45;
//        }
//        if (targetMin > 25 && targetMin < 35) {
//            targetMin = 30;
//        }
//        if (targetMin > 10 && targetMin < 20) {
//            targetMin = 15;
//        }
//        List<SuperviseBoxinfo> superviseBoxInfoVOList = superviseBoxinfoMapper.findByStatus();
//        if (superviseBoxInfoVOList.isEmpty()) {
//            log.info("????????????????????????");
//            return;
//        }
//
//        for (SuperviseBoxinfo superviseBoxinfo : superviseBoxInfoVOList) {
//            Integer maId = superviseBoxinfo.getMaId();
////
////            //?????????????????????????????????
////            List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.findByMaId(maId);
////            for (int i = 0; i < workbatchOrdlinks.size(); i++) {
////                WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinks.get(i);
////                //???????????????
////                Integer currNum = 0;
////                //???????????????
////                BigDecimal planNum = BigDecimal.ZERO;
////                BigDecimal prePlanTime = BigDecimal.ZERO;
////                StatisOrdreach statisOrdreach = statisOrdreachMapper.getByMaIdAndSdIdAndTargetHour(maId, workbatchOrdlink.getId(), nowDate.getHours());
////                //??????????????????
////                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(workbatchOrdlink.getId());
////                //?????????????????????
////                BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
////                //????????????
////                Integer prepareTime = workbatchOrdoee.getMouldStay() * workbatchOrdoee.getMouldNum();
////                ExecuteInfo executeInfo = infoMapper.getByMaIdAndSdId(maId, workbatchOrdlink.getId());
////                //????????????????????????
////                if (executeInfo == null) {
////                    //????????????????????????
////                    Date startTime = workbatchOrdlink.getStartTime();
////                }
////            }
////        }
//
//
//        List<ExecuteInfo> executeInfoList = infoMapper.findByStartTimeAndEndTimeAndMaId(qDate, nowDate, maId);
//        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
//        if (!executeInfoList.isEmpty()) {
//            log.info("???????????????????????????????????????:maid:{}", maId);
//            SuperviseExecute executeOrder = superviseExecuteMapper.getExecuteOrder(maId);
//            for (ExecuteInfo executeInfo : executeInfoList) {
//                //???????????????
//                Integer currNum = 0;
//                //???????????????
//                BigDecimal planNum = BigDecimal.ZERO;
//                BigDecimal prePlanTime = BigDecimal.ZERO;
//                //??????????????????
//                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(executeInfo.getSdId());
//                //?????????????????????
//                BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
//                ExecuteBriefer executeBriefer = brieferMapper.getByExId(executeInfo.getId());
//                //???????????????????????????
//                if (executeInfo.getEndTime() != null) {
//                    if (executeBriefer != null) {
//                        currNum = executeBriefer.getBoxNum();
//                    }
//                } else {
//                    //?????????????????????????????????
//                    currNum = executeOrder.getCurrNum();
//                }
//                //?????????????????????
//                WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(executeInfo.getSdId());
//                //??????????????????
//                WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(workbatchOrdlink.getWsId());
//                //????????????????????????
//                double preDiffTime = 0;
//                //????????????
//                Date endTime = executeInfo.getEndTime();
//                //????????????
//                Integer prepareTime = workbatchOrdoee.getMouldStay() * workbatchOrdoee.getMouldNum();
//                if (endTime == null) {
//                    preDiffTime = DateUtil.calLastedTime(nowDate.getTime(), workbatchOrdlink.getStartTime());
//                    //??????????????????/???
//                    preDiffTime = preDiffTime / 60;
//                    prePlanTime = BigDecimal.valueOf(preDiffTime - prepareTime);
//                    if (prePlanTime.intValue() < 0) {
//                        prePlanTime = BigDecimal.ZERO;
//                    }
//                } else {
//                    //?????????????????????
//                    prePlanTime = BigDecimal.valueOf(workbatchOrdoee.getPlanTotalTime());
//                }
//                //??????????????????
//                planNum = prePlanTime.multiply(planSpeed);
//                //???????????????
//                BigDecimal reachRate = BigDecimal.valueOf(currNum).divide(planNum, 2, BigDecimal.ROUND_HALF_UP);
//                saveOrdReach(nowDate, targetHour, targetMin, targetDay, maId, machineMainfo, executeInfo, currNum, planNum, reachRate, workbatchOrdlink, workbatchShift);
//            }
//        } else {
//            log.info("??????????????????????????????????????????:maid:{}", maId);
//            //??????????????????,??????????????????????????????
//            ExecuteInfo executeInfo = infoMapper.getByMaId(maId);
//            if (executeInfo != null) {
//                SuperviseExecute executeOrder = superviseExecuteMapper.getExecuteOrder(maId);
//                //?????????????????????
//                WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(executeInfo.getSdId());
//                if (workbatchOrdlink == null) {
//                    log.info("??????????????????????????????????????????:maid:{}, exId:{}", maId, executeInfo.getId());
//                    continue;
//                }
//                //??????????????????
//                WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(workbatchOrdlink.getWsId());
//                //??????????????????
//                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(executeInfo.getSdId());
//                //?????????????????????
//                Integer currNum = executeOrder.getCurrNum();
//                //?????????????????????realTimeRate
//                BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
//                //????????????????????????/???
//                double preDiffTime = 0;
//                preDiffTime = DateUtil.calLastedTime(nowDate.getTime(), workbatchOrdlink.getStartTime()) / 60;
//                //??????????????????/???
//                Integer prepareTime = workbatchOrdoee.getMouldStay() * workbatchOrdoee.getMouldNum();
//                BigDecimal prePlanTime = BigDecimal.valueOf(preDiffTime - prepareTime);
//                if (prePlanTime.intValue() < 0) {
//                    prePlanTime = BigDecimal.ZERO;
//                }
//                //??????????????????
//                BigDecimal planNum = prePlanTime.multiply(planSpeed);
//                //???????????????
//                BigDecimal reachRate = BigDecimal.valueOf(currNum).divide(planNum, 2, BigDecimal.ROUND_HALF_UP);
//                saveOrdReach(nowDate, targetHour, targetMin, targetDay, maId, machineMainfo, executeInfo, currNum, planNum, reachRate, workbatchOrdlink, workbatchShift);
//            }
//        }
//    }
//
//}



//    @Scheduled(cron = "0 50 7,19 ? * * ")
//    @Scheduled(cron = "0 0/1 * * * ?")
    private void initialTimeRate() throws ParseException {
        log.info("??????????????????????????????:[hour:{}]", new Date().getHours());
        DBIdentifier.setProjectCode("xingyi");
        Date nowDate = new Date();
        long ctime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatTime = sdf.format(nowDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date classStartTime = null;
        Date classEndTime = null;

        List<WorkbatchShiftset> WorkBatchShiftsetList = workbatchShiftsetMapper.getList();
        WorkbatchShiftset workbatchShiftset = new WorkbatchShiftset();
        for (WorkbatchShiftset shiftset : WorkBatchShiftsetList) {
            String staTime = simpleDateFormat.format(shiftset.getStartTime());
            Date startTime = simpleDate.parse(formatTime + (" ") + staTime);
            String eTime = simpleDateFormat.format(shiftset.getEndTime());
            Date endTime = simpleDate.parse(formatTime + (" ") + eTime);
            classStartTime = startTime;
            //??????
            if (endTime.getTime() < startTime.getTime()) {
                endTime = DateTimeUtil.getNextDay(endTime);
                classEndTime = endTime;
            }
            if (startTime.getTime() <= nowDate.getTime() && nowDate.getTime() <= endTime.getTime()) {
                workbatchShiftset = shiftset;
            }
        }
        List<WorkbatchShift> workBatchShifts = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("ws_id", workbatchShiftset.getId()));
        if (workBatchShifts.isEmpty()) {
            log.info("?????????????????????");
            return;
        }
        List<Integer> workBatchShiftIds = workBatchShifts.stream().map(WorkbatchShift::getId).collect(Collectors.toList());
        //List<WorkbatchOrdlink> workBatchOrdlinks = workbatchOrdlinkMapper.selectList(new QueryWrapper<WorkbatchOrdlink>().in("ws_id", workBatchShiftIds).orderByAsc("sd_sort"));
        List<WorkbatchOrdlinkShiftVO> workBatchOrdlinks =workbatchOrdlinkMapper.selectBatchWsIds(workBatchShiftIds);
        //??????????????????????????????
        Map<Integer, List<WorkbatchOrdlinkShiftVO>> collect = workBatchOrdlinks.stream().collect(Collectors.groupingBy(WorkbatchOrdlinkShiftVO::getMaId));


        for (Map.Entry<Integer, List<WorkbatchOrdlinkShiftVO>> m : collect.entrySet()) {
            Date endTime = null;
            Integer maId = null;
            for (int i = 0; i < m.getValue().size(); i++) {
                WorkbatchOrdlinkShiftVO workbatchOrdlink = m.getValue().get(i);
                //????????????
                MachineMainfo machineMainfo = machineMainfoMapper.selectById(workbatchOrdlink.getMaId());
                //??????oee????????????
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(workbatchOrdlink.getId());
                //????????????
                if (i == 0) {
                    workbatchOrdlink.setStartTime(classStartTime);
                    endTime = new Date(classStartTime.getTime() + (workbatchOrdoee.getPlanTotalTime() * 60 * 1000));
                    workbatchOrdlink.setCloseTime(endTime);
                }
                //??????????????????????????????????????????
                if (workbatchOrdlink.getMaId().equals(maId) && i != 0) {
                    workbatchOrdlink.setStartTime(endTime);
                    endTime = new Date(endTime.getTime() + (workbatchOrdoee.getPlanTotalTime() * 60 * 1000));
                    workbatchOrdlink.setCloseTime(endTime);
                }
                StatisOrdreach statisOrdreach = new StatisOrdreach();
                statisOrdreach.setMaName(machineMainfo.getName());
                statisOrdreach.setMaId(machineMainfo.getId());
                statisOrdreach.setWsId(workbatchOrdlink.getWsId());
                statisOrdreach.setWsName(workbatchShiftset.getCkName());
                statisOrdreach.setPdName(workbatchOrdlink.getPdName());
                statisOrdreach.setSdId(workbatchOrdlink.getId().toString());
                statisOrdreach.setStartTime(workbatchOrdlink.getStartTime());
                statisOrdreach.setEndTime(workbatchOrdlink.getCloseTime());
                statisOrdreach.setPlanNum(workbatchOrdlink.getPlanNum()+"");
                statisOrdreach.setTargetHour(workbatchOrdlink.getStartTime().getHours());
                statisOrdreach.setTargetDay(DateTimeUtil.format(workbatchOrdlink.getStartTime(), DateTimeUtil.DEFAULT_DATE_FORMATTER));
                statisOrdreach.setEndTime(workbatchOrdlink.getCloseTime());
                statisOrdreach.setCreateAt(new Date());
                maId = statisOrdreach.getMaId();
                statisOrdreachMapper.insert(statisOrdreach);
            }
        }
        log.info("??????????????????????????????:");
    }

    /****
     * ???????????????????????????????????????????????? ????????????
     * @param classStartTime
     * @param classEndTime
     * @param sdDate
     * @return
     */
    public Date[] setWsDatetime(Date classStartTime, Date classEndTime, String sdDate) {
        Date[] wsDatetime = new Date[2];
        //?????????????????????????????????????????????????????????????????????
        if (classStartTime.compareTo(classEndTime) > 0) {
            classStartTime = DateUtil.toDate(sdDate + " " + DateUtil.format(classStartTime, "HH:mm:ss"), null);
            classEndTime = DateUtil.toDate(sdDate + " " + DateUtil.format(classEndTime, "HH:mm:ss"), null);
            classEndTime = DateUtil.addDayForDate(classEndTime, 1);
        }
        wsDatetime[0] = classStartTime;
        wsDatetime[1] = classEndTime;
        return wsDatetime;
    }
}

