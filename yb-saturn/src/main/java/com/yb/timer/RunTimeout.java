package com.yb.timer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.statis.entity.StatisOrdreach;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchOrdoee;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchOrdoeeMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class RunTimeout implements Runnable {

    public static long timespace = 0;
    static long nd = 1000 * 24 * 60 * 60;
    static long nh = 1000 * 60 * 60;
    WorkbatchShiftsetMapper workbatchShiftsetMapper = (WorkbatchShiftsetMapper) SpringUtil.getBean(WorkbatchShiftsetMapper.class);
    WorkbatchShiftMapper workbatchShiftMapper = (WorkbatchShiftMapper) SpringUtil.getBean(WorkbatchShiftMapper.class);
    WorkbatchOrdlinkMapper workbatchOrdlinkMapper = (WorkbatchOrdlinkMapper) SpringUtil.getBean(WorkbatchOrdlinkMapper.class);
    StatisOrdreachMapper statisOrdreachMapper = (StatisOrdreachMapper) SpringUtil.getBean(StatisOrdreachMapper.class);
    MachineMainfoMapper machineMainfoMapper = (MachineMainfoMapper) SpringUtil.getBean(MachineMainfoMapper.class);
    WorkbatchOrdoeeMapper workbatchOrdoeeMapper = (WorkbatchOrdoeeMapper) SpringUtil.getBean(WorkbatchOrdoeeMapper.class);

    @SneakyThrows
    @Override
    public void run() {
        initialShiftOrdReach();
    }

    public void initialShiftOrdReach() throws ParseException {
        while (true) {
            try {
                Thread.sleep(timespace);
            } catch (InterruptedException e) {
                log.error("error1:" + e.getMessage());
                continue;
            }
            DBIdentifier.setProjectCode("xingyi");
            Date nowDate = new Date();
            long ctime = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatTime = sdf.format(nowDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            List<WorkbatchShiftset> WorkBatchShiftsetList = workbatchShiftsetMapper.getList();
            WorkbatchShiftset workbatchShiftset = new WorkbatchShiftset();
            for (WorkbatchShiftset shiftset : WorkBatchShiftsetList) {
                String staTime = simpleDateFormat.format(shiftset.getStartTime());
                Date startTime = simpleDate.parse(formatTime + (" ") + staTime);
                String eTime = simpleDateFormat.format(shiftset.getEndTime());
                Date endTime = simpleDate.parse(formatTime + (" ") + eTime);

                //跨天
                if (endTime.getTime() < startTime.getTime()) {
                    endTime = DateTimeUtil.getNextDay(endTime);
                }
                System.out.println(simpleDate.format(startTime));
                System.out.println(simpleDate.format(endTime));
                System.out.println(simpleDate.format(nowDate));

                System.out.println(startTime.getTime() <= nowDate.getTime());
                System.out.println(nowDate.getTime() <= endTime.getTime());
                if (startTime.getTime() <= nowDate.getTime() && nowDate.getTime() <= endTime.getTime()) {
                    timespace = endTime.getTime() - ctime;
                    workbatchShiftset = shiftset;
                }
            }
            List<WorkbatchShift> workBatchShifts = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("ws_id", workbatchShiftset.getId()));
            if (workBatchShifts.isEmpty()) {
                log.info("班次信息不存在");
                return;
            }
            List<Integer> workBatchShiftIds = workBatchShifts.stream().map(WorkbatchShift::getId).collect(Collectors.toList());
            List<WorkbatchOrdlink> workBatchOrdlinks = workbatchOrdlinkMapper.selectList(new QueryWrapper<WorkbatchOrdlink>().in("ws_id", workBatchShiftIds).orderByAsc("sd_sort"));
            if (!workBatchOrdlinks.isEmpty()) {
                for (WorkbatchOrdlink workBatchOrdlink : workBatchOrdlinks) {
                    //计算小时时间区间
                    Date startDate = workBatchOrdlink.getStartTime();
                    Date endDate = workBatchOrdlink.getEndTime();
                    long diff = endDate.getTime() - startDate.getTime();
                    // 计算差多少小时
                    Long hour = diff % nd / nh;
                    log.info("排产预计生产小时:[hour:{}]", hour);
                    Integer startHour = startDate.getHours();
                    Integer endHour = endDate.getHours();
                    //获取班次区间
                    if (hour != null) {
                        for (int i = 0; i < hour; i++) {
                            Date date = DateTimeUtil.getNextHoutTime(startDate, i);
                            //获取计划速度
                            WorkbatchOrdoee workbatchOrdoee = workbatchOrdoeeMapper.getOeeBySdId(workBatchOrdlink.getId());
                            //转为每分生产数
                            BigDecimal planSpeed = BigDecimal.valueOf(workbatchOrdoee.getSpeed() / 60).setScale(2);
                            BigDecimal planNum = BigDecimal.ZERO;
                            //换膜时长
                            Integer prepareTime = workbatchOrdoee.getMouldStay() * workbatchOrdoee.getMouldNum();

                            double time = 0;
                            //预计已生产时间/分
                            if (i != 0) {
                                time = DateUtil.calLastedTime(nowDate.getTime(), startDate) / 60;
                            }
                            if (i == hour - 1) {
                                time = DateUtil.calLastedTime(endDate.getTime(), startDate) / 60;
                            }
                            Double planProTime = time - prepareTime;
                            if (planProTime < 0) {
                                planProTime = 0.0;
                            }
                            planNum = BigDecimal.valueOf(planProTime).multiply(planSpeed).setScale(2, BigDecimal.ROUND_HALF_UP);

                            //计算预计生产的达成率
                            StatisOrdreach statisOrdreach = new StatisOrdreach();
                            statisOrdreach.setTargetDay(DateTimeUtil.format(date, DateTimeUtil.DEFAULT_DATE_FORMATTER));
                            statisOrdreach.setTargetHour(date.getHours());
                            statisOrdreach.setTargetMin(date.getMinutes());
                            statisOrdreach.setSdId(workBatchOrdlink.getId().toString());
                            statisOrdreach.setMaId(workBatchOrdlink.getMaId());
                            //获取设备信息
                            MachineMainfo machineMainfo = machineMainfoMapper.selectById(workBatchOrdlink.getMaId());
                            statisOrdreach.setMaName(machineMainfo.getName());
                            statisOrdreach.setPdName(workBatchOrdlink.getPdName());
                            statisOrdreach.setWbNo(workBatchOrdlink.getOdNo());
                            statisOrdreach.setWsName(workbatchShiftset.getCkName());
                            statisOrdreach.setCreateAt(nowDate);
                            statisOrdreach.setPlanNum(planNum.intValue()+"");
                            statisOrdreachMapper.insert(statisOrdreach);
                        }
                    }
                }
            }
            log.info("添加班次实时达成率初始化信息成功===================:");
        }
    }


    /**
     * @param planSpeed   标注速度
     * @param countTime   已生产时间
     * @param prepareTime 换膜时间
     * @return
     */
    public BigDecimal getPlanNum(BigDecimal planSpeed, double countTime, Integer prepareTime) {
        Double planProTime = countTime - prepareTime;
        if (planProTime < 0) {
            planProTime = 0.0;
        }
        BigDecimal planNum = BigDecimal.valueOf(planProTime).multiply(planSpeed).setScale(2, BigDecimal.ROUND_HALF_UP);
        return planNum;
    }


}
