package com.anaysis.executSupervise;

import com.anaysis.Influx.InfluxDBRepo;
import com.anaysis.common.*;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.entity.MachineEntity;
import com.anaysis.executSupervise.entity.*;
import com.anaysis.executSupervise.mapper.MachineMainfoMapper;
import com.anaysis.executSupervise.mapper.SuperviseRunregularMapper;
import com.anaysis.executSupervise.mapper.SuperviseShiftcountMapper;
import com.anaysis.executSupervise.mapper.WorkbatchShiftMapper;
import com.anaysis.executSupervise.service.*;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.anaysis.executSupervise.vo.WorkbatchMachShiftVO;
import com.anaysis.service.MachineService;
import com.anaysis.statis.entity.StatisOrdreach;
import com.anaysis.statis.mapper.StatisOrdreachMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


/*****
 * 不同数据提供后数据统计分析结果内容再进行处理。
 */
@Slf4j
@Component
public class MachineSupervise {

    /**
     * 保存日志到数据库的线程池
     */
    ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("SLogAspect-Thread-%d").build();

    /**
     * 构造线程池
     */
    ExecutorService executor = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024),
            threadFactory,
            new ThreadPoolExecutor.AbortPolicy());
    //盒子的基本信息内容
    public SuperviseBoxinfoService boxInfoService = (SuperviseBoxinfoService) SpringUtil.getBean(SuperviseBoxinfoService.class);

    private MachineService machineService = (MachineService) SpringUtil.getBean(MachineService.class);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //public Map<String, SuperviseBoxinfo> hasBoxinfo = new HashMap<String, SuperviseBoxinfo>();//盒子的缓存数据信息
    public static Map<String, Date> isHourly = new HashMap();//key表示uuid，value表示是否有或者没有
    public static Map<String, Integer> isHourlyLastAlg = new HashMap();
    //处理状态间隔数据
    SuperviseIntervalService intervalService = (SuperviseIntervalService) SpringUtil.getBean(SuperviseIntervalService.class);
    SuperviseExerunService exerunService = (SuperviseExerunService) SpringUtil.getBean(SuperviseExerunService.class);//执行
    SuperviseBoxinfoService boxinfoService = (SuperviseBoxinfoService) SpringUtil.getBean(SuperviseBoxinfoService.class);//盒子实时信息
    SuperviseRunregularService runregularService = (SuperviseRunregularService) SpringUtil.getBean(SuperviseRunregularService.class);//执行
    SuperviseRunregularMapper superviseRunregularMapper = (SuperviseRunregularMapper) SpringUtil.getBean(SuperviseRunregularMapper.class);
    ExecuteStateService executeStateService = (ExecuteStateService) SpringUtil.getBean(ExecuteStateService.class);
    SuperviseExecuteService superviseExecuteService = (SuperviseExecuteService) SpringUtil.getBean(SuperviseExecuteService.class);
    AnalyTenantService analyTenantService = (AnalyTenantService) SpringUtil.getBean(AnalyTenantService.class);
    ExecutCheck check = (ExecutCheck) SpringUtil.getBean(ExecutCheck.class);
    StatisOrdreachMapper statisOrdreachMapper = (StatisOrdreachMapper) SpringUtil.getBean(StatisOrdreachMapper.class);
    SuperviseShiftcountMapper superviseShiftcountMapper = (SuperviseShiftcountMapper) SpringUtil.getBean(SuperviseShiftcountMapper.class);
    MachineMainfoMapper machineMapper = (MachineMainfoMapper) SpringUtil.getBean(MachineMainfoMapper.class);
    WorkbatchShiftMapper workbatchShiftMapper = (WorkbatchShiftMapper) SpringUtil.getBean(WorkbatchShiftMapper.class);
    InfluxDBRepo influxDBRepo = (InfluxDBRepo) SpringUtil.getBean(InfluxDBRepo.class);
    SuperviseShiftcountMapper intervalMapper = (SuperviseShiftcountMapper) SpringUtil.getBean(SuperviseShiftcountMapper.class);

    private Boolean release(String tenantId) {
        List list = new ArrayList();
        list.add("xingyi");
        list.add("000000");
        list.add("demo");
        list.add("nxhr");
        list.add("yilong");
        return list.contains(tenantId);
    }

    /****
     * 保存设备上传状态信息，保存到mysql数据库中为零时数据
     * @param tenantId
     * @param boxInfoEntity
     * @param uuid_s
     * @param status
     * @param number
     * @param numberofday
     */
    private SuperviseBoxinfo saveBoxInfo(SuperviseExecute execute, String tenantId, SuperviseBoxinfo boxInfoEntity, String uuid_s, String status, int number, int numberofday, int number_xlh, String sip, Long ctime, Date sendTime, Integer energyNum, Integer speed) {

        Double dspeed = 0d;
        int count = (boxInfoEntity.getNumberOfDay() != null) ? boxInfoEntity.getNumberOfDay() : 0;
        int second = 0;
        if (boxInfoEntity.getSendTime() != null) {
            second = DateUtil.calLastedTime(sendTime.getTime(), boxInfoEntity.getSendTime());
        } else {
            second = DateUtil.calLastedTime(ctime, boxInfoEntity.getUpdateAt()
            );
        }
        Double sdb = (second > 0) ? Double.valueOf(second) : 0d;
        int pcount = numberofday - count;
        if (pcount < 0) {
            pcount = numberofday;
        }
        dspeed = (second > 0) ? pcount * 3600 / sdb : 0d;//小時速度
        BigDecimal bigDecimal = new BigDecimal(dspeed).setScale(2, BigDecimal.ROUND_HALF_UP);
        dspeed = bigDecimal.doubleValue();
        //是否传了速度过来
        if (speed == null) {
            //判断上一个状态为离线的时候，速度就为0
            if ("4".equalsIgnoreCase(boxInfoEntity.getStatus())) {
                boxInfoEntity.setDspeed(0d);//設定當前速度
            } else {
                boxInfoEntity.setDspeed(Double.valueOf(dspeed.intValue()));
            }
        } else {
            boxInfoEntity.setDspeed(speed.doubleValue());
        }
//        //是否为接单中停机
//        if (status.equals(MachineConstant.MA_STOP)) {
//            if (boxInfoEntity.getBlnAccept() != null && boxInfoEntity.getBlnAccept() == 1) {
//                status = "5";
//            }
//        }
        //状态变化更新内容
        boxInfoEntity.setUuid(uuid_s); //uuid
        boxInfoEntity.setStatus(status); //状态
        boxInfoEntity.setNumber(number); //盒子计数
        boxInfoEntity.setNumberOfDay(numberofday); //今日计数
        boxInfoEntity.setXlh(number_xlh); //序列号
        boxInfoEntity.setUpdateAt(new Date(ctime));
        boxInfoEntity.setSip(sip); //最后返回的sip地址信息
        boxInfoEntity.setSendTime(sendTime);
        boxInfoEntity.setMaId(execute.getMaId());
        boxInfoService.saveOrUpdate(boxInfoEntity);
        //如果没有插入状态变化信息，就判断半点和小时进行数据插入
        // String statu = status;
        log.info("开始存储盒子基数数据:[maId:{},sendTime:{}, number:{},numberOfDay:{},index:{}]", boxInfoEntity.getMaId(), sendTime, number, numberofday, boxInfoEntity.getXlh());
        setHalfPointData(boxInfoEntity, status, numberofday, ctime, number, 0, sendTime);
        save_nosql(tenantId, status, uuid_s, numberofday, ctime, number, sendTime, 0);
        log.info("存储盒子基数成功:[maId:{}]", boxInfoEntity.getMaId());


        return boxInfoEntity;
    }

    /****
     *
     * @param uuid_s
     * @param num_xlh 序号
     * @param num 计数器数字
     * @param status
     * @return
     */
    public boolean extOperate(String uuid_s, Integer num_xlh, Integer num, String status, String sip, Long ctime, Date sendTime, Integer energyNum) throws ParseException {
        //获取数据源并取得租户id
        String tenantId = analyTenantService.getTenantId(uuid_s);
        //只要nxhr和xingyi和demo数据
        if (!release(tenantId)) {
            log.info("暂不采集该租户:[uuid:{}, tenantId:{}]", uuid_s, tenantId);
            return false;
        }
        //数据库查询盒子状态信息--获取上一次历史信息【第一次查询当前状态】
        SuperviseBoxinfo boxInfoEntity = boxInfoService.getBoxInfoByBno(uuid_s);//查询矩阵盒子实体数据
        //数据还没有保存机器的信息，根据唯一ID进行数据查询
        if (boxInfoEntity == null) {
            log.info("不能匹配到 uuid：" + uuid_s);
            return false;
        } else {
            Integer number_xlh = (boxInfoEntity.getXlh() != null) ? boxInfoEntity.getXlh() : 0;
            Integer number = (boxInfoEntity.getNumber() != null) ? boxInfoEntity.getNumber() : 0;
            Integer numberofday = (boxInfoEntity.getNumberOfDay() != null) ? boxInfoEntity.getNumberOfDay() : 0;
            //设备序号的判定，不能小于上一个阶段数量
            if (num_xlh >= number_xlh) {
                Integer boxNumber = boxInfoEntity.getNumberOfDay() == null ? 0 : boxInfoEntity.getNumberOfDay();
                //表示正常操作数据，顺序生产
                numberofday = num - number + boxNumber;
                if (numberofday < 0 && boxInfoEntity.getClearNum() != null) {
                    numberofday = numberofday + boxInfoEntity.getClearNum();
                }
            } else if (num_xlh < 10) {
                //表示设备清零操作
                log.info("设备清零操作，进行计数累加:[uuid:{}, xlh_s:{}, numberofday:{}]", uuid_s, num_xlh, numberofday);
                //当天的累计数
                numberofday = num + numberofday;
            }
            SuperviseExecute execute = superviseExecuteService.getExecutByBno(uuid_s);
            SuperviseBoxinfo superviseBoxinfo = saveBoxInfo(execute, tenantId, boxInfoEntity, uuid_s, status, num, numberofday, num_xlh, sip, ctime, sendTime, energyNum, null);

//            //停机检测
//            check.checkDown(execute, uuid_s, status, num);
//            //质量检测
//            check.checkQuality(execute, uuid_s, boxInfoEntity, status, num);
            UpdateStateUtils.updateSuperviseExecute(superviseBoxinfo,uuid_s, status, num);
            // check.propQualityUp(uuid_s, status, num);
            //更新计数
            //检查盒子状态是否变更
            check.checkStatusChange(execute, boxInfoEntity, uuid_s, status);
            //检查C1信息
            //checkC1ByUuId(boxInfoEntity, uuid_s, status, ctime);
            executor.execute(new Thread(() -> {
                try {
                    updateOrdreach(boxInfoEntity, tenantId);
                } catch (ParseException e) {
                    log.error("更新小时达成率出现异常", e.getMessage());
                }
            }));
        }
        return true;
    }

    public boolean yiLongExtOperate(String uuid_s, Integer num_xlh, Integer num, String status, Long ctime, Date sendTime, Integer speed) throws ParseException {
        SuperviseBoxinfo boxInfoEntity = boxInfoService.getBoxInfoByBno(uuid_s);//查询矩阵盒子实体数据
        //数据还没有保存机器的信息，根据唯一ID进行数据查询
        if (boxInfoEntity == null) {
            log.info("不能匹配到 uuid：" + uuid_s);
            return false;
        } else {
            SuperviseExecute execute = superviseExecuteService.getExecutByBno(uuid_s);
            SuperviseBoxinfo superviseBoxinfo = saveBoxInfo(execute, "yilong", boxInfoEntity, uuid_s, status, num, num, num_xlh, "sip", ctime, sendTime, 0, speed);
            //更新计数
            UpdateStateUtils.updateSuperviseExecute(superviseBoxinfo,uuid_s, status, num);
        }
        return true;
    }

    /*****
     * 实时更新设备对应的计划达成率数据信息
     * @param boxInfoEntity
     * @param tenantId
     */
    void updateOrdreach(SuperviseBoxinfo boxInfoEntity, String tenantId) throws ParseException {
        if (!tenantId.equals("xingyi") && !tenantId.equals("nxhr")) {
            log.info("修改小时达成率失败不是兴艺与何瑞的:tenantId：{}", tenantId);
            return;
        }
        DBIdentifier.setProjectCode(tenantId);
        Date date = new Date();
        String targetDay = DateTimeUtil.now(DateTimeUtil.DEFAULT_DATE_FORMATTER);
        date.setMinutes(0);
        date.setSeconds(0);
        Date endDate = new Date(date.getTime() + 1000 * 60 * 60);
        log.info("修改小时达成率:[day:{}, hour:{}, maId:{}]", targetDay, date.getHours(), boxInfoEntity.getMaId());

        StatisOrdreach statisOrdreach = getWorkbatchShiftset(boxInfoEntity, date, targetDay);

        //新增后可以直接采用对象进行数据更新操作
        if (statisOrdreach != null) {
            //获取第一条和最后一条
            // SuperviseShiftcount first = superviseShiftcountMapper.getInTimeFirst(date, endDate, boxInfoEntity.getMaId());
            //SuperviseShiftcount last = superviseShiftcountMapper.getInTimeLast(date, endDate, boxInfoEntity.getMaId());
            Integer count = superviseShiftcountMapper.getPcountByMaIdAndStartTimeAndEndTime(boxInfoEntity.getMaId(), date, endDate);
            log.info("小时达成率:[first:{}, last:{}, count:{}]", date.getHours(), endDate.getHours(), count);
//            if (last.getStartNum() < first.getStartNum()) {
//                //获取比最后一条大的第一条
//                SuperviseShiftcount superviseShiftcount = superviseShiftcountMapper.getInTimeLastSecond(date, endDate, boxInfoEntity.getMaId(), last.getStartNum());
//                if (superviseShiftcount != null) {
//                    count = superviseShiftcount.getStartNum() - first.getStartNum() + last.getStartNum();
//                }
//            }
            statisOrdreach.setRealCount(count);
            Integer planCount = statisOrdreach.getPlanCount();
            if (planCount == null || planCount < 0) {
                statisOrdreach.setReachRate(BigDecimal.ZERO);
                statisOrdreach.setPlanCount(0);
                statisOrdreachMapper.updateById(statisOrdreach);
            } else {
                if (statisOrdreach.getPlanCount() != 0) {
                    statisOrdreach.setReachRate(BigDecimal.valueOf(count / Double.valueOf(statisOrdreach.getPlanCount())).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
                statisOrdreachMapper.updateById(statisOrdreach);
            }
            log.info("修改小时达成率完成:[maId:{},first:{}, last:{}, count:{}]", boxInfoEntity.getMaId(),
                    date.getHours(), endDate.getHours(), count);

        } else {
            log.info("未找到小时达成率:[maid:{},hour:{}]", boxInfoEntity.getMaId(), date.getHours()
            );
        }
    }

    /***
     * 获取班次设置表信息
     * @param boxInfoEntity
     * @param date
     * @param targetDay
     * @return
     * @throws ParseException
     */
    private StatisOrdreach getWorkbatchShiftset(SuperviseBoxinfo boxInfoEntity, Date date, String targetDay) throws ParseException {
        MachineMainfo machineMainfo = machineMapper.selectById(boxInfoEntity.getMaId());
        Integer wsId = null;
        String wsName = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatTime = sdf.format(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<WorkbatchMachShiftVO> workbatchMachShiftVOS = workbatchShiftMapper.findByMaId(boxInfoEntity.getMaId());
        for (WorkbatchMachShiftVO workbatchMachShiftVO : workbatchMachShiftVOS) {
            String staTime = simpleDateFormat.format(workbatchMachShiftVO.getStartTime());
            Date startTime = simpleDate.parse(formatTime + (" ") + staTime);
            String eTime = simpleDateFormat.format(workbatchMachShiftVO.getEndTime());
            Date endTime = simpleDate.parse(formatTime + (" ") + eTime);
            //跨天
            if (endTime.getTime() < startTime.getTime()) {
                endTime = DateTimeUtil.getNextDay(endTime, 1);
            }
            //当前处于跨天了
            if (date.getHours() < endTime.getHours() && date.getHours() < startTime.getHours()) {
                startTime = DateTimeUtil.getFrontDay(startTime, 1);
                endTime = simpleDate.parse(formatTime + (" ") + eTime);
                targetDay = sdf.format(startTime);
            }
            if (startTime.getTime() <= date.getTime() && date.getTime() <= endTime.getTime()) {
                wsId = workbatchMachShiftVO.getId();
                wsName = workbatchMachShiftVO.getCkName();
            }
        }
        StatisOrdreach statisOrdreach = statisOrdreachMapper.getByMaIdAndSdIdAndTargetDayAndTargetHour(boxInfoEntity.getMaId(), targetDay, date.getHours());
        //如果没有查询到，就新增一条记录
        if (statisOrdreach == null) {
            statisOrdreach = new StatisOrdreach();
            statisOrdreach.setMaId(boxInfoEntity.getMaId());
            statisOrdreach.setTargetDay(targetDay);
            statisOrdreach.setTargetHour(date.getHours());
            statisOrdreach.setMaName(machineMainfo.getName());
            statisOrdreach.setWsId(wsId);
            statisOrdreach.setWsName(wsName);
            statisOrdreach.setCreateAt(new Date());
            //设置班次相关信息
            statisOrdreachMapper.insert(statisOrdreach);
        }
        return statisOrdreach;
    }


    /****
     * 校验设备uuid是否C1状态
     * @param boxinfo
     * @param uuid
     * @param status
     * @param ctime
     */
    public void checkC1ByUuId(SuperviseBoxinfo boxinfo, String uuid, String status, Long ctime) {
        SuperviseExerun exerun = exerunService.getByuuid(boxinfo.getUuid());
        if (exerun == null) {
            log.info("检查c1失败,缺少b1缓存信息:[uuid:{}, maId:{}]", uuid, boxinfo.getMaId());
            return;
        }
        SuperviseBoxinfoVo boxinfoVo = boxinfoService.getBoxVoByuuid(uuid);
        Date currtime = new Date(ctime);
        //是否为运行状态
        if (MachineConstant.MA_RUN.equalsIgnoreCase(status)) {
            log.info("设备运行中，更新c1缓存信息");
            int intervalTime = DateUtil.calLastedTime(currtime.getTime(), exerun.getStartTime());
            exerun.setStayTime(intervalTime);
            //转换为秒
            int regularTime = exerun.getRegular() * 60;
            int overTime = intervalTime - regularTime;
            if (overTime > 0) {
                log.info("持续运行超过规则时间，存储日志与执行表c1信息:[uuid:{}]", uuid);
                //超过多少时间
                exerun.setOverTime(overTime / 60);
                //开始记录c1信息
                exerun.setStatus(1);
                //存储执行表c1
                ExecuteState state = new ExecuteState();
                state.setMaId(boxinfoVo.getMaId());
                state.setSdId(boxinfoVo.getSdId());
                ExecuteState b1Execut = executeStateService.getB1Execut(state);
                if (b1Execut == null) {
                    log.info("存储执行表c1异常，b1不存在:[uuid:{}]", uuid);
                    return;
                }
                //C1开始时间
                Integer regular = exerun.getRegular() - 2;
                if (regular < 0) {
                    //默认为2分钟差值
                    regular = 2;
                }
                long time = regular * 60 * 1000;
                Date c1StartTime = new Date(currtime.getTime() - time);
                if (c1StartTime.getTime() <= b1Execut.getStartAt().getTime()) {
                    //设置为B1 2分钟后
                    c1StartTime = new Date(b1Execut.getStartAt().getTime() + 2 * 6000);
                }
                //更新b1结束时间
                b1Execut.setEndAt(c1StartTime);
                b1Execut.setDuration(DateUtil.calLastedTime(currtime.getTime(), b1Execut.getStartAt()));
                executeStateService.updateState(b1Execut);
                //插入C1的操作
                ExecuteState cState = b1Execut;
                cState.setStatus("C");
                cState.setEvent("C1");
                cState.setStartAt(c1StartTime);
                cState.setEndAt(null);
                cState.setDuration(0);
                cState.setUsId(b1Execut.getUsId());
                cState.setCreateAt(currtime);
                executeStateService.saveState(cState);
                //更新实时状态表信息为c1
                if (updateSuperviseExecute(boxinfo, currtime, cState)) {
                    return;
                }
                //存储日志
                saveRunregular(exerun, boxinfo, boxinfoVo, currtime, overTime);
                log.info("持续运行超过规则时间，存储日志与执行表c1信息成功:[uuid:{}]", uuid);
                //删除缓存信息
                exerunService.clearByuuid(uuid);
                return;
            }
            //未达到规则时间更新数据
            exerun.setUpdateAt(currtime);
            exerunService.updateByuuid(exerun);
        } else {
            log.info("设备处于非正常运行状态，重新计算c1:[uuid:{}, status:{}]", uuid, status);
            //重新开始计算间距时间
            if (exerun != null) {
                exerun.setStayTime(0);
                exerun.setUpdateAt(currtime);
                exerun.setStartTime(currtime);
                exerunService.updateByuuid(exerun);
            }
        }
    }

    /****
     * 更新订单实时状态信息表
     * @param boxinfo
     * @param currtime
     * @param cState
     * @return
     */
    private boolean updateSuperviseExecute(SuperviseBoxinfo boxinfo, Date currtime, ExecuteState cState) {
        SuperviseExecute oldExecute = superviseExecuteService.getExecutByBno(boxinfo.getUuid());
        if (oldExecute == null) {
            log.warn("更新实时状态异常,实时状态信息不存在");
            return true;
        }
        Integer numberOfDay = boxinfo.getNumberOfDay();
        SuperviseExecute superviseExecute = new SuperviseExecute();
        superviseExecute.setUuid(boxinfo.getUuid());
        superviseExecute.setStartTime(oldExecute.getStartTime());
        int startNum = (oldExecute.getStartNum() != null) ? oldExecute.getStartNum() : 0;
        Integer currNum = numberOfDay - startNum;
        //说明计数重置了
        if (currNum < 0) {
            currNum = currNum + boxinfo.getClearNum();
            numberOfDay = numberOfDay + boxinfo.getClearNum();
        }
        superviseExecute.setEndNum(numberOfDay);
        superviseExecute.setCurrNum(currNum);
        superviseExecute.setEsId(cState.getId());
        superviseExecute.setEvent(cState.getEvent());
        superviseExecute.setExeStatus(cState.getStatus());
        superviseExecute.setStartTime(currtime);
        superviseExecuteService.update(superviseExecute);
        return false;
    }

    /***
     *
     * @param exerun
     * @param boxinfo
     * @param boxinfoVo
     * @param currtime
     * @param overTime
     */
    private void saveRunregular(SuperviseExerun exerun, SuperviseBoxinfo boxinfo, SuperviseBoxinfoVo boxinfoVo, Date currtime, int overTime) {
        SuperviseRunregular runregular = new SuperviseRunregular();
        runregular = new SuperviseRunregular();
        runregular.setUuid(boxinfo.getUuid());
        runregular.setMaId(boxinfo.getMaId());
        runregular.setSdId(boxinfoVo.getSdId());
        runregular.setNumber(boxinfo.getNumberOfDay());
        runregular.setStartTime(currtime);
        runregular.setRegular(exerun.getRegular());
        runregular.setUsIds(boxinfoVo.getUsIds());
        runregular.setStatus(1);
        runregular.setIsWaring(0);
        int staytime = DateUtil.calLastedTime(currtime.getTime(), runregular.getStartTime());
        runregular.setStayTime(staytime);
        runregular.setOverTime(overTime / 60);
        runregular.setEndTime(currtime);
        runregular.setTargetMin(currtime.getMinutes());
        runregular.setCreateAt(currtime);
        superviseRunregularMapper.insert(runregular);
    }

    /****
     * 保存到mongodb数据库，根据数据状态变化后进行保存
     * @param
     * @param tenantId
     * @param status
     * @param uuid
     * @param ctime //设定当前时间
     */
    public void save_nosql(String tenantId, String status, String uuid, Integer numOfDay, long ctime, Integer number, Date sendTime, Integer pcount) {
        //  influx 时区需要+8h
        ctime = sendTime.getTime() + 60 * 8 * 60 * 1000;
        Date ntime = new Date(ctime);
        String stop = "0";
        String run = "0";
        String error = "0";
        String no_status = status;
        if ("1".equals(no_status)) {
            run = "1";//运动状态
        } else if ("2".equals(no_status)) {
            stop = "1";//停止状态
        } else if ("3".equals(no_status)) {
            error = "1";//故障状态
        } else if ("4".equals(no_status)) {
            //表示离线状态
            run = "-1";//运动状态
            stop = "-1";//停止状态
            error = "-1";//故障状态
        }
        MachineEntity machineEntity = new MachineEntity();
        machineEntity.setB_id(uuid);
        machineEntity.setRun(run);
        machineEntity.setStop(stop);
        machineEntity.setError(error);
        machineEntity.setCount(String.valueOf(numOfDay));
        machineEntity.setTime(formatter.format(ntime));
        machineEntity.setNumber(number.toString());
        machineEntity.setPcount(pcount);
        log.info("上传的mongbd信息---uid：" + uuid + ", count :" + numOfDay);
        influxDBRepo.post(machineEntity, ctime, tenantId);
    }


    /****
     * 保存到mongodb数据库，根据数据状态变化后进行保存
     * 保存离线设备信息数据
     * @param bxinfo
     */
    public void saveOffline(SuperviseBoxinfo bxinfo, long ctime) {
        if (bxinfo == null) {
            return;
        }
        analyTenantService.getTenantId(bxinfo.getUuid());
        //查询出设备的当前状态信息
        SuperviseBoxinfo bty = boxInfoService.getBoxInfoByBno(bxinfo.getUuid());//查询矩阵盒子实体数据
        if (bty == null) {
            return;
        }

        Integer slid = bty.getSlId();
        //判断获取最新状态是否为4，离线状态，如果是就不需要状态变更，如果不是4才进行状态变更
        if (!bty.getStatus().equals(MachineConstant.MA_OFF)) {
            Date nowtime = new Date(ctime);
            bty.setStatus(MachineConstant.MA_OFF); //状态设置为离线状态为4
            //保存设备上传状态信息，保存到mysql数据库中为零时数据 status 默认为4
            //设置速度为0
            bty.setDspeed(Double.valueOf(0));
            bty.setUpdateAt(nowtime);
            // bty.setSlId(0);//表示设备离线了，设置状态表信息内容。
            //离线后需要进行离线数据的更新内容；设定interval间隔状态表中
            //TODO 需要插入 superviseInterval数据表中
            // TODO 需要修改  saveInterval(bty, "4", bty.getNumberOfDay(), ctime);
            boxInfoService.saveOrUpdate(bty);
            //String tenantId = analyTenantService.getTenantId(bxinfo.getUuid());
            //save_nosql(tenantId, bty.getStatus(), bty.getUuid(), bty.getNumberOfDay(), ctime, 0);//离线盒子数据存0

            // setHalfPointData(bty, MachineConstant.MA_OFF, bty.getNumberOfDay(), ctime, 0, 0,sendTime);
            log.info("离线设备状态修改为4================::uuid:" + bxinfo.getUuid());
        } else {
            log.info("离线设备状态已经是4================::uuid:" + bxinfo.getUuid());
            return;
        }
    }

    /****
     * 保存mongdb 即时数据信息内容
     * @param coldata
     * @return
     */
//    public boolean savemongdb(CollectData coldata) {
//        String tenantId = analyTenantService.getTenantId(coldata.getMid());
//        boolean result = machineService.addBasedb(tenantId, coldata);
//        log.info("add-BASEDB--mongobd信息。uid-base：" + coldata.getMid() + ",uindex:" + coldata.getUindex() + " count :" + coldata.getMc() + ",mtc:" + coldata.getMtc());
//        return result;
//    }

    /****
     * 保存mongdb 即时数据信息内容
     * @param bladeData
     * @return
     */
//    public boolean saveBladeData(BladeData bladeData) {
//        boolean result = machineService.addBladeData(bladeData);
//        log.info("add-BLADEDATA--mongobd信息。uid-base：" + bladeData.getMid() + ",uindex:" + bladeData.getUindex() + " count :" + bladeData.getMc() + ",mtc:" + bladeData.getMtc());
//        return result;
//    }

    /****
     * 保存mongdb 即时数据信息内容
     * @param conmachrs
     * @return
     */
//    public boolean saveConmachRs(ConmachRs conmachrs) {
//        boolean result = machineService.addConmachRs(conmachrs);
//        log.info("add-BLADEDATA--mongobd信息。uid-base：" + conmachrs.getUuid() + ",uindex:" + conmachrs.getUindex());
//        return result;
//    }

    /****
     * 分析数据信息；整点和半点进行mongodb数据插入；状态变化进行数据插入
     * @param ctime
     * @param boxInfoEntity
     */
    public Integer setHalfPointData(SuperviseBoxinfo boxInfoEntity, String status, int numOfDaynum, Long ctime, Integer number, Integer energyNum, Date sendTime) {
        SuperviseShiftcount newIntval = new SuperviseShiftcount();//初始化
        //开机的第一条数据
        if (boxInfoEntity != null && (boxInfoEntity.getSlId() == null || boxInfoEntity.getSlId() == 0)) {
            newIntval.setUuid(boxInfoEntity.getUuid());
            newIntval.setMaId(boxInfoEntity.getMaId());
            newIntval.setStatus(status);//最新状态信息
            newIntval.setStartNum(numOfDaynum);//今日第一次数据暂时没有结束数据
            newIntval.setTargetTime(DateUtil.refNowDay().replaceAll("-", ""));
            newIntval.setStartTime(sendTime);
            newIntval.setNumber(number);//盒子数量保存
            newIntval.setCreateAt(new Date(ctime));
            newIntval.setModel(0);//表示开始或离线类型；
            newIntval.setEnergyNum(energyNum);
            //获取上一条的计数信息
            SuperviseShiftcount lastCountInfo = intervalMapper.getLastByUuid(boxInfoEntity.getUuid());
            if (lastCountInfo != null) {
                newIntval.setInPcout(lastCountInfo.getPcout());
                newIntval.setInNum(lastCountInfo.getNumber());
                //表示清零了
                if (number < lastCountInfo.getNumber()) {
                    newIntval.setPcout(number);
                } else if (lastCountInfo.getStartNum().equals(newIntval.getStartNum())) {
                    newIntval.setPcout(0);
                } else if (lastCountInfo.getStatus().equals(4)) {
                    //离线特殊处理盒子计数为0
                    newIntval.setPcout(newIntval.getStartNum() - lastCountInfo.getStartNum());
                } else {
                    newIntval.setPcout(number - lastCountInfo.getNumber());
                }
            }
            isHourlyLastAlg.put(boxInfoEntity.getUuid(), intervalService.saveAlg(newIntval));
            return newIntval.getPcout();
        }
        return 0;

    }

//    public static void main(String[] args) throws ParseException {
//        String start = "20:00:00";
//        String end = "08:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String formatTime = sdf.format(new Date());
//        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date startTime = simpleDate.parse(formatTime + (" ") + start);
//        Date endTime = simpleDate.parse(formatTime + (" ") + end);
//        if (endTime.getTime() < startTime.getTime()) {
//            endTime = DateTimeUtil.getNextDay(endTime, 1);
//        }
//        String targetDay = null;
//        String e = "01:00:00";
//        Date eTime = simpleDate.parse(formatTime + (" ") + e);
//        if (eTime.getHours() < endTime.getHours() && eTime.getHours() < startTime.getHours()) {
//            startTime = DateTimeUtil.getFrontDay(startTime, 1);
//            endTime = simpleDate.parse(formatTime + (" ") + end);
//            targetDay = sdf.format(startTime);
//        }
//
//        System.out.println(endTime);
//        System.out.println(targetDay);
//
//    }
}
