package com.anaysis.executSupervise;

import com.alibaba.fastjson.JSON;
import com.anaysis.common.*;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.entity.ExeDownCacheEntity;
import com.anaysis.entity.ExeQualityCacheEntity;
import com.anaysis.entity.ExeStatusChangeCacheEntity;
import com.anaysis.entity.ExecutProcessEntity;
import com.anaysis.executSupervise.entity.*;
import com.anaysis.executSupervise.mapper.*;
import com.anaysis.executSupervise.service.*;
import com.anaysis.fz.mapper.SuperviseIntervalMapper;
import com.anaysis.service.SuperviseRegularService;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.tool.ChatType;
import org.springblade.message.feign.ImMessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class ExecutCheck {
    //设置超时的时间，记录为离线状态；单位毫秒
    private static long timeunit = 60 * 1000;

    private static ExecutCheck check;

    @Autowired
    private ExecuteStateService executeState;
    @Autowired
    private ExecuteFaultService executeFault;
    @Autowired
    private ExecuteFaultMapper executeFaultMapper;
    @Autowired
    private ExecuteWasteService executeWaste;
    @Autowired
    private ExesetQualityService exesetQuality;
    @Autowired
    private ExesetFaultService exesetFault;

    @Autowired
    private SuperviseExecuteService executService;
    @Autowired
    private ImMessageClient imMessageClient;

    @Autowired
    private SuperviseRegularService superviseRegularService;

    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;

    @Autowired
    private SuperviseIntervalMapper superviseIntervalMapper;

    @Autowired
    private SuperviseExestopMapper superviseExestopMapper;

    @Autowired
    private SuperviseStoplogMapper superviseStoplogMapper;

    @Autowired
    private SuperviseStopregularMapper superviseStopregularMapper;

    @Autowired
    private SuperviseQualityMapper superviseQualityMapper;

    @Autowired
    private SuperviseQualityregularMapper superviseQualityregularMapper;

    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;

    @Autowired
    private QualityBfwasteMapper qualityBfwasteMapper;

    @PostConstruct
    public void init() {
        check = this;
        check.executService = executService;
        check.executeState = executeState;
        check.executeFault = executeFault;
        check.executeWaste = executeWaste;
        check.exesetQuality = exesetQuality;
        check.exesetFault = exesetFault;
        check.imMessageClient = imMessageClient;
        check.superviseRegularService = superviseRegularService;
        check.superviseRegularMapper = superviseRegularMapper;
        check.superviseIntervalMapper = superviseIntervalMapper;
        check.superviseExestopMapper = superviseExestopMapper;
        check.superviseStoplogMapper = superviseStoplogMapper;
        check.superviseStopregularMapper = superviseStopregularMapper;
        check.executeFaultMapper = executeFaultMapper;
        check.superviseQualityMapper = superviseQualityMapper;
        check.superviseQualityregularMapper = superviseQualityregularMapper;
        check.qualityBfwasteMapper = qualityBfwasteMapper;
    }


    /**
     * 停机检测
     *
     * @param execute
     * @param uuid
     * @param status
     * @param num
     */
    public void checkDown(SuperviseExecute execute, String uuid, String status, Integer num) {
        log.info("停机检测开始:[uuid:{}, status:{}]", uuid, status);
        if (execute == null) {
            log.info("停机检测完成，实时状态表信息不存在:[uuid:{},mysql:{}]", uuid, DBIdentifier.getProjectCode());
            return;
        }
        //判定该设备执行订单状态不是正在执行的状态
        // ExecutProcessEntity expro = ExecutConstant.machineProc.get(uuid);
//        if (expro == null) {
//            log.info("停机检测完成，设备不处于正式执行状态:[uuid:{}]", uuid);
//            return;
//        }
        //获取停机缓存
        SuperviseExestop exeStopCache = check.superviseExestopMapper.getByUuid(uuid);
        //获取实时状态信息
        if (execute == null) {
            log.info("停机检测完成，该设备未绑定生产工单状态:[uuid:{}]", uuid);
            return;
        }
        //存储订单信息（订单名称和订单编号）集合
        Map<String, String> map = new HashMap<>();
        //获取订单名称、编号信息。
        if (execute.getSdId() != null) {
            map = check.executService.getOrderName(execute.getSdId());
        }
        //当前设备生产的所有登录人员
        String usIds = execute.getUsIds();
        Date currTime = new Date();
        //缓存存在
        if (exeStopCache != null) {
            Integer regularTime = exeStopCache.getRegular() == null ? 5 : exeStopCache.getRegular();
            exeStopCache.setRegular(regularTime);
            exeStopCache.setStayTime(DateUtil.duration(exeStopCache.getStartTime()));
            Calendar calendar = Calendar.getInstance();
            int targetHour = calendar.get(Calendar.HOUR_OF_DAY); // 时（24小时制）
            int targetMin = calendar.get(Calendar.MINUTE); // 分
            String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
            if (PopConstant.DOWN_STATUS.equals(status)) {
                //判断超过弹出框的时间 ,并且没有弹过窗口
                if (exeStopCache.getStartTime() != null && DateUtil.diffNowTime(exeStopCache.getStartTime().getTime(),
                        5 * timeunit) && !exeStopCache.getPopDown()) {
                    log.info("超出停机弹窗时间，存在停机缓存:[uuid:{}]", uuid);
                    //获取故障表信息且修改
//                    ExecuteFault fault = check.executeFaultMapper.selectById(exeStopCache.getFaultId());
//                    fault.setModel(1);
//                    //已处理
//                    fault.setHandle(1);
//                    check.executeFault.updateById(fault);
                    //获取未处理的停机信息。（小红点）
                    //int redCount = check.executeFault.getDownCount(exeStopCache.getMaId());
                    //弹窗实体类
//                    PopUpEntity pop = new PopUpEntity();
//                    //订单信息
//                    OrderInfo info = new OrderInfo();
//                    //排产单信息
//                    info.setId(execute.getSdId());
//                    if ()
//                    info.setOrderName(map.get("odName"));
//                    info.setOrderNo(map.get("odNo"));
                    //弹窗类型：停机
//                    pop.setPopModel(PopConstant.DOWN_POP);
//                    //弹窗消失时间
//                    pop.setDisapper(expro.getFaultDisapper());
//                    //小红点
//                    pop.setRedCount(redCount);
//                    pop.setStartTime(execute.getStartTime());
//                    pop.setEndTime(currTime);
//                    //停机当前生产数量
//                    pop.setLimitNum(execute.getEndNum());
//                    pop.setEsId(execute.getEsId());
//                    pop.setOrderInfo(info);
                    //推送到机台的停机信息
//                    sendMsgToMac("maId" + String.valueOf(expro.getMaId()),
//                            JSON.toJSONString(pop), ChatType.MAC_DOWN.getType());
                    //改变弹窗状态
                    exeStopCache.setPopDown(true);
                    log.info("停机检测完成,超过弹出框时间，推送机台停机消息:[uuid:{}]", uuid);
                }
                //更新缓存
                check.superviseExestopMapper.updateById(exeStopCache);
                return;
            }
            //状态发生变化,且停机时间小于规则时间
            if (!DateUtil.diffNowTime(exeStopCache.getStartTime().getTime(), regularTime * timeunit)) {
                SuperviseStoplog superviseStoplog = new SuperviseStoplog();
                superviseStoplog.setCreateAt(currTime);
                superviseStoplog.setStartTime(exeStopCache.getStartTime());
                superviseStoplog.setEndTime(currTime);
                superviseStoplog.setMaId(execute.getMaId());
                superviseStoplog.setSdId(execute.getSdId());
                superviseStoplog.setUsIds(usIds);
                superviseStoplog.setUuid(uuid);
                superviseStoplog.setNumber(num);
                superviseStoplog.setTargetDay(targetDay);
                superviseStoplog.setTargetHour(targetHour);
                superviseStoplog.setTargetMin(targetMin);
                //持续时间
                int stayTime = DateUtil.duration(superviseStoplog.getStartTime());
                superviseStoplog.setStayTime(stayTime);
                check.superviseStoplogMapper.insert(superviseStoplog);
            } else {
                //状态发生变化,且停机时间大于规则时间
                SuperviseStopregular superviseStopregular = new SuperviseStopregular();
                superviseStopregular.setCreateAt(currTime);
                superviseStopregular.setStartTime(exeStopCache.getStartTime());
                superviseStopregular.setEndTime(currTime);
                superviseStopregular.setMaId(execute.getMaId());
                superviseStopregular.setSdId(execute.getSdId());
                superviseStopregular.setUsIds(usIds);
                superviseStopregular.setStatus(2);
                superviseStopregular.setIsWaring(1);
                superviseStopregular.setTargetDay(targetDay);
                superviseStopregular.setTargetHour(targetHour);
                superviseStopregular.setTargetMin(targetMin);
                superviseStopregular.setUuid(uuid);
                //持续时间
                int stayTime = DateUtil.duration(exeStopCache.getStartTime());
                superviseStopregular.setStayTime(stayTime);
                check.superviseStopregularMapper.insert(superviseStopregular);
            }
            Date startAt = exeStopCache.getStartTime();
            Integer duration = DateUtil.calLastedTime(currTime.getTime(), startAt);
            //更新停机间隔时间
            ExecuteFault executeFault = check.executeFaultMapper.selectById(exeStopCache.getFaultId());
            if (executeFault != null) {
                executeFault.setEndAt(currTime);
                executeFault.setDelayTime(duration);
                executeFault.setDuration(duration);
                check.executeFault.updateById(executeFault);
            }
            //手机推送消息,机台登录的所有人接收消息
//            PopUpEntity pop = new PopUpEntity();
//            pop.setStartTime(startAt);
//            pop.setDuration(duration.doubleValue());
//            //停机结束-----推送消息
//            sendMsgToUser(usIds, JSON.toJSONString(pop),
//                    ChatType.MAC_DOWN.getType());
            //状态变化清除缓存数据
            check.superviseExestopMapper.deleteById(exeStopCache);
            //更新状态为C5
            execute.setEvent("C5");
            check.executService.update(execute);
            log.info("停机检测结束设备状态变更，推送消息:[uuid:{}, status:{}]", uuid, status);
        } else {
            //新增停机缓存信息
            if (PopConstant.DOWN_STATUS.equals(status)) {
                SuperviseExestop superviseExestop = new SuperviseExestop();
                superviseExestop.setMaId(execute.getMaId());
                superviseExestop.setSdId(execute.getSdId());
                superviseExestop.setStartTime(currTime);
                superviseExestop.setUuid(uuid);
                superviseExestop.setUsIds(usIds);
                superviseExestop.setCreateAt(currTime);
                superviseExestop.setStatus(0);
                superviseExestop.setNumber(execute.getEndNum());
                //保存设备故障信息表
                ExecuteFault fault = new ExecuteFault();
                fault.setHandle(0);
                fault.setCreateAt(currTime);
                fault.setWay(2);
                fault.setStartAt(currTime);
                //获取排产单的班次信息
                if (GlobalConstant.ProType.PERSONNEL_STATUS.getType().equals(execute.getExeStatus()) || (GlobalConstant.ProType.AFTERPRO_STATUS.getType()).equals(execute.getExeStatus())) {
                    fault.setEfType(0);
                } else {
                    fault.setEfType(1);
                    //WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(execute.getWfId());
                    fault.setWfId(execute.getWfId());
                }
                //设置班次id
                fault.setWsId(workbatchShiftMapper.getWsIdByMaId(execute.getMaId()));
                fault.setMaId(execute.getMaId());
                check.executeFault.save(fault);
                superviseExestop.setFaultId(fault.getId());
                check.superviseExestopMapper.insert(superviseExestop);
                log.info("停机检测完成，设备离线新增缓存:[uuid:{}]:", uuid);
            }
        }
    }


    /**
     * 质检检测
     *
     * @param execute
     * @param uuid
     * @param status
     * @param num
     */
    public void checkQuality(SuperviseExecute execute, String uuid, SuperviseBoxinfo boxInfoEntity, String status, Integer num) {
        log.info("质监检测开始:[uuid:{}, status:{}]", uuid, status);
        ExecutProcessEntity expro = ExecutConstant.machineProc.get(uuid);
        if (expro == null) { //表示设备不是正式执行状态中
            log.info("质检弹窗结束,设备未处于正式执行状态:[uuid:{}]", uuid);
            return;
        }


        //判断是否已接单。如果没有接单就不会有质检弹窗。
        if (execute == null || execute.getSdId() == null || execute.getExeStatus() == null) {
            log.info("质检弹窗结束，未处于接单状态:[uuid:{}]", uuid);
            return;
        }
        Integer sdId = execute.getSdId();
        Integer prId = check.workbatchShiftMapper.getPrIdBySdId(sdId);
        Date currTime = new Date();
        //质量巡检缓存
        SuperviseQuality qualityCache = check.superviseQualityMapper.getByUuid(uuid);

        //获取订单名称
        Map<String, String> map = check.executService.getOrderName(execute.getSdId());
        String usIds = execute.getUsIds();
        //如果当前订单  是结束状态，就需要清除缓存
        if (GlobalConstant.ProType.AFTERPRO_STATUS.getType()
                .equals(execute.getExeStatus())) {
            if (qualityCache != null) {
                check.superviseQualityMapper.deleteById(qualityCache);
            }
            log.info("质检结束，当前订单为结束状态，清除质检缓存:[uuid:{}, sdId:{}]", uuid, execute.getSdId());
            return;
        }
        if (qualityCache != null) {
            //按照计数方式弹窗
            if (expro.getModel() != null && expro.getModel() == PopConstant.NUM_QUALITY) {
                if (boxInfoEntity.getNumberOfDay() - qualityCache.getNumber() >= expro.getLimitNum()) {
                    //发送弹出窗口消息,新增执行表数据
                    //发送弹出窗口消息1.把巡检的内容发给前台quality
                    ExecuteState state = new ExecuteState();
                    state.setMaId(expro.getMaId());
                    state.setWbId(execute.getWbId());
                    state.setSdId(execute.getSdId());
                    state.setTeamId(execute.getUsIds());
                    state.setUsId(execute.getOperator());
                    state.setStartAt(currTime);
                    state.setCreateAt(currTime);
                    state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
                    state.setEvent(GlobalConstant.ProType.QUALITY_EVENT.getType());
                    UpdateStateUtils.updateSupervise(state, num);
                    //质量巡检表插入一条数据
                    QualityBfwaste qualityBfwaste = new QualityBfwaste();
                    qualityBfwaste.setMaId(expro.getMaId());
                    qualityBfwaste.setCreateAt(new Date());
                    qualityBfwaste.setStartAt(execute.getStartTime());
                    qualityBfwaste.setEndNum(execute.getEndNum());
                    qualityBfwaste.setExId(execute.getExId());
                    qualityBfwaste.setUsId(execute.getOperator());
                    qualityBfwaste.setPrId(prId);
                    qualityBfwaste.setStartNum(execute.getStartNum());
                    qualityBfwaste.setReportStatus(0);
                    check.qualityBfwasteMapper.insert(qualityBfwaste);
//                    ExecuteWaste qualityWaste = new ExecuteWaste();
//                    qualityWaste.setUsId(execute.getOperator());
//                    qualityWaste.setEsId(state.getId());
//                    //未处理。
//                    qualityWaste.setHandle(0);
//                    //质检类型：自检
//                    qualityWaste.setWsType("0");
//                    qualityWaste.setCreateAt(currTime);
//                    qualityWaste.setDelayTime(DateUtil.duration(execute.getStartTime()));
//                    check.executeWaste.save(qualityWaste);
                    int redCount = check.exesetQuality.getQualityCount(expro.getMaId());
                    PopUpEntity pop = new PopUpEntity();
                    OrderInfo info = new OrderInfo();
                    info.setId(execute.getSdId());
                    info.setOrderName(map.get("odName"));
                    info.setOrderNo(map.get("odNo"));
                    pop.setDisapper(expro.getQualityDisapper());
                    pop.setRedCount(redCount);
                    pop.setLimitNum(expro.getLimitNum());
                    pop.setOrderInfo(info);
                    pop.setStartTime(currTime);
                    pop.setEndTime(currTime);
                    //弹窗类型:质检
                    pop.setPopModel(PopConstant.QUALITY_POP);
                    //按照计数质检
                    pop.setQaModel(PopConstant.NUM_QUALITY);
                    pop.setEsId(state.getId());
                    pop.setQualityId(qualityBfwaste.getId());
                    //推送消息
                    sendMsgToMac("maId" + String.valueOf(expro.getMaId()),
                            JSON.toJSONString(pop), ChatType.MAC_QUALITY.getType());
                    //手机推送消息,机台登录的所有人接收消息
                    sendMsgToUser(usIds, JSON.toJSONString(pop),
                            ChatType.MAC_QUALITY.getType());
                    //更新缓存
                    qualityCache.setNumber(boxInfoEntity.getNumberOfDay());
                    check.superviseQualityMapper.updateById(qualityCache);
                    //存储质检记录
                    saveQualityregular(boxInfoEntity, expro, execute, currTime);
                    log.info("质检弹窗结束，按时间弹窗:[uuid:{}]", uuid);
                }
            }
            //按照时间方式弹窗
            if (expro.getModel() != null && expro.getModel() == PopConstant.TIME_QUALITY) {
                if (DateUtil.diffNowTime(qualityCache.getStartTime().getTime(),
                        expro.getQualityTime() * timeunit)) {
                    //推送给前台弹框消息,执行表插入数据
                    ExecuteState state = new ExecuteState();
                    state.setMaId(expro.getMaId());
                    state.setWbId(execute.getWbId());
                    state.setSdId(execute.getSdId());
                    state.setTeamId(execute.getUsIds());
                    state.setUsId(execute.getOperator());
                    state.setStartAt(currTime);
                    state.setCreateAt(currTime);
                    state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
                    state.setEvent(GlobalConstant.ProType.QUALITY_EVENT.getType());
                    UpdateStateUtils.updateSupervise(state, num);
                    //质量巡检表插入一条数据
                    QualityBfwaste qualityBfwaste = new QualityBfwaste();
                    qualityBfwaste.setMaId(expro.getMaId());
                    qualityBfwaste.setCreateAt(new Date());
                    qualityBfwaste.setStartAt(execute.getStartTime());
                    qualityBfwaste.setEndNum(execute.getEndNum());
                    qualityBfwaste.setExId(execute.getExId());
                    qualityBfwaste.setPrId(prId);
                    qualityBfwaste.setUsId(execute.getOperator());
                    qualityBfwaste.setStartNum(execute.getStartNum());
                    qualityBfwaste.setReportStatus(0);
                    check.qualityBfwasteMapper.insert(qualityBfwaste);
//                    ExecuteWaste qualityWaste = new ExecuteWaste();
//                    qualityWaste.setEsId(state.getId());
//                    qualityWaste.setUsId(execute.getOperator());
//                    //是否处理
//                    qualityWaste.setHandle(0);
//                    // 质检类型,自检
//                    qualityWaste.setWsType("0");
//                    qualityWaste.setCreateAt(currTime);
//                    qualityWaste.setDelayTime(DateUtil.duration(execute.getStartTime()));
//                    check.executeWaste.save(qualityWaste);
                    //更改缓存标志位的状态和时间状态
                    int redCount = check.exesetQuality.getQualityCount(expro.getMaId());
                    PopUpEntity pop = new PopUpEntity();
                    OrderInfo info = new OrderInfo();
                    //订单的信息
                    info.setId(execute.getSdId());  //工单id
                    info.setOrderName(map.get("odName"));
                    info.setOrderNo(map.get("odNo"));
                    pop.setDisapper(expro.getQualityDisapper());
                    pop.setRedCount(redCount);
                    pop.setLimitNum(execute.getCurrNum());
                    pop.setOrderInfo(info);
                    pop.setStartTime(qualityCache.getStartTime());
                    pop.setEndTime(currTime);
                    pop.setPopModel(PopConstant.QUALITY_POP);  //弹窗类型：质检
                    pop.setQaModel(PopConstant.TIME_QUALITY);   //时间质检
                    pop.setEsId(state.getId());
                    pop.setQualityId(qualityBfwaste.getId());
                    //推送到机台消息
                    sendMsgToMac("maId" + String.valueOf(expro.getMaId()),
                            JSON.toJSONString(pop), ChatType.MAC_QUALITY.getType());
                    //手机推送消息,机台登录的所有人接收消息
                    sendMsgToUser(usIds, JSON.toJSONString(pop),
                            ChatType.MAC_QUALITY.getType());
                    qualityCache.setStartTime(currTime);
                    check.superviseQualityMapper.updateById(qualityCache);
                    //存储质检记录
                    saveQualityregular(boxInfoEntity, expro, execute, currTime);
                    log.info("质检弹窗结束，按时间弹窗:[uuid:{}]", uuid);
                }
            }
        } else {
            //通过设备id获取当前设备巡检设置信息
            qualityCache = new SuperviseQuality();
            //当前时间放入缓存中
            qualityCache.setStartTime(currTime);
            qualityCache.setMaId(boxInfoEntity.getMaId());
            qualityCache.setUuid(boxInfoEntity.getUuid());
            qualityCache.setCreateAt(currTime);
            qualityCache.setSdId(execute.getSdId());
            qualityCache.setUsIds(execute.getUsIds());
            qualityCache.setStartTime(currTime);
            //把盒子上的数据放入缓存中
            qualityCache.setNumber(boxInfoEntity.getNumberOfDay());
            check.superviseQualityMapper.insert(qualityCache);
            log.info("质检弹窗结束，新增缓存信息成功:[uuid:{}]", uuid);
            return;
        }
    }

    private void saveQualityregular(SuperviseBoxinfo boxInfoEntity, ExecutProcessEntity expro, SuperviseExecute execute, Date currTime) {
        SuperviseQualityregular superviseQualityregular = new SuperviseQualityregular();
        superviseQualityregular.setCreateAt(currTime);
        superviseQualityregular.setIsWaring(1);
        superviseQualityregular.setMaId(boxInfoEntity.getMaId());
        superviseQualityregular.setTargetDay(DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER));
        superviseQualityregular.setTargetHour(currTime.getHours());
        superviseQualityregular.setTargetMin(currTime.getMinutes());
        superviseQualityregular.setStartNum(boxInfoEntity.getNumberOfDay());
        superviseQualityregular.setEndNum(boxInfoEntity.getNumberOfDay());
        superviseQualityregular.setCurrentNum(0);
        //根据类型存储不同规则记录
        if (expro.getModel() != null && expro.getModel() == 1) {
            superviseQualityregular.setRegularModel(2);
            superviseQualityregular.setRegular(expro.getQualityTime());
        } else {
            superviseQualityregular.setRegularModel(1);
            superviseQualityregular.setRegular(expro.getLimitNum());
        }

        superviseQualityregular.setSdId(execute.getSdId());
        superviseQualityregular.setUsIds(execute.getUsIds());
        superviseQualityregular.setStatus(1);
        superviseQualityregular.setNumber(boxInfoEntity.getNumberOfDay());
        check.superviseQualityregularMapper.insert(superviseQualityregular);
    }

    /**
     * 质检弹窗
     *
     * @param uuid
     * @param status
     */
    //todo 金世纪关闭
    public void propQualityUp(String uuid, String status, Integer num) {
        log.info("质检弹窗开始:[uuid:{}, status:{}]", uuid, status);

        SuperviseExecute execute = check.executService.getExecutByBno(uuid);
        //判断是否已接单。如果没有接单就不会有质检弹窗。
        if (execute == null || execute.getSdId() == null) {
            log.info("质检弹窗结束，未处于接单状态:[uuid:{}]", uuid);
            return;
        }
        //判定该设备执行订单状态不是正在执行的状态
        ExecutProcessEntity expro = ExecutConstant.machineProc.get(uuid);
        if (expro == null) { //表示设备不是正式执行状态中
            log.info("质检弹窗结束,设备未处于正式执行状态:[uuid:{}]", uuid);
            return;
        }
        Date currTime = new Date();
        //质量巡检缓存
        ExeQualityCacheEntity qualityCache = ExecutConstant.qualityCache.get(uuid);

        //获取订单名称
        Map<String, String> map = check.executService.getOrderName(execute.getSdId());
        String usIds = execute.getUsIds();
        //如果当前订单  是结束状态，就需要清除缓存
        if (GlobalConstant.ProType.AFTERPRO_STATUS.getType()
                .equals(execute.getExeStatus())) {
            ExecutConstant.qualityCache.remove(uuid);
            log.info("当前订单为结束状态，清除质检缓存:[uuid:{}, sdId:{}]", uuid, execute.getSdId());
        } else {
            //判断是不是有缓存
            if (qualityCache != null) {
                //巡检方式
                if (qualityCache.getModel() != null && qualityCache.getModel() == PopConstant.NUM_QUALITY) {
                    if (num - qualityCache.getNumber() >= expro.getLimitNum()) {
                        //发送弹出窗口消息1.把巡检的内容发给前台quality
                        //执行表状态添加一条数据-
                        ExecuteState state = new ExecuteState();
                        state.setMaId(expro.getMaId());
                        state.setWbId(execute.getWbId());
                        state.setSdId(execute.getSdId());
                        state.setTeamId(execute.getUsIds());
                        state.setUsId(execute.getOperator());
                        state.setStartAt(currTime);
                        state.setCreateAt(currTime);
                        state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
                        state.setEvent(GlobalConstant.ProType.QUALITY_EVENT.getType()); //质检状态
                        UpdateStateUtils.updateSupervise(state, num);
                        //质量巡检表插入一条数据
                        ExecuteWaste qualityWaste = new ExecuteWaste();
                        qualityWaste.setUsId(execute.getOperator());
                        qualityWaste.setEsId(state.getId());
                        qualityWaste.setHandle(0);      //未处理。
                        qualityWaste.setWsType("0");    //质检类型：自检
                        qualityWaste.setCreateAt(currTime);
                        qualityWaste.setDelayTime(DateUtil.duration(execute.getStartTime()));
                        check.executeWaste.save(qualityWaste);
                        int redCount = check.exesetQuality.getQualityCount(expro.getMaId());
                        PopUpEntity pop = new PopUpEntity();
                        OrderInfo info = new OrderInfo();
                        info.setId(execute.getSdId());
                        info.setOrderName(map.get("odName"));
                        info.setOrderNo(map.get("odNo"));
                        pop.setDisapper(expro.getQualityDisapper());
                        pop.setRedCount(redCount);
                        pop.setLimitNum(expro.getLimitNum());
                        pop.setOrderInfo(info);
                        pop.setStartTime(qualityCache.getStartTime());
                        pop.setEndTime(currTime);
                        pop.setPopModel(PopConstant.QUALITY_POP);  //弹窗类型:质检
                        pop.setQaModel(PopConstant.NUM_QUALITY);   //按照计数质检
                        pop.setEsId(state.getId());
                        pop.setQualityId(qualityWaste.getId());
                        //推送消息
                        sendMsgToMac("maId" + String.valueOf(expro.getMaId()),
                                JSON.toJSONString(pop), ChatType.MAC_QUALITY.getType());
                        //手机推送消息,机台登录的所有人接收消息
                        sendMsgToUser(usIds, JSON.toJSONString(pop),
                                ChatType.MAC_QUALITY.getType());
                        //更新缓存信息，更改标志位的状态和更新计数功能
                        qualityCache.setNumber(num);   //把当前盒子计数放入缓存中
                        qualityCache.setEsId(state.getId());
                        ExecutConstant.qualityCache.put(uuid, qualityCache);
                    }
                } else if (qualityCache.getModel() != null && qualityCache.getStartTime() != null && qualityCache.getModel() == PopConstant.TIME_QUALITY) { //按照时间方式弹窗
                    if (DateUtil.diffNowTime(qualityCache.getStartTime().getTime(),
                            expro.getQualityTime() * timeunit)) {
                        //推送给前台弹框消息（设备id，弹出消失时间,exesetFault,前台请求停机列表）
                        //执行表插入数据
                        ExecuteState state = new ExecuteState();
                        state.setMaId(expro.getMaId());
                        state.setWbId(execute.getWbId());
                        state.setSdId(execute.getSdId());
                        state.setTeamId(execute.getUsIds());
                        state.setUsId(execute.getOperator());
                        state.setStartAt(currTime);
                        state.setCreateAt(currTime);
                        state.setStatus(GlobalConstant.ProType.INPRO_STATUS.getType());
                        state.setEvent(GlobalConstant.ProType.QUALITY_EVENT.getType());
                        UpdateStateUtils.updateSupervise(state, num);
                        //质量巡检表插入一条数据
                        ExecuteWaste qualityWaste = new ExecuteWaste();
                        qualityWaste.setEsId(state.getId());
                        qualityWaste.setUsId(execute.getOperator());
                        qualityWaste.setHandle(0);    //是否处理
                        qualityWaste.setWsType("0");   // 质检类型。自检
                        qualityWaste.setCreateAt(currTime);
                        qualityWaste.setDelayTime(DateUtil.duration(execute.getStartTime()));
                        check.executeWaste.save(qualityWaste);
                        //更改缓存标志位的状态和时间状态
                        int redCount = check.exesetQuality.getQualityCount(expro.getMaId());
                        PopUpEntity pop = new PopUpEntity();
                        OrderInfo info = new OrderInfo();
                        //订单的信息
                        info.setId(execute.getSdId());  //工单id
                        info.setOrderName(map.get("odName"));
                        info.setOrderNo(map.get("odNo"));
                        pop.setDisapper(expro.getQualityDisapper());
                        pop.setRedCount(redCount);
                        pop.setLimitNum(execute.getCurrNum());
                        pop.setOrderInfo(info);
                        pop.setStartTime(qualityCache.getStartTime());
                        pop.setEndTime(currTime);
                        pop.setPopModel(PopConstant.QUALITY_POP);  //弹窗类型：质检
                        pop.setQaModel(PopConstant.TIME_QUALITY);   //时间质检
                        pop.setEsId(state.getId());
                        pop.setQualityId(qualityWaste.getId());
                        //推送到机台消息
                        sendMsgToMac("maId" + String.valueOf(expro.getMaId()),
                                JSON.toJSONString(pop), ChatType.MAC_QUALITY.getType());
                        //手机推送消息,机台登录的所有人接收消息
                        sendMsgToUser(usIds, JSON.toJSONString(pop),
                                ChatType.MAC_QUALITY.getType());
                        qualityCache.setStartTime(currTime);
                        qualityCache.setEsId(state.getId());
                        ExecutConstant.qualityCache.put(uuid, qualityCache);
                        log.info("质检弹窗结束，按时间弹窗:[uuid:{}]", uuid);

                    }
                }
            } else {
                //通过设备id获取当前设备巡检设置信息
                qualityCache = new ExeQualityCacheEntity();
                qualityCache.setStartTime(currTime);   //当前时间放入缓存中
                qualityCache.setNumber(num);  //把盒子上的数据放入缓存中
                qualityCache.setModel(expro.getModel());
                ExecutConstant.qualityCache.put(uuid, qualityCache);
                log.info("质检弹窗结束，新增缓存信息成功:[uuid:{}]", uuid);
                return;
            }
        }
        log.info("质检弹窗结束，不符合:[uuid:{}]", uuid);
    }


    /**
     * 检查盒子状态
     *
     * @param uuid
     * @param status
     */
    public  void checkStatusChange(SuperviseExecute execute, SuperviseBoxinfo superviseBoxinfo, String uuid, String status) {
        log.info("开始检测设备状态:[uuid:{}, status:{}]", uuid, status);

        if (execute == null) {
            log.info("获取设备实时信息失败:[uuid:{}]", uuid);
            return;
        }
//        //是否为接单中停机
//        if (status.equals(MachineConstant.MA_STOP)) {
//            if (superviseBoxinfo.getBlnAccept() != null && 1 == superviseBoxinfo.getBlnAccept()) {
//                status = "5";
//            }
//        }

        Date currTime = Date.from(Instant.now());
        //设备状态检测缓存
        ExeStatusChangeCacheEntity exeStatusChangeCacheEntity = ExecutConstant.maStatusCache.get(uuid);
        int startNum = superviseBoxinfo.getNumberOfDay() == null ? 0 : superviseBoxinfo.getNumberOfDay();
        if (exeStatusChangeCacheEntity != null) {
            if (exeStatusChangeCacheEntity.getStatus() != null && status.equals(exeStatusChangeCacheEntity.getStatus())) {
                log.info("设备状态检测完成，状态未发生变化:[uuid:{}, status:{}]", uuid, status);
                return;
            }
            //先寻找上一条记录
            SuperviseInterval oldLog = check.superviseIntervalMapper.getPreviousLog(superviseBoxinfo.getMaId());
            if (oldLog != null && oldLog.getStartTime() != null) {
                oldLog.setEndTime(currTime);
                int diffTime = DateUtil.calLastedTime(currTime.getTime(), oldLog.getStartTime());
                oldLog.setDiffTime(diffTime);
                oldLog.setEndNum(startNum);
                //小于上一条
                if (startNum < oldLog.getStartNum()) {
                    startNum = startNum + superviseBoxinfo.getClearNum();
                }
                //加上清零还是小于
                if (startNum < oldLog.getStartNum()) {
                    startNum = startNum + superviseBoxinfo.getNumber();
                }
                int pcount = startNum - oldLog.getStartNum();
                if (pcount < 0) {
                    if (superviseBoxinfo.getNumberOfDay() > 0 && superviseBoxinfo.getNumberOfDay() != 0) {
                        pcount = superviseBoxinfo.getNumberOfDay();
                    } else {
                        pcount = superviseBoxinfo.getNumber();
                    }
                }
                oldLog.setPcout(pcount);
                if (diffTime > 0) {
                    Double dspeed = (diffTime > 0) ? pcount / Double.valueOf(diffTime) : 0d;
                    dspeed = dspeed * 60;
                    dspeed = BigDecimal.valueOf(dspeed).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    oldLog.setCurrSpeed(dspeed);
                } else {
                    oldLog.setCurrSpeed(Double.valueOf(0));
                }
                oldLog.setUpdateAt(currTime);
                check.superviseIntervalMapper.updateById(oldLog);
            }
            //状态发生变化保存
            saveSuperviseInterval(superviseBoxinfo, status, execute, currTime, superviseBoxinfo.getNumberOfDay());
            //更新缓存
            exeStatusChangeCacheEntity.setStatus(status);
            ExecutConstant.maStatusCache.put(uuid, exeStatusChangeCacheEntity);
            log.info("检测设备状态完成，状态发生变化:[uuid:{}, status:{}]", uuid, status);
        } else {
            //如没有缓存，放入
            exeStatusChangeCacheEntity = new ExeStatusChangeCacheEntity();
            exeStatusChangeCacheEntity.setStatus(status);
            ExecutConstant.maStatusCache.put(uuid, exeStatusChangeCacheEntity);
            saveSuperviseInterval(superviseBoxinfo, status, execute, currTime, startNum);
            log.info("检测设备状态完成，新增缓存信息:[uuid:{}, status:{}]", uuid, status);
        }
    }

    /**
     * 存储状态定期检测表信息
     *
     * @param status   盒子状态
     * @param execute  设备当前信息
     * @param currTime 当前时间
     * @param startNum 开始盒子计数
     */
    private void saveSuperviseInterval(SuperviseBoxinfo superviseBoxinfo, String status, SuperviseExecute execute, Date currTime, int startNum) {
        SuperviseInterval superviseInterval = new SuperviseInterval();
        superviseInterval.setMaId(execute.getMaId());
        superviseInterval.setUuid(execute.getUuid());
        superviseInterval.setCreateAt(currTime);
        superviseInterval.setStartNum(startNum);
        superviseInterval.setStartTime(currTime);
        superviseInterval.setStatus(Integer.valueOf(status));
        superviseInterval.setBlnAccept(superviseBoxinfo.getBlnAccept());
        superviseInterval.setWbNo(superviseBoxinfo.getWbNo());

        check.superviseIntervalMapper.insert(superviseInterval);
    }

    /**
     * 调用发送消息
     */
    private void sendMsgToUser(String usIds, String content, String type) {
        if (usIds != null) {
            String[] split = usIds.split("\\|");
            if (split == null || split.length < 2) {
                return;
            }
            //手机推送消息给所有用户
            for (int i = 1, length = split.length; i < length; i++) {
                try {
                    check.imMessageClient.sendMsgToUser(split[i], content, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 调用发送消息
     */
    private void sendMsgToMac(String maId, String content, String type) {
        //手机推送质检
        try {
            check.imMessageClient.sendMsgToMac(maId, content, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
