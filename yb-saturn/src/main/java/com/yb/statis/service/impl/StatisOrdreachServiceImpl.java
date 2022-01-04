package com.yb.statis.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.order.mapper.OrderOrdinfoMapper;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.entity.StatisReachremark;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.statis.mapper.StatisReachremarkMapper;
import com.yb.statis.request.StatisOrdreachPageRequest;
import com.yb.statis.request.StatisOrdreachSaveUpdateRequest;
import com.yb.statis.service.StatisOrdreachService;
import com.yb.statis.vo.*;
import com.yb.system.dict.entity.Dict;
import com.yb.system.dict.mapper.SaDictMapper;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchOrdoeeMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.vo.WorkbatchOrdlinkShiftVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springblade.common.modelMapper.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2018-2019, 成都艾弗克斯科技有限公司
 *
 * @author my
 * @date 2020-06-11
 * Description: 设备订单达成率_yb_statis_ordreach ServiceImpl
 */
@Service
@Slf4j
public class StatisOrdreachServiceImpl extends ServiceImpl<StatisOrdreachMapper, StatisOrdreach> implements StatisOrdreachService {

    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private WorkbatchOrdoeeMapper workbatchOrdoeeMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;
    @Autowired
    private StatisReachremarkMapper statisReachremarkMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;

    @Autowired
    private SaDictMapper saDictMapper;

    static long nd = 1000 * 24 * 60 * 60;
    static long nh = 1000 * 60 * 60;

    static int rate = 60;

    @Override
    public List<StatisOrdreachListVO> list(StatisOrdreachPageRequest request) {
//        List<StatisOrdreach> list = statisOrdreachMapper.list(request);
//        /*查询订单客户名称和批次编号*/
//        if (list.isEmpty()) {
//            log.info("暂无实时达成率数据 ");
//            return new ArrayList<>();
//        }
//        List<StatisOrdreachListVO> vos = new ArrayList<>();
//        Map<String, List<StatisOrdreach>> collect = list.stream().collect(Collectors.groupingBy(StatisOrdreach::getMaName));
//        collect.forEach((k, v) -> {
//            StatisOrdreachListVO statisOrdreachListVO = new StatisOrdreachListVO();
//            statisOrdreachListVO.setName(k);
//            List<StatisOrdreachListRateVO> statisOrdreachListRateVOS = new ArrayList<>();
//            v.forEach(o -> {
//                StatisOrdreachListRateVO statisOrdreachListRateVO = new StatisOrdreachListRateVO();
//                statisOrdreachListRateVO.setHour(o.getTargetHour());
//                statisOrdreachListRateVO.setReachRate(o.getReachRate());
//                statisOrdreachListRateVO.setRealCount(o.getRealCount());
//                statisOrdreachListRateVO.setStandardNum(o.getPlanNum() + "");
//                statisOrdreachListRateVOS.add(statisOrdreachListRateVO);
//            });
//            statisOrdreachListVO.setStatisOrdreachListRateVOS(statisOrdreachListRateVOS);
//            vos.add(statisOrdreachListVO);
//        });

        return null;
    }

    /**
     * 小时达成率
     *
     * @param request
     * @return
     */
    @Override
    public List<StatisOrdreachListVO> hourRateList(StatisOrdreachPageRequest request) {
//        if (request.getWsId() != null) {
//            WorkbatchMachShiftVO WorkbatchMachShiftVO = workbatchShiftMapper.findShiftByMaIdAndWsId(request.getMaId(), request.getWsId());
//            long sTime = WorkbatchMachShiftVO.getStartTime().getTime();
//            long eTime = WorkbatchMachShiftVO.getEndTime().getTime();
//            if (sTime >= eTime) {
//                //为夜班
//                String targetDay = request.getTargetDay();
//                try {
//                    Date format = DateTimeUtil.format(targetDay, DateTimeUtil.DEFAULT_DATE_FORMATTER);
//                    Date nextDay = DateTimeUtil.getNextDay(format);
//                    String nexDay = DateTimeUtil.format(nextDay, DateTimeUtil.DEFAULT_DATE_FORMATTER);
//                    request.setNextDay(nexDay);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        List<StatisOrderreachListVO> list = statisOrdreachMapper.list(request);
        if (list.isEmpty() || list.get(0).getMaId() == null) {
            log.info("暂无实时达成率数据");
            return new ArrayList<>();
        }
        List<StatisOrdreachListVO> vos = new ArrayList<>();
        Map<String, List<StatisOrderreachListVO>> collect = list.stream().collect(Collectors.groupingBy(StatisOrdreach::getMaName));
        collect.forEach((k, v) -> {
            StatisOrdreachListVO statisOrdreachListVO = new StatisOrdreachListVO();
            statisOrdreachListVO.setName(k);
            List<StatisOrdreachListRateVO> statisOrdreachListRateVOS = new ArrayList<>();
            v.forEach(o -> {
                String wbNo = o.getWbNo();
                StatisOrdreachListRateVO statisOrdreachListRateVO = new StatisOrdreachListRateVO();
                statisOrdreachListRateVO.setId(o.getId());
                statisOrdreachListRateVO.setHour(o.getTargetHour());
                statisOrdreachListRateVO.setStandardNum(o.getSumPlanNUm().intValue());
                statisOrdreachListRateVO.setRealCount(o.getSumRelyCount().intValue());
                statisOrdreachListRateVO.setWbNo(wbNo);
                statisOrdreachListRateVO.setPdName(o.getPdName());//更换为客户名称
                statisOrdreachListRateVO.setRemark(o.getRemark());
                statisOrdreachListRateVO.setReachRate(o.getReachRate() == null ? BigDecimal.ZERO : o.getReachRate());
                statisOrdreachListRateVO.setCmName(o.getPdName());
                statisOrdreachListRateVO.setReachIslock(o.getReachIslock());//增加锁定的字段信息
                statisOrdreachListRateVO.setPlanTime(o.getPlanTime());//设定计划可使用时间
                statisOrdreachListRateVOS.add(statisOrdreachListRateVO);
            });
            statisOrdreachListVO.setStatisOrdreachListRateVOS(statisOrdreachListRateVOS);
            vos.add(statisOrdreachListVO);
        });
        return vos;
    }

    public static void main(String[] args) {
        try {
            Date format = DateTimeUtil.format("2020-08-1", DateTimeUtil.DEFAULT_DATE_FORMATTER);
            System.out.println(format);
            System.out.println(DateTimeUtil.getNextDay(format));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改达成率备注
     *
     * @param request
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StatisOrdreachSaveUpdateRequest request) {
        StatisOrdreach statisOrdreach = statisOrdreachMapper.selectById(request.getId());
        if (statisOrdreach != null) {
            StatisReachremark statisReachremark = statisReachremarkMapper.selectOne(new QueryWrapper<StatisReachremark>().eq("sr_id", statisOrdreach.getId()));
            if (statisReachremark == null) {
                //为空就插入
                StatisReachremark reachremark = ModelMapperUtil.getStrictModelMapper().map(request, StatisReachremark.class);
                reachremark.setSrId(statisOrdreach.getId());
                String remark = SplicingOreachRemark(reachremark);
                statisOrdreach.setRemark(remark);
                statisOrdreachMapper.updateById(statisOrdreach);
                statisReachremarkMapper.insert(reachremark);
            } else {
                Integer id = statisReachremark.getId();
                //存在就为编辑
                ModelMapperUtil.getStrictModelMapper().map(request, statisReachremark);
                statisReachremark.setId(id);
                String remark = SplicingOreachRemark(statisReachremark);
                statisOrdreach.setRemark(remark);
                statisOrdreachMapper.updateRemark(statisOrdreach.getId(), remark);
                statisReachremarkMapper.updateRemark(statisReachremark);
            }
        }
    }

    String SplicingOreachRemark(StatisReachremark reachremark) {
        StringBuilder builder = new StringBuilder();
        if (reachremark.getProReadyTime() != null) {
            builder.append("生产准备时间:").append(reachremark.getProReadyTime()).append("分钟,");
        }
        if (reachremark.getDeviceFaultTime() != null) {
            builder.append("设备故障时间:").append(reachremark.getDeviceFaultTime()).append("分钟,");
        }
        if (reachremark.getQualityTestTime() != null) {
            builder.append("品质实验时间:").append(reachremark.getQualityTestTime()).append("分钟,");
        }
        if (reachremark.getTypeSwitchTime() != null) {
            builder.append("品种切换时间:").append(reachremark.getTypeSwitchTime()).append("分钟,");
        }
        if (reachremark.getManageStopTime() != null) {
            builder.append("管理停止时间:").append(reachremark.getManageStopTime()).append("分钟,");
        }
        if (reachremark.getOtherLossTime() != null) {
            builder.append("其他损失时间:").append(reachremark.getOtherLossTime()).append("分钟,");
        }
        if (StringUtils.isNotBlank(reachremark.getOtherLossCause())) {
            builder.append("其他损失事由:").append(reachremark.getOtherLossCause()).append(",");
        }
        if (StringUtils.isBlank(builder.toString())) {
            return null;
        }
        return builder.substring(0, builder.toString().length() - 1);
    }

    /**
     * 小时达成率详情信息
     *
     * @param id
     * @return
     */
    @Override
    public StatisOrdreachVO get(Integer id) {

        StatisReachremark statisReachremark = statisReachremarkMapper.selectOne(new QueryWrapper<StatisReachremark>().eq("sr_id", id));
        if (statisReachremark == null) {
            return new StatisOrdreachVO();
        }
        StatisOrdreachVO vo = ModelMapperUtil.getStrictModelMapper().map(statisReachremark, StatisOrdreachVO.class);

        return vo;
    }

    /**
     * 获取达成率备注字典信息
     *
     * @return
     */
    @Override
    public List<Dict> getReachDictList() {
        List<Dict> reachList = saDictMapper.findReachList();
        return reachList;
    }


    /*****
     * 前期编写测试方法之一，当前已作废未使用
     * @param targetDay
     * @param wsId
     * @param maId
     */
    @Override
    public void updateStatisOrdreach(String targetDay, Integer wsId, Integer maId) {
        //获取班次信息管理
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date startTime = workbatchShiftset.getStartTime();
        Date endTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(startTime, endTime, targetDay);//判断大小后赋值给开始时间和结束时间
        startTime = wsDatetime[0];
        endTime = wsDatetime[1];
        Integer mealStay = workbatchShiftset.getMealStay();
        List<StatisOrdreach> targetHourList = statisOrdreachMapper.statisOrdInitList(targetDay, wsId, maId);

        List<Integer> hourList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        Integer start = Integer.valueOf(simpleDateFormat.format(startTime));
        Integer end = Integer.valueOf(simpleDateFormat.format(endTime));
        while (true) {
            if (start > end) {
                int end1 = end + 24;
                if (start < end1) {
                    if (start >= 24) {
                        start = start - 24;
                    }
                    hourList.add(start);
                    start++;
                } else {
                    break;
                }
            } else {
                if (start < end) {
                    hourList.add(start);
                    start++;
                } else {
                    break;
                }
            }
        }

        Integer stayTime = 0;
        Integer stayCount = 0;
        StatisOrdreach sdIdinfo = null;
        for (Integer hour : hourList) {
            Integer hour1 = 60;
            Integer cont = 0;
            Iterator<StatisOrdreach> iterator = targetHourList.iterator();
            while (iterator.hasNext()) {
                StatisOrdreach statisOrdreach = iterator.next();
                sdIdinfo = (stayCount > 0 || stayTime > 0) ? sdIdinfo : statisOrdreach;
                Integer plannum = (stayCount > 0) ? stayCount : statisOrdreach.getPlanCount();
                WorkbatchOrdoee workbatchOrdoee =
                        workbatchOrdoeeMapper.selectOne(new QueryWrapper<WorkbatchOrdoee>().eq("wk_id", sdIdinfo.getSdId()));
                Integer mouldStay = workbatchOrdoee.getMouldStay();
                Integer speed = workbatchOrdoee.getSpeed();
                if (hour.equals(12)) {
                    stayTime = 60 - mealStay;
                    StatisOrdreach statisOrdreach1 = sdIdinfo;
                    statisOrdreach1.setTargetHour(hour);
                    statisOrdreach1.setPlanCount(0);
                    statisOrdreach1.setId(null);
                    ordreachInsertT(statisOrdreach1);
                    if (stayTime > 0) {
                        StatisOrdreach statisOrdreach2 = sdIdinfo;
                        statisOrdreach1.setTargetHour(hour);
                        statisOrdreach1.setPlanCount(speed * stayTime / rate);
                        statisOrdreach1.setId(null);
                        ordreachInsertT(statisOrdreach1);
                        break;
                    } else {
                        break;
                    }

                }
                if (stayTime > 0 || stayCount > 0) {
                    if (stayCount > 0) {
                        Integer intcot = 0;
                        if (stayCount > speed) {
                            stayCount = stayCount - speed;
                            intcot = hour1 * speed / rate;
                            //新增数据
                            StatisOrdreach statisOrdreach1 = sdIdinfo;
                            statisOrdreach1.setTargetHour(hour);
                            statisOrdreach1.setPlanCount(intcot);
                            statisOrdreach1.setId(null);
                            ordreachInsertT(statisOrdreach1);
                            break;
                        } else {
                            cont = speed - stayCount;
                            intcot = stayCount;
                            stayTime = (int) Math.round(((double) cont / speed) * rate);
                            //新增数据
                            StatisOrdreach statisOrdreach1 = sdIdinfo;
                            statisOrdreach1.setTargetHour(hour);
                            statisOrdreach1.setPlanCount(intcot);
                            statisOrdreach1.setId(null);
                            ordreachInsertT(statisOrdreach1);
                            stayCount = 0;//已经完成所以清零
                            sdIdinfo = statisOrdreach1;
                            iterator.remove();
                            continue;
                        }
                    }
                    if (stayTime > 0) {
                        if (mouldStay >= stayTime) {
                            stayTime = mouldStay - stayTime;
                            break;
                        } else {
                            Integer time = stayTime - mouldStay;
                            cont = (time * speed) / 60;
                            if (sdIdinfo.getTargetHour() == hour) {
                                stayCount = stayCount - cont;
                                statisOrdreachMapper.updateStatisOrdreach(maId, wsId, targetDay, hour, cont, sdIdinfo.getSdId());
                                break;
                            } else {
                                //新增数据
                                StatisOrdreach statisOrdreach1 = sdIdinfo;
                                statisOrdreach1.setTargetHour(hour);
                                statisOrdreach1.setPlanCount(cont);
                                statisOrdreach1.setId(null);
                                ordreachInsertT(statisOrdreach1);
                                sdIdinfo = statisOrdreach1;
                                break;
                            }
                        }
                    }
                } else {
                    if (stayTime == 0) {
                        Integer time = hour1 - mouldStay;
                        stayCount = plannum;
                        if (time >= 0) {
                            cont = (time * speed) / 60;
                            if (sdIdinfo.getTargetHour() == hour) {
                                statisOrdreachMapper.updateStatisOrdreach(maId, wsId, targetDay, hour, cont, sdIdinfo.getSdId());
                                stayCount = stayCount - cont;
                                break;
                            } else {
                                /*--------------*/
                                //新增数据
                                StatisOrdreach statisOrdreach1 = sdIdinfo;
                                statisOrdreach1.setTargetHour(hour);
                                statisOrdreach1.setPlanCount(cont);
                                statisOrdreach1.setId(null);
                                ordreachInsertT(statisOrdreach1);
                                sdIdinfo = statisOrdreach1;
                                break;
                            }
                        } else {
                            stayTime = mouldStay - hour1;
                            //表示一个小时都在换膜
                            if (sdIdinfo.getTargetHour() == hour) {
                                statisOrdreachMapper.updateStatisOrdreach(maId, wsId, targetDay, hour, 0, sdIdinfo.getSdId());
                            } else {
                                //新增数据
                                StatisOrdreach statisOrdreach1 = sdIdinfo;
                                statisOrdreach1.setTargetHour(hour);
                                statisOrdreach1.setPlanCount(0);
                                statisOrdreach1.setId(null);
                                ordreachInsertT(statisOrdreach1);
                                sdIdinfo = statisOrdreach1;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /****
     *
     * @param targetDay
     * @param wsId
     * @param maId
     */
    public void setAddOrdreach(String targetDay, Integer wsId, Integer maId) {
        //设备信息
        MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date startTime = workbatchShiftset.getStartTime();
        Date endTime = workbatchShiftset.getEndTime();
        List<WorkbatchOrdlink> workbatchOrdlinkLs =
                workbatchOrdlinkMapper.selectListBytargetDay(targetDay, wsId, maId);
        if (!workbatchOrdlinkLs.isEmpty()) {
            Date[] wsDatetime = setWsDatetime(startTime, endTime, targetDay);//判断大小后赋值给开始时间和结束时间
            startTime = wsDatetime[0];
            endTime = wsDatetime[1];

            for (int i = 0; i < workbatchOrdlinkLs.size(); i++) {
                WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkLs.get(i);
                //获取oee设定信息
                WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(workbatchOrdlink.getId());

                //设备首单
                if (i == 0) {
                    workbatchOrdlink.setStartTime(startTime);
                    endTime = new Date(startTime.getTime() + (workbatchOrdoee.getPlanTotalTime() * 60 * 1000));
                    workbatchOrdlink.setCloseTime(endTime);
                }
                //同一台设备非首单计算开始结束
                if (workbatchOrdlink.getMaId().equals(maId) && i != 0) {
                    workbatchOrdlink.setStartTime(endTime);
                    endTime = new Date(endTime.getTime() + (workbatchOrdoee.getPlanTotalTime() * 60 * 1000));
                    workbatchOrdlink.setCloseTime(endTime);
                }

                StatisOrdreach statisOrdreach = new StatisOrdreach();
                statisOrdreach.setMaName(machineMainfo.getName());
                statisOrdreach.setMaId(machineMainfo.getId());
                statisOrdreach.setWsId(wsId);
                statisOrdreach.setWsName(workbatchShiftset.getCkName());
                //statisOrdreach.setPdName(workbatchOrdlink.getPdName());
                statisOrdreach.setPdName(workbatchOrdlink.getCmName()); //当前把客户名称放入产品名称中,前端不动
                statisOrdreach.setSpeed(workbatchOrdoee.getSpeed());
                statisOrdreach.setSdId(workbatchOrdlink.getId().toString());
                statisOrdreach.setStartTime(workbatchOrdlink.getStartTime());
                statisOrdreach.setEndTime(workbatchOrdlink.getCloseTime());
                statisOrdreach.setPlanNum(workbatchOrdlink.getPlanNum() + "");
                statisOrdreach.setTargetHour(workbatchOrdlink.getStartTime().getHours());
                statisOrdreach.setTargetDay(workbatchOrdlink.getSdDate());
                statisOrdreach.setEndTime(workbatchOrdlink.getCloseTime());
                statisOrdreach.setCreateAt(new Date());
                maId = statisOrdreach.getMaId();
                ordreachInsertT(statisOrdreach);
            }
        }
    }

    /***
     * 很特别小时达成率的初始化，暂停用方法
     */
    public void setNowdayOrdreach(Integer wsId) {
//        DBIdentifier.setProjectCode("xingyi");
        String targetDay = DateUtil.refNowDay();
        log.info("开始初始化达成率信息:[hour:{}]", targetDay);
        //WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.getShiftsetByNow();
        //Integer wsId = workbatchShiftset.getId();
        List<Integer> maIdLs = workbatchOrdlinkMapper.getMachineBytargetDay(targetDay, wsId);
        if (maIdLs != null && maIdLs.size() > 0) {
            for (Integer maId : maIdLs) {
                setOrdreachBytargetDay(targetDay, wsId, maId);//TODO  WWWWWWW
                //setAddOrdreach(targetDay, wsId, maId);
                //updateStatisOrdreach(targetDay, wsId, maId);
            }
        }
        log.info("初始化达成率信息成功:[hour:{}]", targetDay);
    }


    /**
     * @param targetDay
     * @param wsId
     * @param maId
     */
    @Override
    public void setOrdreachByMaid(String targetDay, Integer wsId, Integer maId) {
        setAddOrdreach(targetDay, wsId, maId);
        updateStatisOrdreach(targetDay, wsId, maId);
    }

    /****
     * 根据开始时间、结束时间、排产日期 返回数组
     * @param classStartTime
     * @param classEndTime
     * @param sdDate
     * @return
     */
    public static Date[] setWsDatetime(Date classStartTime, Date classEndTime, String sdDate) {
        Date[] wsDatetime = new Date[2];
        //判断开始时间大于结束时间，需要加一天在实际时间
        Date startTime = DateUtil.toDate(sdDate + " " + DateUtil.format(classStartTime, "HH:mm:ss"), null);
        Date EndTime = DateUtil.toDate(sdDate + " " + DateUtil.format(classEndTime, "HH:mm:ss"), null);
        if (classStartTime.compareTo(classEndTime) > 0) {
            EndTime = DateUtil.addDayForDate(EndTime, 1);
        }
        wsDatetime[0] = startTime;
        wsDatetime[1] = EndTime;
        return wsDatetime;
    }

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++第二种算式方法++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
     *********/

    /****
     * 定时任务，早上7点进行排产单，8点之前进行工单排产；如果已经有了就不需要再进行更新了。
     */
//    @Scheduled(cron = "0 50 7,19 * * ?")
    public void setPlanOrdreach() {
        DBIdentifier.setProjectCode("xingyi");//todo 修改兴艺的统计数据
        Date nowtime = new Date();
        String targetDay = DateUtil.refNowDay(nowtime);
        Integer h = nowtime.getHours();
        if (h <= 12) { //小于12点表示要进行工单排产的统一计算；
            Date toDay = DateUtil.addDayForDate(nowtime, -1);
            targetDay = DateUtil.refNowDay(toDay);
        } else {
            targetDay = DateUtil.refNowDay();
        }
        //Integer wsId = (hour >= 19) ? 46 : 45; //默认白班、夜班操作处理
//        List<Integer> maIds = workbatchOrdlinkMapper.getMachineBySaDate(targetDay);
//        if (!maIds.isEmpty()) {
//            for (Integer maId : maIds) {
//                setOrdreachBytargetDay(targetDay, wsId, maId);
//            }
//        }
    }


    /*******
     *
     * @param targetDay
     * @param wsId
     * @param maId
     */
    public void setOrdreachBytargetDay(String targetDay, Integer wsId, Integer maId) {
        Date newday = new Date();
        Integer islock = getIslockReach(targetDay, wsId, maId);//查询锁定状态为1表示锁定，不能够修改
        if (islock != null && islock == 1) {
            return;
        }
        //清除当班的信息内容
        //clearOrdreach(targetDay, wsId, maId);
        //设定计划达成率
        //获取班次信息管理
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, targetDay);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        //获取每小时有多少分钟可用，并且跨夜的使用了24以上计数，需要进行24小时的减法运算小时数
//        List<Integer> hourList = getHoursByClass(classStartTime, classEndTime, workbatchShiftset);
        Map<Integer, Integer> hourList = getHoursForUptime(classStartTime, classEndTime, workbatchShiftset);

        MachineMainfo mainfo = machineMainfoMapper.getMachineMainfo(maId);

        //获取该班次的排产内容
        List<WorkbatchOrdlinkShiftVO> worklinkshiftLs = workbatchOrdlinkMapper.getListByshift(targetDay, wsId, maId);
        Map<String, Object> stinfo = new HashMap<>();
        stinfo.put("staytime", 0);//上一个小时还剩余时间
        stinfo.put("staycont", 0);//上个小时还剩余生产数量
        stinfo.put("isbreak", false);//是否换单操作
        stinfo.put("ismould", true);//是否为换模时间 否：表示已进入正式生产 是：表示换模时间
        stinfo.put("wklinksh", null);
        stinfo.put("oldwklinksh", null);
        stinfo.put("newday", newday);

        int classHours = classStartTime.getHours();
        Map<Integer, Integer> hasHours = new HashMap<>();//表示已经处理过的小时数进行跳过
        //设置达成率基础信息
        StatisOrdreach ordreach = new StatisOrdreach();
        Map<Integer, Integer> realctLs = getRealcoutByHour(classStartTime, classEndTime, maId);//若是夜班需要跨时间
        //判定排产信息，返回
        if (!worklinkshiftLs.isEmpty()) {
            Iterator<WorkbatchOrdlinkShiftVO> itera = worklinkshiftLs.iterator();
            //工单生产完了，需要进行换单执行
            while (itera.hasNext()) {//遍历排产单
                WorkbatchOrdlinkShiftVO worklkshift = itera.next();
                stinfo.put("wklinksh", worklkshift);//设定当前订单状态信息

                ordreach.setWsId(wsId);//班次ID信息
                ordreach.setWsName(workbatchShiftset.getCkName());//班次名称
                ordreach.setMaName(mainfo.getName());
                ordreach.setCreateAt(newday);//设定创建时间
                ordreach.setUpdateAt(newday);//设定创建时间
                ordreach.setMaId(maId);
                ordreach.setTargetDay(targetDay);

//                Integer speed = workbatchOrdoee.getSpeed();//生产时速
//                Integer mouldStay = workbatchOrdoee.getMouldStay();//换模时间
//                Integer alltime = (produceNum * 60) / speed;//产品所需要的的总生产时间
//                ordreach.setSpeed(speed);
//                Integer planNum = workbatchOrdlink.getPlanNum();

                //时间循环跳转
                for (int j = 0; j < hourList.size(); j++) {
                    Integer currhour = classHours + j;
                    currhour = (currhour > 23) ? currhour - 24 : currhour;//当当前小时；
                    //判断已经有剩余时间，无需更换小时数据信息
                    if (hasHours.containsKey(currhour)) {
                        continue;
                        //把已经存在的小时数进行清理，以免重复循环操作
                    }
                    //设定每个小时的实际数量内容，如果没有就为数据初始化
                    if (realctLs != null && !realctLs.isEmpty()) {
                        //设置对应这个小时的实际数量，如果没有就设置为null值信息
                        ordreach.setRealCount(realctLs.get(currhour));
                    } else {
                        ordreach.setRealCount(null);
                        ordreach.setReachRate(null);
                    }

                    Boolean isbreak = (Boolean) stinfo.get("isbreak");//是否换单操作
                    //判断之前已经包含本次排产信息，就不做数据追加字符串信息
                    if (isbreak) {
                        String sdIdstr = (ordreach.getSdId() != null && ordreach.getSdId().length() > 0) ? ordreach.getSdId() + "|" : "";
                        if (!ordreach.getSdId().equalsIgnoreCase(worklkshift.getId() + ""))
                            ordreach.setSdId(sdIdstr + worklkshift.getId());//派单id
                        String wbNostr = (ordreach.getWbNo() != null && ordreach.getWbNo().length() > 0) ? ordreach.getWbNo() + "|" : "";
                        if (!ordreach.getWbNo().equalsIgnoreCase(worklkshift.getWbNo()))
                            ordreach.setWbNo(wbNostr + worklkshift.getWbNo());//批次单编号
                        String cmName = (worklkshift.getCmName() != null && worklkshift.getCmName().length() > 0) ? worklkshift.getCmName() : "";
                        String pdname = (ordreach.getPdName() != null && ordreach.getPdName().length() > 0) ? ordreach.getPdName() : "";
                        if (!pdname.equalsIgnoreCase(cmName))
                            ordreach.setPdName(pdname + "|" + cmName);//设置客户名称信息
                    } else {
                        //TODO  WWWWWWWwwwwwwwww
                        worklkshift = (worklkshift == null || worklkshift.getId() == null) ? new WorkbatchOrdlinkShiftVO() : worklkshift;
                        String sdIdstr = (worklkshift.getId() != null) ? worklkshift.getId().toString() : "";
                        ordreach.setSdId(sdIdstr);
                        ordreach.setWbNo(worklkshift.getWbNo());
                        ordreach.setPdName(worklkshift.getCmName());//设置客户名称信息
                    }
                    ordreach.setSpeed(worklkshift.getSpeed());
                    Integer havemin = hourList.get(currhour);//设置每小时的分钟数量;
                    // 设定有多少分钟可以用于生产；
                    ordreach.setPlanTime(havemin);//每小时的可用时间
                    //设定小时数
                    ordreach.setTargetHour(currhour);
                    //log.info("===================j:" + j);
                    isbreak = (Boolean) stinfo.get("isbreak");
                    if (j == 0 && !isbreak) {
                        //换版时间大于已有时间
                        stinfo.putAll(setOrdreachByOnce(stinfo, currhour, ordreach, havemin, hasHours));
                        isbreak = (Boolean) stinfo.get("isbreak");
                        if (isbreak) {
                            break;
                        }
                    } else {
                        Integer staytime = (Integer) stinfo.get("staytime");
                        Integer staycont = (Integer) stinfo.get("staycont");
                        //刚好整点换单后处理信息
                        if (staycont == 0 && staytime == 0) {
                            stinfo.putAll(setJustStart(stinfo, currhour, ordreach, havemin, hasHours));
                            isbreak = (Boolean) stinfo.get("isbreak");
                            if (isbreak) {
                                break;
                            }
                        }

                        //havemin
                        if (staytime > 0) {
                            stinfo.putAll(setOrdreachByStaytime(stinfo, currhour, ordreach, havemin, hasHours));
                        }
                        isbreak = (Boolean) stinfo.get("isbreak");
                        if (isbreak) {
                            break;
                        }
                        if (staycont > 0) {
                            stinfo.putAll(setOrdreachByStaycount(stinfo, currhour, ordreach, havemin, hasHours));
                        }
                        isbreak = (Boolean) stinfo.get("isbreak");
                        if (isbreak) {
                            break;
                        }

                    }
                    System.out.println("XXX小时达成数据：currhour:" + currhour);
                }
                System.out.println("YYY测试数据信息：hourls:" + hasHours.size());
            }
            //若循环最后一次任务，保存最后的数据信息
            Integer staytime = (Integer) stinfo.get("staytime");
            if (staytime > 0) {
                ordreachInsertT(ordreach);
            }
        }
        //补零
        setFullzero(targetDay, mainfo, workbatchShiftset, realctLs, newday, hasHours);//不够信息内容
    }

    /***
     * 换班的第一个小时的判断内容
     * @param stinfo
     * @param hour
     * @param ordreach
     * @param havemin
     * @param hasHours
     * @return
     */
    private Map<String, Object> setOrdreachByOnce(Map<String, Object> stinfo, Integer hour, StatisOrdreach ordreach, Integer havemin, Map<Integer, Integer> hasHours) {
        //换单以后，新的订单sdId
        WorkbatchOrdlinkShiftVO linksh = (WorkbatchOrdlinkShiftVO) stinfo.get("wklinksh");
        ordreach.setTargetHour(hour);//设定小时数量
        //ordreach.setPdName(linksh.getCmName());
        //ordreach.setWbNo(linksh.getWbNo());
        int mouldstay = (linksh != null && linksh.getMouldStay() != null) ? linksh.getMouldStay() : 20;//如果换模时间为空就默认20分钟
        int speed = (linksh != null && linksh.getSpeed() != null && linksh.getSpeed() > 0) ? linksh.getSpeed() : 3500;//如果标准时速为空就默认3000每小时
        if (mouldstay > havemin) {
            ordreach.setPlanCount(0);
            ordreach.setPlanNum(0 + "");
            int staytime = mouldstay - havemin;
            ordreach.setTargetHour(hour);
            ordreachInsertT(ordreach);
            stinfo.put("isbreak", false);
            stinfo.put("staycont", 0);
            stinfo.put("staytime", staytime);
            stinfo.put("ismould", true);//换模时间大于当前已有的时间
            ordreach.setPlanCount(0);//新增后跳新的小时数量
            ordreach.setPlanNum("");//重置新的数量信息
            hasHours.put(hour, hour);
        } else {
            //再生产，剩余数量的统计
            Integer nowtime = havemin - mouldstay;//减去换型时间剩余生产时间
            int fulltime = (int) Math.round(((double) linksh.getPlanNum() * 60) / speed);
            if (fulltime > nowtime) {//生产部分内容
                int productnum = (speed * nowtime) / 60;
                int staycont = linksh.getPlanNum() - productnum;
                ordreach.setPlanCount(productnum);
                ordreach.setPlanNum(productnum + "");//小时计划生产数量
                hasHours.put(hour, hour);
                ordreachInsertT(ordreach);
                stinfo.put("isbreak", false);
                stinfo.put("staycont", staycont);
                stinfo.put("staytime", 0);
                stinfo.put("ismould", false);//全部生产时间大于当前生产剩余生产时间，所以还是生产时间
                ordreach.setPlanCount(0);//新增后跳新的小时数量
                ordreach.setPlanNum("");//重置新的数量信息
            } else {
                //生产全部产品内容，还剩余时间然后进行换单
                int staytime = nowtime - fulltime; //本小时还剩余时间
                //int partnum = (speed / 60) * nowtime;
                ordreach.setPlanCount(linksh.getPlanNum());
                ordreach.setPlanNum(linksh.getPlanNum() + "");//设定小时计划生产数量
                ordreach.setTargetHour(hour);
                stinfo.put("ismould", true);//生产完成全部产品，应该是进入换模时间
                stinfo.put("isbreak", true);
                stinfo.put("staycont", 0);
                stinfo.put("staytime", staytime);
            }
        }
        return stinfo;
    }

    /***
     *  根据剩余时间，剩余时间有两种情况，
     * 1、剩余换模时间未完成：需要进行继续换模时间，如果当前小时能在一小时内换模完成，那就执行剩余时间的生产数量的计算，并且判定是否能够完成该订单，若不能完成，则剩余数量信息，时间信息清零
     * 2、剩余时间需要进行【换单】情况，需要进行换单换模时间处理（判断以换单id相同为准）
     * @param stinfo
     * @param hour
     * @param ordreach
     */
    private Map<String, Object> setOrdreachByStaytime(Map<String, Object> stinfo, Integer hour, StatisOrdreach ordreach, Integer havemin, Map<Integer, Integer> hasHours) {
        Boolean isbreak = (Boolean) stinfo.get("isbreak");
        Integer staytime = (Integer) stinfo.get("staytime");
        Boolean ismould = (Boolean) stinfo.get("ismould");
        //换单以后，新的订单sdId
        WorkbatchOrdlinkShiftVO linksh = (WorkbatchOrdlinkShiftVO) stinfo.get("wklinksh");
        ordreach.setTargetHour(hour);//设定小时数量

        int mouldstay = (linksh != null && linksh.getMouldStay() != null) ? linksh.getMouldStay() : 20;//如果换模时间为空就默认20分钟
        int speed = (linksh != null && linksh.getSpeed() != null && linksh.getSpeed() > 0) ? linksh.getSpeed() : 3500;//如果标准时速为空就默认3000每小时
        //判断上一个是换单来的数据信息
        if (isbreak) {
            //如果换模时间大于预留时间，就需要进行当前小时插入信息，并且进行isbreak重置为false
            if (mouldstay > staytime) {
                ordreach.setPlanCount(ordreach.getPlanCount() + 0);//保存历史时间记录数量
                ordreach.setPlanNum(ordreach.getPlanNum() + "|" + 0);
                staytime = mouldstay - staytime;
                stinfo.put("isbreak", false);
                stinfo.put("staycont", 0);
                stinfo.put("ismould", true);//进入换模时间
                stinfo.put("staytime", staytime);
                ordreach.setTargetHour(hour);
                ordreachInsertT(ordreach);
                ordreach.setPlanCount(0);//新增后跳新的小时数量
                ordreach.setPlanNum("");//重置新的数量信息
                hasHours.put(hour, hour);
            } else {
                //换模时间小于剩余时间，进行换模后再计算如何进入生产
                int nowtime = staytime - mouldstay;//当前剩余时间进行生产数量统计
                if (nowtime == 0) {//刚好下个小时直接生产
                    Integer productnum = 0;
                    ordreach.setPlanCount(ordreach.getPlanCount() + productnum);
                    ordreach.setPlanNum(ordreach.getPlanNum() + "|" + productnum);
                    stinfo.put("isbreak", false);
                    stinfo.put("staycont", linksh.getPlanNum());//表示为0，换型时间刚刚好还完处理，进入生产数据
                    stinfo.put("staytime", 0);
                    stinfo.put("ismould", false);//剩余时间全部用来生产，还有没有生产完成产品，所以状态是生产产品过程
                    ordreach.setTargetHour(hour);
                    ordreachInsertT(ordreach);
                    ordreach.setPlanCount(0);//新增后跳新的小时数量
                    ordreach.setPlanNum("");
                    hasHours.put(hour, hour);
                }
                //看剩下的时间是否还够继续生产数量，生产全部需要的时间
                int fulltime = (int) Math.round(((double) linksh.getPlanNum() * 60) / speed);
                if (fulltime > nowtime) {
                    Integer productnum = (speed * nowtime) / 60;
                    Integer staycont = linksh.getPlanNum() - productnum;
                    ordreach.setPlanCount(ordreach.getPlanCount() + productnum);
                    ordreach.setPlanNum(ordreach.getPlanNum() + "|" + productnum);
                    stinfo.put("isbreak", false);
                    stinfo.put("staycont", staycont);
                    stinfo.put("staytime", 0);
                    stinfo.put("ismould", false);//剩余时间全部用来生产，还有没有生产完成产品，所以状态是生产产品过程
                    ordreach.setTargetHour(hour);
                    ordreachInsertT(ordreach);
                    ordreach.setPlanCount(0);//新增后跳新的小时数量
                    ordreach.setPlanNum("");
                    hasHours.put(hour, hour);
                } else {
                    if (fulltime == nowtime) {
                        ordreach.setPlanCount(ordreach.getPlanCount() + linksh.getPlanNum());
                        ordreach.setPlanNum(ordreach.getPlanNum() + "|" + linksh.getPlanNum());
                        stinfo.put("staycont", 0);
                        stinfo.put("staytime", 0);
                        stinfo.put("ismould", true);//剩余时间全部用来生产，还有没有生产完成产品，所以状态是生产产品过程
                        ordreach.setTargetHour(hour);
                        ordreachInsertT(ordreach);
                        ordreach.setPlanCount(0);//新增后跳新的小时数量
                        ordreach.setPlanNum("");
                        hasHours.put(hour, hour);
                        stinfo.put("isbreak", true);//当刚刚好的时候就执行换单操作
                    } else {
                        Integer staynow = nowtime - fulltime;//用换模剩余的时间 全部生产后还剩余时间
                        //Integer productnum = speed / 60 * nowtime;
                        ordreach.setPlanCount(ordreach.getPlanCount() + linksh.getPlanNum());
                        ordreach.setPlanNum(ordreach.getPlanNum() + "|" + linksh.getPlanNum());
                        stinfo.put("isbreak", true);
                        stinfo.put("staycont", 0);
                        stinfo.put("ismould", true);//生产完成后，进入新的换单模式，就进入换模管理
                        stinfo.put("staytime", staynow);//剩余的时间后进行换单
                        stinfo.put("oldwklinksh", linksh);
                        ordreach.setTargetHour(hour);
                    }
                }
            }
        } else {
            //非换单来的时间，表示换型生产未完成，继续接着换版。到了新的可用分钟的小时阶段，并且与剩余时间比较，如果大于剩余时间，表示可以完成换版任务，小于时间，不能完成换版，并且还要遗留下一个小时进行换版内容
            if (havemin > staytime) {
                int nowtime = havemin - staytime;//当前小时减去换型时间，剩余的时间
                int fulltime = (int) Math.round(((double) linksh.getPlanNum() * 60) / speed);
                String pnum = ordreach.getPlanNum();
                pnum = (pnum != null && pnum.length() > 0) ? pnum + "|" : "0|";
                if (fulltime > nowtime) {
                    //减去换型时间后继续生产，并且把剩余数量重置清楚
                    Integer productnum = (speed * nowtime) / 60;
                    Integer staycont = linksh.getPlanNum() - productnum;
                    ordreach.setPlanCount(ordreach.getPlanCount() + productnum);
                    ordreach.setPlanNum(pnum + productnum);
                    stinfo.put("isbreak", false);
                    stinfo.put("staycont", staycont);
                    stinfo.put("staytime", 0);
                    stinfo.put("ismould", false);//换单后，剩余的时间判断是否为换模
                    ordreach.setTargetHour(hour);
                    ordreachInsertT(ordreach);
                    ordreach.setPlanCount(0);//新增后跳新的小时数量
                    ordreach.setPlanNum("");
                    hasHours.put(hour, hour);
                } else {
                    //继续生产后还有剩余时间，然后进行换单操作
                    Integer staynow = nowtime - fulltime;//用换模剩余的时间
                    //Integer productnum = speed / 60 * fulltime;
                    ordreach.setPlanCount(ordreach.getPlanCount() + linksh.getPlanNum());
                    ordreach.setPlanNum(pnum + linksh.getPlanNum());
                    stinfo.put("isbreak", true);
                    stinfo.put("staycont", 0);
                    stinfo.put("ismould", true);//换单后，剩下时间需要重新换型
                    stinfo.put("staytime", staynow);//剩余的时间后进行换单
                    stinfo.put("oldwklinksh", linksh);
                    ordreach.setTargetHour(hour);
                }
            } else {
                //小时剩余时间全部用于换版内容
                if (havemin <= staytime) {
                    int nowtime = staytime - havemin;//小时内可用时间全部用户上到换版工序中，还有剩余staytime
                    ordreach.setPlanCount(0);//全部用于换版，无需生产数据
                    ordreach.setTargetHour(hour);
                    stinfo.put("isbreak", false);
                    stinfo.put("staycont", 0);
                    stinfo.put("staytime", nowtime);
                    if (havemin == staytime && ismould) {
                        stinfo.put("ismould", false);//换单后，完成了时间，刚好进入正式生产过程，需要加入生产的产品数量
                        stinfo.put("staycont", linksh.getPlanNum());//剩余数量
                    }
                    ordreachInsertT(ordreach);
                    ordreach.setPlanCount(0);//新增后跳新的小时数量
                    ordreach.setPlanNum("");
                    hasHours.put(hour, hour);
                }
            }
        }
        return stinfo;
    }

    /***
     * 根据剩余数量，剩余数量有一种情况，
     * 1、上一小时进行生产后，剩余数量信息，再接着生产，若能在一个小时生产完成就需要进行换单，并且进入剩余时间状态，剩余数量清零；
     * 2、上一小时进行生产后，剩余数量，再接着生产，不能在一个小时内生产完成就需要进行下一个小时进行生产，并且还需要剩余数量信息，剩余时间清零；
     * @param stinfo
     * @param hour
     * @param ordreach
     * @param havemin
     */
    private Map<String, Object> setOrdreachByStaycount(Map<String, Object> stinfo, Integer hour, StatisOrdreach ordreach, Integer havemin, Map<Integer, Integer> hasHours) {
        Integer staycont = (Integer) stinfo.get("staycont");
        //换单以后，新的订单sdId
        WorkbatchOrdlinkShiftVO linksh = (WorkbatchOrdlinkShiftVO) stinfo.get("wklinksh");
        int speed = (linksh != null && linksh.getSpeed() != null && linksh.getSpeed() > 0) ? linksh.getSpeed() : 3500;//如果标准时速为空就默认3000每小时
        ordreach.setPdName(linksh.getCmName());
        ordreach.setWbNo(linksh.getWbNo());
        //判断对象不为空，并且速度必须大于零
        int nowtime = (int) Math.round(((double) staycont * 60) / speed);
        //该小时生产剩余的数量信息，还可以换单生产，然后设置剩余时间长度
        if (havemin > nowtime) {
            int staytime = havemin - nowtime;
            ordreach.setPlanCount(staycont);
            ordreach.setPlanNum(staycont + "");
            ordreach.setTargetHour(hour);
            stinfo.put("isbreak", true);
            stinfo.put("staycont", 0);
            stinfo.put("staytime", staytime);//剩余的时间后进行换单
            stinfo.put("oldwklinksh", linksh);//不能插入hour数据列表中
        } else {
            //设定生产数量信息
            if (nowtime >= havemin) {
                int staytime = nowtime - havemin;
                //减去换型时间后继续生产，并且把剩余数量重置清楚
                Integer productnum = (int) Math.round((double) (speed * havemin) / 60);
                staycont = staycont - (speed * havemin) / 60;
                ordreach.setPlanCount(productnum);
                ordreach.setPlanNum(productnum + "");
                stinfo.put("staycont", staycont);
                stinfo.put("staytime", 0);
                ordreach.setTargetHour(hour);
                ordreachInsertT(ordreach);
                //当如果相等表示需要换单，那就调整换单后再继续
                if (nowtime == havemin)
                    stinfo.put("isbreak", true);
                else
                    stinfo.put("isbreak", false);
                ordreach.setPlanCount(0);//新增后跳新的小时数量
                ordreach.setPlanNum("");
                hasHours.put(hour, hour);
            }
        }
        return stinfo;
    }

    /*****
     * 当碰到不剩余数量不剩余时间的时候就执行刚刚整点开始方法
     * @param stinfo
     * @param hour
     * @param ordreach
     * @param havemin
     * @param hasHours
     * @return
     */
    private Map<String, Object> setJustStart(Map<String, Object> stinfo, Integer hour, StatisOrdreach ordreach, Integer havemin, Map<Integer, Integer> hasHours) {
        return setOrdreachByOnce(stinfo, hour, ordreach, havemin, hasHours);
    }

    /****
     * 已经进行计划保存后，返回对应小时数的补零操作
     * @param targetDay
     * @param mainfo
     * @param workbatchShiftset
     * @param newday
     */
    private void setFullzero(String targetDay, MachineMainfo mainfo, WorkbatchShiftset workbatchShiftset, Map<Integer, Integer> realctLs, Date newday, Map<Integer, Integer> addHours) {
        Integer wsId = workbatchShiftset.getId();
        Map<Integer, Integer> hours = setClasshours(targetDay, workbatchShiftset);
//        int hoursize = (hours != null && hours.size() > 0) ? hours.size() : 0;
//        Integer allCount = statisOrdreachMapper.statisOrdInitListCount(targetDay, wsId, mainfo.getId());
//        allCount = (allCount != null && allCount > 0) ? allCount : 0;
        if (hours != null && addHours != null && hours.size() == addHours.size())
            return;//数据相等表示无需补零操作
        //获取数据库中对应班次的小时达成率的数据信息
        //List<StatisOrdreach> targetHourList = statisOrdreachMapper.statisOrdInitList(targetDay, wsId, mainfo.getId());
//        Map<Integer, Integer> hasHours = new HashMap<>();
        StatisOrdreach ordreach = new StatisOrdreach();
        ordreach.setTargetDay(targetDay);
        ordreach.setMaId(mainfo.getId());
        ordreach.setMaName(mainfo.getName());
        ordreach.setPlanCount(0);
        ordreach.setWbNo("无");
        ordreach.setPdName("无");
        ordreach.setPdName("无");
        ordreach.setWsId(wsId);
        ordreach.setWsName(workbatchShiftset.getCkName());
        ordreach.setCreateAt(newday);
        ordreach.setUpdateAt(newday);
//        //匹配，提取出已存在的小时数
//        if (targetHourList != null && !targetHourList.isEmpty()) {
//            for (Map.Entry<Integer, Integer> entry : hours.entrySet()) {
//                Integer hour = entry.getKey();
//                for (StatisOrdreach osreach : targetHourList) {
//                    Integer tghour = osreach.getTargetHour();
//                    if (hour.equals(tghour)) {
//                        hashours.put(tghour, tghour);
//                        break;
//                    }
//                }
//            }
//        }
        //对于不存在的小时数进行补零操作
        if (hours != null && !hours.isEmpty()) {
            for (Map.Entry<Integer, Integer> entry : hours.entrySet()) {
                Integer isAddhour = addHours.get(entry.getValue());
                if (entry.getValue() == isAddhour) {
                    continue;
                } else {
                    ordreach.setTargetHour(entry.getValue());
                    if (realctLs != null && !realctLs.isEmpty()) {
                        ordreach.setRealCount(realctLs.get(entry.getValue()));
                    }
                    ordreachInsertT(ordreach);
                }
            }
        }
    }

    /***
     * 设置每个班次开始时间和班次时间
     * @param targetDay
     * @param workbatchShiftset
     * @return
     */
    private Map<Integer, Integer> setClasshours(String targetDay, WorkbatchShiftset workbatchShiftset) {
        Map<Integer, Integer> classHours = new LinkedHashMap<>(); //班次的总小时数列表信息
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, targetDay);//判断大小后赋值给开始时间和结束时间
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        //判断是否大于结束时间
        while (classStartTime.compareTo(classEndTime) < 0) {
            classHours.put(classStartTime.getHours(), classStartTime.getHours());
            //每循环一次进行1小时增加
            classStartTime = DateUtil.addHourForDate(classStartTime, 1);
        }
        return classHours;
    }

    /****
     * 获取真实的统计分析数据
     *
     * @param classStartTime
     * @param classEndTime
     * @param maId
     * @return
     */
    @Override
    public Map<Integer, Integer> getRealcoutByHour(Date classStartTime, Date classEndTime, Integer maId) {
        //根据班次开始时间和结束时间以及设备查询出每小时的真实数据
        List<Map<String, Object>> realctLs = superviseRegularMapper.findByMaidDay(DateUtil.refNowTimes(classStartTime), DateUtil.refNowTimes(classEndTime), maId);
        Map<Integer, Integer> realct = null;//如果没有查到数据就返回空值信息
        if (realctLs != null && !realctLs.isEmpty()) {
            realct = new HashMap<>();
            for (Map<String, Object> realcont : realctLs) {
                Double ptdb = (Double) realcont.get("pcout");
                Integer hour = (Integer) realcont.get("target_hour");
                if (ptdb != null)
                    realct.put(hour, ptdb.intValue());
                else
                    realct.put(hour, 0);
            }
        }
        return realct;
    }

    @Override
    public List<TodayOrdreachVO> todyOrdreach() {
        return statisOrdreachMapper.todyOrdreach();
    }

    @Override
    public IPage<StatisOrdreachVO> gethourRateList(HourRateVO hourRateVO, IPage<StatisOrdreachVO> page) {
        List<StatisOrdreachVO> statisOrdreachVOList = statisOrdreachMapper.gethourRateList(hourRateVO, page);
        for (StatisOrdreachVO statisOrdreachVO : statisOrdreachVOList) {
            Integer targetHour = statisOrdreachVO.getTargetHour();
            int nextHour = targetHour + 1;
            String timeInterval;
            if (targetHour < 10) {
                if (targetHour < 9) {
                    timeInterval = "0" + targetHour + ":00-0" + nextHour + ":00";
                } else {
                    timeInterval = "0" + targetHour + ":00-" + nextHour + ":00";
                }
            } else {
                timeInterval = targetHour + ":00-" + nextHour + ":00";
            }
            statisOrdreachVO.setTimeInterval(timeInterval);
        }
        return page.setRecords(statisOrdreachVOList);
    }


    /***
     * 通过三次吃饭时间筛选每个班次的每个小时可用时间
     * @param startTime
     * @param endTime
     * @param workbatchShiftset
     * @return
     */
    private Map<Integer, Integer> getHoursForUptime(Date startTime, Date endTime, WorkbatchShiftset workbatchShiftset) {
        Map<Integer, Integer> hours = new LinkedHashMap<>();//返回已经处理过的小时数
        Map<Integer, List<Date>> mealTime = new LinkedHashMap<>();//返回班次的小时数量
        String targetDay = DateUtil.refNowDay(startTime);
        if (workbatchShiftset != null) {
            //设置三次吃饭时间
            setTimeByMeal(workbatchShiftset.getMealOnetime(), mealTime, 1, targetDay);
            setTimeByMeal(workbatchShiftset.getMealSecondtime(), mealTime, 2, targetDay);
            setTimeByMeal(workbatchShiftset.getMealThirdtime(), mealTime, 3, targetDay);
        }
        if (mealTime != null && !mealTime.isEmpty()) {
            for (Map.Entry<Integer, List<Date>> entry : mealTime.entrySet()) {
                List<Date> lsTime = entry.getValue();
                hours.putAll(getHoursFormeal(lsTime));
            }
        }
        String stahour = DateUtil.toDatestr(startTime, "HH");
        Integer stahourInt = (stahour != null && stahour.length() > 0) ? Integer.parseInt(stahour) : 0;
        String endhour = DateUtil.toDatestr(endTime, "HH");
        Integer endhourInt = (endhour != null && endhour.length() > 0) ? Integer.parseInt(endhour) : 0;

        Integer day = endhourInt - stahourInt;//开始时间和结束时间相差多少小时数
        //当开始小时大于结束小时数；就需要跨天管理
        day = (day <= 0) ? (24 - stahourInt) + endhourInt : day;
        if (day > 0) {
            for (int i = stahourInt; i < stahourInt + day; i++) {
                Integer currhour = (i > 23) ? i - 24 : i;//当当前小时；
                Integer useMin = hours.get(currhour);
                if (useMin == null) {
                    hours.put(currhour, 60);
                }
            }
        }
        return hours;
    }

    /****
     * 循环的吃饭时间，后计算每小时的可利用分钟数
     * @param lsTime
     * @return
     */
    private Map<Integer, Integer> getHoursFormeal(List<Date> lsTime) {
        Map<Integer, Integer> mealhour = new LinkedHashMap<>();
        Integer useTime = 0;
        if (lsTime != null && lsTime.size() == 2) {
            Date statime = lsTime.get(0);
            Date endtime = lsTime.get(1);
            if (statime == null || endtime == null)
                return null;
            Double mealStayDoub = new Double(DateUtil.calLastedTime(endtime.getTime(), statime) / 60);
            String stahour = DateUtil.toDatestr(statime, "HH");
            Integer stahourInt = (stahour != null && stahour.length() > 0) ? Integer.parseInt(stahour) : 0;
            String endhour = DateUtil.toDatestr(endtime, "HH");
            Integer endhourInt = (endhour != null && endhour.length() > 0) ? Integer.parseInt(endhour) : 0;
            if (stahourInt == endhourInt) {
                //同一个小时内
                useTime += 60 - mealStayDoub.intValue(); //整点到开始时间到结束时间需要多少分钟
                mealhour.put(stahourInt, useTime);
                return mealhour;
            } else {
                int manyhour = endhourInt - stahourInt;
                //不同一个小时的
                if (manyhour <= 1) {
                    //吃饭时间小于等120分钟时间
                    Date nextHour = DateUtil.addHourForDate(statime, 1);
                    Date starPointZero = DateUtil.toDate(DateUtil.toDatestr(nextHour, "yyyy-MM-dd HH") + ":00:00", null);
                    //开始小时数，得到吃饭时间，并且返回小时可利用时间
                    Double mealDoub = new Double(DateUtil.calLastedTime(starPointZero.getTime(), statime) / 60);
                    mealDoub = (mealDoub != null) ? mealDoub : 0;
                    useTime = 60 - mealDoub.intValue();
                    mealhour.put(stahourInt, useTime);//开始那个小时的可用时间
                    //结束小时的吃饭时间
                    Date endZero = DateUtil.toDate(DateUtil.toDatestr(endtime, "yyyy-MM-dd HH") + ":00:00", null);
                    mealDoub = new Double(DateUtil.calLastedTime(endtime.getTime(), endZero) / 60);
                    useTime = 60 - mealDoub.intValue(); //结束小时可以利用时间
                    mealhour.put(endhourInt, useTime);//结束小时的可用时间
                } else {
                    //吃饭时间大于120分钟时间
                    Date nextHour = DateUtil.addHourForDate(statime, 1);
                    Date starPointZero = DateUtil.toDate(DateUtil.toDatestr(nextHour, "yyyy-MM-dd HH") + ":00:00", null);
                    //开始小时数，得到吃饭时间，并且返回小时可利用时间
                    Double mealDoub = new Double(DateUtil.calLastedTime(statime.getTime(), starPointZero) / 60);
                    mealDoub = (mealDoub != null) ? mealDoub : 0;
                    useTime = 60 - mealDoub.intValue();

                    mealhour.put(stahourInt, useTime);//开始那个小时的可用时间
                    for (int i = 1; i < manyhour; i++) {
                        mealhour.put(stahourInt + i, 0);//开始那个小时的可用时间
                    }
                    //结束小时的吃饭时间
                    Date endZero = DateUtil.toDate(DateUtil.toDatestr(endtime, "yyyy-MM-dd HH") + ":00:00", null);
                    mealDoub = new Double(DateUtil.calLastedTime(endZero.getTime(), endtime) / 60);
                    useTime = 60 - mealDoub.intValue(); //结束小时可以利用时间
                    mealhour.put(endhourInt, useTime);//结束小时的可用时间
                }
            }
        }
        return mealhour;
    }

    /****
     * 设置每小时的分钟数使用，就是减去吃饭的时间，每小时剩下的分钟数量【停用作废方法】
     * @param startTime
     * @param endTime
     * @param workbatchShiftset
     * @return
     * @deprecated
     */
    private List<Integer> getHoursByClass(Date startTime, Date endTime, WorkbatchShiftset workbatchShiftset) {
        long diff = endTime.getTime() - startTime.getTime();
        long day = diff / (60 * 60 * 1000);
        List<Integer> hours = new ArrayList<>();//返回已经处理过的小时数
        Map<Integer, List<Date>> mealTime = new LinkedHashMap<>();//返回班次的小时数量
        String targetDay = DateUtil.refNowDay(startTime);
        if (workbatchShiftset != null) {
            //设置三次吃饭时间
            setTimeByMeal(workbatchShiftset.getMealOnetime(), mealTime, 1, targetDay);
            setTimeByMeal(workbatchShiftset.getMealSecondtime(), mealTime, 2, targetDay);
            setTimeByMeal(workbatchShiftset.getMealThirdtime(), mealTime, 3, targetDay);
        }
        //循环每小时，生成每小时，以及每小时的有效分钟数时间；除去两次吃饭时间
        if (day > 0) {//循环多少次数，就是小时数量多少，一般一个班次12个小时
            Date endnxtime = new Date();
            Integer mapHour = startTime.getHours();
            for (int i = 0; i < day; i++) {
                int hour = (i == 0) ? startTime.getHours() : endnxtime.getHours();
                Date newstarttime = (i == 0) ? startTime : endnxtime;
                int nexhour = (mapHour == 23) ? 0 : hour + 1;
                String endnxtimestr = DateUtil.format(startTime, "yyyy-MM-dd") + " " + nexhour + ":00:00";
                //加了一小时的结束时间
                endnxtime = DateUtil.toDate(endnxtimestr, null);
                endnxtime = (mapHour >= 23) ? DateUtil.addDayForDate(endnxtime, 1) : endnxtime;
                long staytimeMin = (endnxtime.getTime() - newstarttime.getTime()) / 1000 / 60;  //每个小时分钟数
                long pointMin = 0;//表示中间点位间隔时间信息，如果为0表示没有扣除的时间。
                long sttphour, entphour = 0;//开始小时；结束小时
                for (Map.Entry<Integer, List<Date>> entry : mealTime.entrySet()) {
                    List<Date> ls = entry.getValue();
                    Date startopint = (mapHour > 24) ? DateUtil.addDayForDate(ls.get(0), 1) : ls.get(0);
                    Date endopint = (mapHour > 24) ? DateUtil.addDayForDate(ls.get(1), 1) : ls.get(1);
                    sttphour = (endnxtime.compareTo(startopint) == 0) ? 0 : getMin(hour, newstarttime, endnxtime, startopint, false);//判断间隔的开始时间
                    entphour = (newstarttime.compareTo(endopint) == 0) ? 0 : getMin(hour, newstarttime, endnxtime, endopint, true);//判断间隔的结束时间
                    if (sttphour == -2 && entphour == -2) {
                        pointMin = -2;
                    } else {
                        if (sttphour == -2 && entphour == -1) {
                            pointMin = (endnxtime.getTime() - endopint.getTime()) / 1000 / 60;  //每个小时分钟数
                        } else if (entphour == -2 && (sttphour == -1)) {
                            pointMin = (newstarttime.getTime() - startopint.getTime()) / 1000 / 60;  //每个小时分钟数
                        } else {
                            if (sttphour < 0)
                                pointMin += entphour;
                            else if (entphour < 0)
                                pointMin += sttphour;
                            else
                                pointMin += sttphour + entphour;
                        }
                    }
                    //开始小时数等于开始点的时间，开始点的小时数不等于结束点的小时数
                    if (startopint.compareTo(newstarttime) == 0 && startopint.getHours() != endopint.getTime()) {
                        pointMin = -1;
                    }
                }
                //判断中间的吃饭时间不为空，就用开始和结束的时间
                staytimeMin = (pointMin > 0) ? pointMin : staytimeMin;
                staytimeMin = (pointMin < 0) ? 0 : staytimeMin; //关键点落在了开始和结束的点上，就返回-1；那就设置为0，表示没有可用的分钟数
                hours.add((int) staytimeMin);
                mapHour++;
            }
        }
        return hours;
    }


    /****
     * 通过班次设置表中的吃饭时间给Map对象
     * @param mealtimestr
     * @param mealTimeMap
     * @param num
     */
    private void setTimeByMeal(String mealtimestr, Map<Integer, List<Date>> mealTimeMap, Integer num, String targetDay) {
        List<Date> ls = new ArrayList<>();
        //判断字符串没超过10，判断字符串大于2即可
        if (mealtimestr != null && mealtimestr.length() > 2) {
            Date startDate = DateUtil.toDate((mealtimestr.split("~")[0]).trim() + ":00", "HH:mm:ss");
            Date endDate = DateUtil.toDate((mealtimestr.split("~")[1]).trim() + ":00", "HH:mm:ss");
            //开始时间大于结束时间，表示跨天了，需要累加1天时间
            if (startDate.compareTo(endDate) > 0) {
                Date classStartDate = DateUtil.toDate(targetDay + " " + mealtimestr.split("~")[0] + ":00", "yyyy-MM-dd HH:mm:ss");
                Date nxtomorr = DateUtil.toDate(targetDay, "yyyy-MM-dd");
                nxtomorr = DateUtil.addDayForDate(nxtomorr, 1);
                Date classEndDate = DateUtil.toDate(DateUtil.toDatestr(nxtomorr, "yyyy-MM-dd") + " " + mealtimestr.split("~")[1] + ":00", "yyyy-MM-dd HH:mm:ss");
                ls.add(classStartDate);
                ls.add(classEndDate);
            } else {
                Date classStartDate = DateUtil.toDate(targetDay + " " + mealtimestr.split("~")[0] + ":00", "yyyy-MM-dd HH:mm:ss");
                Date classEndDate = DateUtil.toDate(targetDay + " " + mealtimestr.split("~")[1] + ":00", "yyyy-MM-dd HH:mm:ss");
                ls.add(classStartDate);
                ls.add(classEndDate);
            }
        }
        if (ls != null && ls.size() > 0)
            mealTimeMap.put(num, ls);
    }

    /****
     * 判断opint中间时间点是否在开始和结束时间之间，如果在之间，如果不是结束，就计算开始时间到opint的时间，如果是结束，就计算opint到结束时间的分钟数
     * 计算每小时有多少空余时间可用
     * @param starttime
     * @param endtime
     * @param opint
     * @param isend
     * @return
     */
    public long getMin(int hour, Date starttime, Date endtime, Date opint, boolean isend) {
        if (hour == starttime.getHours() || hour == endtime.getHours()) {
            //需要判断的时间
            Calendar date = Calendar.getInstance();
            date.setTime(opint);
            //开始时间
            Calendar begin = Calendar.getInstance();
            begin.setTime(starttime);
            //结束时间
            Calendar end = Calendar.getInstance();
            end.setTime(endtime);
            boolean flag = (date.after(begin) && date.before(end)) || (date.compareTo(begin) == 0 || date.compareTo(end) == 0);
            long staytimeMin = (endtime.getTime() - starttime.getTime()) / 1000 / 60;  //每个小时分钟数
            //判断是否在开始和结束之间，不在之间，就直接返回开始和结束时间的分钟数
            if (flag) {
                //是否是结束的时间点 true
                if (isend) {
                    if (date.after(begin) && date.before(end)) {
                        //是结束的点的时间点；计算后半段时间
                        staytimeMin = (endtime.getTime() - opint.getTime()) / 1000 / 60;  //每个小时分钟数
                    } else {
                        //碰到落结束点
                        if (date.compareTo(end) == 0) {
                            staytimeMin = -2;
                        } else {
                            staytimeMin = -1;
                        }
                    }
                    return staytimeMin;
                } else {
                    if (date.after(begin) && date.before(end)) {
                        //非结束，开始的时间点；计算前半段时间
                        staytimeMin = (opint.getTime() - starttime.getTime()) / 1000 / 60;  //每个小时分钟数
                    } else {
                        //碰到落开始点
                        if (date.compareTo(begin) == 0) {
                            staytimeMin = -2;
                        } else {
                            staytimeMin = -1;
                        }
                    }
                    return staytimeMin;
                }
            }
            return 0;
        }
        return 0;
    }

    /****
     * 设定兴艺的模式修改内容信息
     * 无需定时进行计划达成率算法，在排产进行计算统计即可
     * @param targetDay
     * @param wsId
     * @param maId
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    @Async
    public void setOrdreach(String targetDay, Integer wsId, Integer maId, String tenantId) {
        DBIdentifier.setProjectCode(tenantId);
        setOrdreachBytargetDay(targetDay, wsId, maId);
    }

    /****
     * 插入数据进行达成率的统计分析
     * @param ordreach
     * @return
     */
    @Override
    public int ordreachInsertT(StatisOrdreach ordreach) {
//        Integer hour = ordreach.getTargetHour();
//
//        if (ordreach.getWsId() != null) {
//            WorkbatchShiftset wshiftset = workbatchShiftsetMapper.selectById(ordreach.getWsId());
//            if (wshiftset.getStartTime().getTime() > wshiftset.getEndTime().getTime()) {
//                //判断开始时间打印结束时间，就表示晚班
//                if (hour <= 12) {
//                    String toDate = ordreach.getTargetDay();
//                    toDate = (toDate != null && toDate.length() > 0) ? toDate : null;
//                    ordreach.setTargetDay(DateUtil.format(DateUtil.addDayForDate(DateUtil.toDate(toDate, "yyyy-MM-dd"), 1), "yyyy-MM-dd"));
//                }
//            }
//        }


        //插入数据时刻进行达成率统计算法，如果没有该数据及设置null即可
        Integer planCount = (ordreach.getPlanCount() != null) ? ordreach.getPlanCount() : 0;
        Integer realCount = (ordreach.getRealCount() != null) ? ordreach.getRealCount() : 0;
        if (planCount == null || planCount <= 0) {
            ordreach.setReachRate(null);
        } else {
            //设定小时达成率
//            BigDecimal realrate = new BigDecimal(realCount / (double) planCount);
            if (realCount > 0) {
                BigDecimal realrate = BigDecimal.valueOf((double) realCount / (double) planCount).setScale(4, BigDecimal.ROUND_HALF_UP);
                ordreach.setReachRate(realrate);
            }
        }
        // 判断是否已经存在，如果存在就不保存
        StatisOrdreach ordrachDB = getOrdreachByOne(ordreach);
        if (ordrachDB != null) {
            //按照时间排产已经写入的数据信息。
            //判断存入数据库和取出来数据库信息误差有毫秒级别会导致判断时间不同，所以处理成相差不到1秒即为相同时间
            double difftime = DateUtil.calLastedTime(ordreach.getUpdateAt().getTime(), ordrachDB.getUpdateAt());
            if (difftime <= 1 && ordrachDB.getUpdateAt() != null) {
                log.info("已经更新该小时信息 ，将无需更新该小时数据信息" + ordreach.getTargetHour());
                return 0;//如果更新时间一致就无需更新
            } else {
                return statisOrdreachMapper.updateByStatisOrdreach(ordreach);
            }
        } else {
            return statisOrdreachMapper.insert(ordreach);
        }

    }

    /****
     * 清除对应设备的日期的班次小时达成率的内容信息。12个小时数据信息内容
     * @param targetDay
     * @param wsId
     * @param maId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public int clearOrdreach(String targetDay, Integer wsId, Integer maId) {
        return statisOrdreachMapper.clearOrdreach(targetDay, wsId, maId);
    }

    /****
     * 返回是否被锁定状态
     * @param targetDay
     * @param wsId
     * @param maId
     * @return
     */
    @Override
    public Integer getIslockReach(String targetDay, Integer wsId, Integer maId) {
        return statisOrdreachMapper.getIslockReach(targetDay, wsId, maId);
    }

    /*****
     *
     * @param ordreach
     * @return
     */
    private StatisOrdreach getOrdreachByOne(StatisOrdreach ordreach) {
        String targetDay = ordreach.getTargetDay();
        Integer wsId = ordreach.getWsId();
        Integer maId = ordreach.getMaId();
        Integer hour = ordreach.getTargetHour();
        return statisOrdreachMapper.getOrdreachBySingle(targetDay, wsId, maId, hour);
    }


    /***
     * 执行小时达成率算法
     * @param targetDay
     * @param wsId
     * @param maId
     */
    public void setOrdreachNewCaset(String targetDay, Integer wsId, Integer maId) {
        Date newday = new Date();
        Integer islock = getIslockReach(targetDay, wsId, maId);//查询锁定状态为1表示锁定，不能够修改
        if (islock != null && islock == 1) {
            return;
        }
//        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectById(wsId);
        WorkbatchShiftset workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        Date classStartTime = workbatchShiftset.getStartTime();
        Date classEndTime = workbatchShiftset.getEndTime();
        Date[] wsDatetime = setWsDatetime(classStartTime, classEndTime, targetDay);
        classStartTime = wsDatetime[0];
        classEndTime = wsDatetime[1];
        //
        Map<Integer, Integer> hourList = getHoursForUptime(classStartTime, classEndTime, workbatchShiftset);
        List<WorkbatchOrdlinkShiftVO> worklinkshiftLs = workbatchOrdlinkMapper.getListByshift(targetDay, wsId, maId);
        int stayTime = 0;

        Map<Integer, StatisOrdreachShiftVO> pointMap = new HashMap<>();
        pointMap.putAll(setMealAll(workbatchShiftset));//新增吃饭的点对象
        pointMap.putAll(setWorkAll(worklinkshiftLs, classStartTime, classEndTime));


        Integer partStatus = 0;//时间间隔状态 1、生产准备时间2、正式生产3、休息吃饭
        PointType partlastPoint = null;
        Map<Integer, List<PartType>> putPart = new LinkedHashMap<>();
        //循环每个时间段的点位信息，并且换成时间段并且计算累加每个时间端的生产数量
        for (Map.Entry<Integer, Integer> entry : hourList.entrySet()) {
            Integer currhour = entry.getKey();
            StatisOrdreachShiftVO shiftVO = pointMap.get(currhour);
            //当前小时的点位信息，如果没有点位表示延续后面的
            if (shiftVO != null) {
                List<PointType> points = new ArrayList<>();
                //把点位统一追加一个对象里面，然后进行排序
                if (shiftVO.getRestPoints() != null)
                    points.addAll(shiftVO.getRestPoints());
                if (shiftVO.getWorkPoints() != null)
                    points.addAll(shiftVO.getWorkPoints());
                //把点位按照时间顺序排列出来，然后进行端的循环
                points = points.stream().sorted((u1, u2) -> u2.getPoint().compareTo(u1.getPoint())).collect(Collectors.toList());

                //生成时间段信息
                PartType paty = new PartType();//端的状态


            } else {
                //状态为2表示生产状态
                if (partStatus == 2) {
                    PartType partone = new PartType();
                    partone.setHour(currhour);
                    partone.setStartmin(0);
                    partone.setEndmin(60);
                    partone.setWfId(partlastPoint.getPoint());
                    List<PartType> part = new ArrayList<>();
                    part.add(partone);
                    putPart.put(currhour, part);
                }
            }
        }
    }

    /*****
     * 循环点位，验证这个小时的所有点位，每个点位以及和零点和60点分段的状态信息
     * @param points
     * @param currhour
     * @param putPart
     */
    private void verifyPart(List<PointType> points, Integer currhour, Map<Integer, List<PartType>> putPart) {
        List<PartType> nkPart = putPart.get(currhour);

        PointType zeropt = new PointType();
        zeropt.setPoint(0);
        zeropt.setHour(currhour);
        zeropt.setType(7);//表示为整点的开始 7，表示为0分开始


        PointType endpoint = new PointType();
        endpoint.setPoint(60);
        endpoint.setHour(currhour);
        endpoint.setType(8);//表示小时的结束 8 表示为60分结束

        PointType lastpt = null;
        for (int i = 0; i < points.size(); i++) {
            lastpt = points.get(i);
            if (i == 0) {
                //两个点进行比较并且返回段落信息

            }
        }
    }

    private List<PartType> putParty(List<PartType> moreParty, PointType onePt, PointType endPt, Integer laststatus) {
        moreParty = (moreParty != null) ? moreParty : new ArrayList<>();
        PartType newPty = new PartType();
        //第一个点位onePt
        if (onePt.getType() != null) {
            if (onePt.getType() == 1) {

            }

            if (onePt.getType() == 2) {

            }
            if (onePt.getType() == 3) {

            }
            if (onePt.getType() == 4) {

            }
            if (onePt.getType() == 5) {

            }
            if (onePt.getType() == 6) {

            }
            if (onePt.getType() == 7) {
                //从小时0分开始
                if (endPt != null && endPt.getType() != null) {
                    //返回接收到换型状态信息
                    if (endPt.getType() == 2) {
                        newPty.setWfId(endPt.getWfId());
                        newPty.setHour(endPt.getHour());
                        newPty.setType(1);//如果是2表示为换型状态
                        newPty.setStartmin(onePt.getPoint());
                        newPty.setEndmin(endPt.getPoint());
                        newPty.setDiffmin(onePt.getPoint() - endPt.getPoint());
                        moreParty.add(newPty);
                        return moreParty;
                    }
                    //返回接收到换型状态信息
                    if (endPt.getType() == 2) {
                        newPty.setWfId(endPt.getWfId());
                        newPty.setHour(endPt.getHour());
                        newPty.setType(1);//如果是2表示为换型状态
                        newPty.setStartmin(onePt.getPoint());
                        newPty.setEndmin(endPt.getPoint());
                        newPty.setDiffmin(onePt.getPoint() - endPt.getPoint());
                        moreParty.add(newPty);
                        return moreParty;
                    }

                }
            }
            if (onePt.getType() == 6) {

            }
        }
        return null;
    }


    /****
     * 设置工作时间点位信息
     * @param worklinkshiftLs
     * @param shiftSrtime
     * @param shiftEdtime
     * @return
     */
    private Map setWorkAll(List<WorkbatchOrdlinkShiftVO> worklinkshiftLs, Date shiftSrtime, Date shiftEdtime) {
        Map<Integer, StatisOrdreachShiftVO> pointMap = new HashMap<>();
        if (!worklinkshiftLs.isEmpty()) {
            Iterator<WorkbatchOrdlinkShiftVO> itera = worklinkshiftLs.iterator();
            Date enddate = shiftSrtime;//最后结束完成时间
            int i = 0;
            PointType lastpoint = null;
            //工单生产完了，需要进行换单执行
            while (itera.hasNext()) {//遍历排产单
                i++;
                List<PointType> worklist = new ArrayList();
                WorkbatchOrdlinkShiftVO wfInfo = itera.next();
                if (i == 1) {
                    PointType startpoint = new PointType();
                    startpoint.setHour(enddate.getHours());
                    startpoint.setPoint(enddate.getMinutes());
                    startpoint.setWfId(wfInfo.getWfId());
                    startpoint.setType(1);//生产准备结束类型
                    setWorkPoint(startpoint, pointMap);
                }

                //计划的总时间长度
                Integer plantime = wfInfo.getPlanTotalTime();//总时间长度
                Integer mouldStay = wfInfo.getMouldStay();//总换型时间长度

                Date moddate = DateUtil.addMinBystarttime(enddate, mouldStay);//换型时间从开始起步
                enddate = DateUtil.addMinBystarttime(enddate, plantime);//最后完成结束的时间
                boolean isshiftend = (shiftEdtime.compareTo(enddate) < 0) ? true : false;//是否已经超过了下班时间

                //准备时间的点
                PointType mopoint = new PointType();
                mopoint.setHour(moddate.getHours());
                mopoint.setPoint(moddate.getMinutes());//换型的时间点
                mopoint.setWfId(wfInfo.getWfId());
                mopoint.setType(2);//生产准备结束类型

                //后生产时间的点
                PointType finishpoint = new PointType();
                Integer pointtp = (isshiftend) ? 4 : 3;//3表示生产开始结束4表示生产全结束
                Integer minpoint = (isshiftend) ? shiftEdtime.getMinutes() : enddate.getMinutes();
                Integer hourpoint = (isshiftend) ? shiftEdtime.getHours() : enddate.getHours();
                finishpoint.setHour(hourpoint);
                finishpoint.setPoint(minpoint);//生产的时间点
                finishpoint.setWfId(wfInfo.getWfId());
                finishpoint.setType(pointtp);//生产开始结束类型


                //当时间不在同一个小时处理换型的点位和生产结束的点位时间
                if (moddate.getHours() == enddate.getHours()) {
                    setWorkPoint(mopoint, pointMap);
                    setWorkPoint(finishpoint, pointMap);
                } else {
                    setWorkPoint(mopoint, pointMap);
                    setWorkPoint(finishpoint, pointMap);
                }
                //当结束时间大于计划落点时间就跳出循环
                if (shiftEdtime.compareTo(enddate) < 0)
                    break;
            }
        }
        return pointMap;
    }

    /***
     * 设置点位中存在吃饭时间的情况。
     * @param point
     * @param pointMap
     * @return
     */
    private Map<Integer, StatisOrdreachShiftVO> setWorkPoint(PointType point, Map<Integer, StatisOrdreachShiftVO> pointMap) {
        Integer currhour = point.getHour();
        //获取当前小时的点对象信息，如果没有就初始化
        StatisOrdreachShiftVO sfvo = (pointMap != null && pointMap.get(currhour) != null) ? pointMap.get(currhour) : new StatisOrdreachShiftVO();

        List<PointType> restpoint = (sfvo.getRestPoints() != null) ? sfvo.getRestPoints() : null;//表示获取对象中的吃饭的点信息
        List<PointType> workpoint = (sfvo.getWorkPoints() != null) ? sfvo.getWorkPoints() : new ArrayList<>();
        //换型的点
        if (point != null && (point.getType() == 2 || point.getType() == 3)) {
            //表示没有吃饭休息点信息
            if (restpoint != null && restpoint.size() > 0) {
                int pnum = restpoint.size();
                //先判断只有一个点的情况；也会有两个点
                if (pnum == 1) {
                    PointType rspoint = restpoint.get(0);
                    //类型是吃饭开始时间
                    if (rspoint != null && rspoint.getType() == 5) {
                        //小于等于即表示在吃饭之前，即不必修改该点位
                        if (point.getPoint() <= rspoint.getPoint()) {
                            StatisOrdreachShiftVO stvoTemp = (pointMap.get(point.getHour()) != null) ? pointMap.get(point.getHour()) : new StatisOrdreachShiftVO();
                            List<PointType> endwkpoint = (stvoTemp.getWorkPoints() != null) ? stvoTemp.getWorkPoints() : new ArrayList<>();
                            endwkpoint.add(point);
                            stvoTemp.setWorkPoints(endwkpoint);
                            pointMap.put(point.getHour(), stvoTemp);//设定换型时间的点位信息；

                        } else {
                            //在吃饭途中点位，需要修改工作换型结束点位时间信息;结束值在吃饭完结束的map对象中操作处理；
                            int pointdiff = point.getPoint() - rspoint.getPoint();
                            //获得吃饭的结束小时和分钟数对象
                            String mealstr = rspoint.getMealtime();
                            String[] mealstrarry = (mealstr != null && mealstr.length() > 0 && mealstr.indexOf("~") > 0) ? mealstr.split("~") : null;

                            Date endrestDate = (mealstrarry != null && mealstrarry.length > 1) ? DateUtil.toDate(mealstrarry[1], "HH:mm") : null;
                            if (endrestDate == null)
                                return pointMap;
                            //得到工作的点位信息
                            Date realworkpt = DateUtil.addMinBystarttime(endrestDate, pointdiff);
                            //设定工作的换型结束的点位信息，并且放置到点位的对应的小时中去
                            PointType newwkpt = point;
                            newwkpt.setHour(realworkpt.getHours());
                            newwkpt.setPoint(realworkpt.getMinutes());
                            StatisOrdreachShiftVO stvoTemp = (pointMap.get(realworkpt.getHours()) != null) ? pointMap.get(realworkpt.getHours()) : new StatisOrdreachShiftVO();
                            List<PointType> endwkpoint = (stvoTemp.getWorkPoints() != null) ? stvoTemp.getWorkPoints() : new ArrayList<>();
                            endwkpoint.add(newwkpt);
                            stvoTemp.setWorkPoints(endwkpoint);
                            pointMap.put(realworkpt.getHours(), stvoTemp);//设定换型时间的点位信息；
                        }
                    }
                    //吃饭结束时间
                    if (rspoint.getType() == 6) {
                        //当点落在吃饭结束点的之前
                        if (point.getPoint() <= rspoint.getPoint()) {
                            //在吃饭途中点位，需要修改工作换型结束点位时间信息;结束值在吃饭完结束的map对象中操作处理；
                            int pointdiff = rspoint.getPoint() - point.getPoint();
                            int movePart = getMealMin(rspoint.getMealtime()) - pointdiff;//在吃饭间隔时间减去间隔的时间，就是需要推移的时间信息。
                            if (rspoint.getMealtime() != null && rspoint.getMealtime().indexOf("~") > 0) {
                                //吃饭的结束时间点
                                Date endmealtime = DateUtil.toDate(rspoint.getMealtime().split("~")[1], "HH:mm");
                                //移动到的点位差值
                                if (movePart > 0) {
                                    Date movePartTime = DateUtil.addMinBystarttime(endmealtime, movePart);
                                    PointType newwkpt = point;
                                    newwkpt.setHour(movePartTime.getHours());
                                    newwkpt.setPoint(movePartTime.getMinutes());
                                    StatisOrdreachShiftVO stvoTemp = (pointMap.get(movePartTime.getHours()) != null) ? pointMap.get(movePartTime.getHours()) : new StatisOrdreachShiftVO();
                                    List<PointType> endwkpoint = (stvoTemp.getWorkPoints() != null) ? stvoTemp.getWorkPoints() : new ArrayList<>();
                                    endwkpoint.add(newwkpt);
                                    stvoTemp.setWorkPoints(endwkpoint);
                                    pointMap.put(movePartTime.getHours(), stvoTemp);//设定换型时间的点位信息；
                                }
                            }
                        } else {
                            //在吃饭结束点位之后，换型的点位时间
                            //吃饭的结束时间点
                            Date endmealtime = DateUtil.toDate(":" + point.getPoint(), "HH:mm");
                            Date moveTime = DateUtil.addMinBystarttime(endmealtime, getMealMin(rspoint.getMealtime()));

                            PointType newwkpt = point;
                            newwkpt.setHour(moveTime.getHours());
                            newwkpt.setPoint(moveTime.getMinutes());
                            StatisOrdreachShiftVO stvoTemp = (pointMap.get(moveTime.getHours()) != null) ? pointMap.get(moveTime.getHours()) : new StatisOrdreachShiftVO();
                            List<PointType> endwkpoint = (stvoTemp.getWorkPoints() != null) ? stvoTemp.getWorkPoints() : new ArrayList<>();
                            endwkpoint.add(newwkpt);
                            stvoTemp.setWorkPoints(endwkpoint);
                            pointMap.put(moveTime.getHours(), stvoTemp);//设定换型时间的点位信息；
                        }
                    }

                }
            } else {
                //没有吃饭的点就直接put到map中
                workpoint.add(point);
                sfvo.setWorkPoints(workpoint);
                pointMap.put(currhour, sfvo);//设定换型时间的点位信息；
            }
        }
        return pointMap;
    }


    /****
     * 根据吃饭的间隔字符得到中间的休息的分钟数
     * @param mealstr
     * @return
     */
    private Integer getMealMin(String mealstr) {
        String[] mealstrarry = (mealstr != null && mealstr.length() > 0 && mealstr.indexOf("~") > 0) ? mealstr.split("~") : null;
        if (mealstrarry == null)
            return 0;
        Date startm = DateUtil.toDate(mealstrarry[0], "HH:mm");
        Date endtm = DateUtil.toDate(mealstrarry[1], "HH:mm");
        //判断如果结束时间小于开始时间就说明跨天了，需要加一个日期
        if (endtm.compareTo(startm) < 0) {
            endtm = DateUtil.addDayForDate(endtm, 1);
        }
        int mealtime = new Double((DateUtil.calLastedTime(endtm.getTime(), startm)) / 60).intValue();//分钟数

        return mealtime;
    }


    /*****
     * 返回全部吃饭的三次时间的落点的POINT
     * @param workbatchShiftset
     * @return
     */
    private Map setMealAll(WorkbatchShiftset workbatchShiftset) {
        Map pointMap = new HashMap<Integer, StatisOrdreachShiftVO>();
        pointMap.putAll(setMealPoint(workbatchShiftset.getMealOnetime()));
        pointMap.putAll(setMealPoint(workbatchShiftset.getMealSecondtime()));
        pointMap.putAll(setMealPoint(workbatchShiftset.getMealThirdtime()));
        return pointMap;
    }

    /****
     * 返回对应的吃饭的时间点的map对象
     * @param mealtimestr
     * @return
     */
    private Map setMealPoint(String mealtimestr) {
        Map pointMap = new HashMap<Integer, StatisOrdreachShiftVO>();
        StatisOrdreachShiftVO shiftVO = new StatisOrdreachShiftVO();
        List<PointType> restlist = new ArrayList<>();
        if (mealtimestr != null && mealtimestr.length() > 2 && mealtimestr.indexOf("~") > 0) {
            Date startDate = DateUtil.toDate((mealtimestr.split("~")[0]).trim() + ":00", "HH:mm:ss");
            Date endDate = DateUtil.toDate((mealtimestr.split("~")[1]).trim() + ":00", "HH:mm:ss");
            PointType startPoint = new PointType();
            startPoint.setHour(startDate.getHours());
            startPoint.setPoint(startDate.getMinutes());
            startPoint.setMealtime(mealtimestr);
            startPoint.setType(5);//吃饭开始时间


            PointType endPoint = new PointType();
            endPoint.setHour(startDate.getHours());
            endPoint.setPoint(startDate.getMinutes());
            endPoint.setMealtime(mealtimestr);
            endPoint.setType(6);//吃饭结束时间

            //开始时间和结束时间在相同的小时内
            if (startDate.getHours() == endDate.getHours()) {
                restlist.add(startPoint);
                restlist.add(endPoint);
                shiftVO.setRestPoints(restlist);
                pointMap.put(startDate.getHours(), shiftVO);
            } else {
                restlist.add(startPoint);
                shiftVO.setRestPoints(restlist);
                pointMap.put(startDate.getHours(), shiftVO);

                restlist = new ArrayList<>();
                restlist.add(endPoint);
                shiftVO.setRestPoints(restlist);
                pointMap.put(endDate.getHours(), shiftVO);
            }

        }
        return pointMap;
    }

}
