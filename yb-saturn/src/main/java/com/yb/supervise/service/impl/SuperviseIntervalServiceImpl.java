package com.yb.supervise.service.impl;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.statis.mapper.StatisOrdreachMapper;
import com.yb.supervise.entity.SuperviseInterval;
import com.yb.supervise.mapper.SuperviseIntervalMapper;
import com.yb.supervise.request.GetYieldStatisticsRequest;
import com.yb.supervise.request.PerformanceAnalyRequest;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.supervise.vo.*;
import com.yb.timer.DateTimeUtil;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchShiftsetMapper;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

/**
 * 设备清零日志表 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseIntervalServiceImpl extends ServiceImpl<SuperviseIntervalMapper, SuperviseInterval> implements ISuperviseIntervalService {

    @Autowired
    private SuperviseIntervalMapper superviseIntervalMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private StatisOrdreachMapper statisOrdreachMapper;
    @Autowired
    private SuperviseRegularMapper superviseRegularMapper;
    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;


    @Override
    public IPage<SuperviseIntervalVO> selectSuperviseIntervalPage(IPage<SuperviseIntervalVO> page, SuperviseIntervalVO superviseInterval) {
        return page.setRecords(baseMapper.selectSuperviseIntervalPage(page, superviseInterval));
    }

    @Override
    public List<SuperviseIntervalVO> getDayResultByCheckDay(String targetDate, String dpId, String proType) {
        return superviseIntervalMapper.getDayResultByCheckDay(targetDate, dpId, proType);
    }

    @Override
    public List<SuperviseIntervalVO> getHourResultByCheckDay(String targetDate, String dpId, String proType, Integer startTime, Integer endTime) {
        return superviseIntervalMapper.getHourResultByCheckDay(targetDate, dpId, proType, startTime, endTime);
    }

    @Override
    public List<SuperviseIntervalVO> getWeekResultByCheckDay(String targetDate, String dpId, String proType) throws ParseException {
        //获取所选日期的周的开始结束时间
        Date date = DateTimeUtil.format(targetDate, DateTimeUtil.DEFAULT_DATE_FORMATTER);
        String weekStartTime = DateTimeUtil.getWeekStartTime(date);
        String weekEndTime = DateTimeUtil.getWeekEndime(date);
        return superviseIntervalMapper.getWeekResultByCheckDay(weekStartTime, weekEndTime, dpId, proType);
    }

    @Override
    public List<SuperviseIntervalVO> getMonthResultByCheckDay(String targetDate, String dpId, String proType) throws ParseException {
        //获取选定月的开始结束时间
        Date date = DateTimeUtil.format(targetDate, DateTimeUtil.DEFAULT_DATE_FORMATTER);
        Integer year = Integer.valueOf(DateTimeUtil.format(date, DateTimeUtil.YEAR_FORMATTER));
        int month = DateTimeUtil.getLastDayByDesignationMonth(date);
        String beginDayOfMonth = DateTimeUtil.getFirstDayOfMonth(year, month);
        String endDayOfMonth = DateTimeUtil.format(DateTimeUtil.getEndDayByMonth(month, date), DateTimeUtil.DEFAULT_DATE_FORMATTER);

        return superviseIntervalMapper.getWeekResultByCheckDay(beginDayOfMonth, endDayOfMonth, dpId, proType);
    }

    @Override
    public Integer getSumTimeByMaId(Integer status, Integer mtId, Date startTime, Date endTime) {
        return superviseIntervalMapper.getSumTimeByMaId(status, mtId, startTime, endTime);
    }

    @Override
    public List<SuperviseIntervalVO> getCalculateCkNameResult(String targetDate, String dpId, String proType) {
        List<WorkbatchShiftset> workbatchShiftsetList = workbatchShiftsetMapper.selectList(new QueryWrapper<WorkbatchShiftset>()
                .le("start_date", targetDate).ge("end_date", targetDate));
        List<SuperviseIntervalVO> superviseIntervalVOS = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date parse = null;
        try {
            parse = sdf.parse(targetDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!workbatchShiftsetList.isEmpty()) {
            int count = 0;
            List<SuperviseIntervalVO> superviseIntervalVOList = null;
            for (WorkbatchShiftset WorkbatchShiftset : workbatchShiftsetList) {
                Date startTime = WorkbatchShiftset.getStartTime();
                String startTimeformat = targetDate + " " + simpleDateFormat.format(startTime);//开始时间
                Date endTime = WorkbatchShiftset.getEndTime();
                String endTimeFormat;//结束时间
                if (endTime.before(startTime) && count == 0) {
                    parse.setTime(parse.getTime() + (1000 * 60 * 60 * 24));
                    endTimeFormat = sdf.format(parse) + " " + simpleDateFormat.format(endTime);
                    count++;
                } else {
                    endTimeFormat = targetDate + " " + simpleDateFormat.format(endTime);
                }
                superviseIntervalVOList = superviseIntervalMapper.getCalculateCkNameResult(startTimeformat, endTimeFormat, dpId, proType);
                if (!superviseIntervalVOList.isEmpty()) {
                    for (SuperviseIntervalVO superviseIntervalVO : superviseIntervalVOList) {
                        superviseIntervalVO.setCkName(WorkbatchShiftset.getCkName());
                        superviseIntervalVO.setCkId(WorkbatchShiftset.getId());
                        superviseIntervalVOS.add(superviseIntervalVO);
                    }
                }
            }
        }
        return superviseIntervalVOS;
    }

    @Override
    public List<SuperviseIntervalVO> getHourResutPassDay(String targetDate, String tomorrowDay, String dpId, String proType, Integer startHour, Integer endHour) {
        return superviseIntervalMapper.getHourResutPassDay(targetDate, tomorrowDay, dpId, proType, startHour, endHour);
    }

    @Override
    public Integer SumBoxNumber(Integer maId, Date classStartTime, Date classEndTime) {
        Integer sumBoxNumber = superviseIntervalMapper.SumBoxNumber(maId, classStartTime, classEndTime);
        return sumBoxNumber;
    }

    @Override
    public YieldStatisticsVO getYieldStatistics(GetYieldStatisticsRequest request) {
        YieldStatisticsVO yieldStatisticsVO = new YieldStatisticsVO();
        if (StringUtils.isNotBlank(request.getMaType())) {
            request.setGroupBy("a.target_hour,b.id");
        } else {
            request.setGroupBy("a.target_hour,b.ma_type");
        }
        List<YieldStatisticsListVO> vos = statisOrdreachMapper.findByTargetDateAndMaTypeAndWsId(request);
        if (vos.isEmpty()) {
            return yieldStatisticsVO;
        }

        Integer totalRelyNum = vos.stream().mapToInt(YieldStatisticsListVO::getCompletNum).sum();
        Integer totalPlanNUm = vos.stream().mapToInt(YieldStatisticsListVO::getPlanNUm).sum();
        BigDecimal rate = BigDecimal.ZERO;
        if (totalPlanNUm != 0) {
            rate = BigDecimal.valueOf((float) totalRelyNum / (float) totalPlanNUm).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        yieldStatisticsVO.setStatisticsListVOList(vos);
        yieldStatisticsVO.setTotalPlanNum(totalPlanNUm);    
        yieldStatisticsVO.setTotalRelyNum(totalRelyNum);
        yieldStatisticsVO.setTotalRate(rate);

        return yieldStatisticsVO;
    }

    @Override
    public PerformanceAnalyVO getPerformanceAnaly(PerformanceAnalyRequest request) {
        //获取本月开始结束时间
        PerformanceAnalyVO performanceAnalyVO = new PerformanceAnalyVO();
        Date startTime = request.getStartTime();
        startTime.setHours(0);
        startTime.setMinutes(0);
        startTime.setSeconds(0);
        Date endTime = request.getEndTime();
        endTime.setHours(23);
        endTime.setMinutes(59);
        endTime.setSeconds(59);
        List<PerformanceAnalyVOListVO> performanceAnaly = superviseRegularMapper.getPerformanceAnaly(request, startTime, endTime);
        if (!performanceAnaly.isEmpty()) {
            int completNum = performanceAnaly.stream().mapToInt(PerformanceAnalyVOListVO::getCompletNum).sum();
            int wasteNum = performanceAnaly.stream().mapToInt(PerformanceAnalyVOListVO::getWasteNum).sum();
            performanceAnalyVO.setStatisticsNum(completNum + wasteNum);
            performanceAnalyVO.setGoodNum(completNum);
            performanceAnalyVO.setWasteNum(wasteNum);
        }
        performanceAnalyVO.setPerformanceAnalyVOListVOS(performanceAnaly);

        return performanceAnalyVO;
    }

    @Override
    public List<Integer> getLineDataMa(queryInterval queryInterval) {
        //        如果选择了班次
        if (queryInterval.getMaId() != null && queryInterval.getWsId() != null) {
//            查询班次详情
//            WorkbatchShiftset shiftset = workbatchShiftsetMapper.selectById(queryInterval.getWsId());
            WorkbatchShiftset shiftset = workbatchShiftsetMapper.selectByMaid(queryInterval.getWsId(),queryInterval.getMaId());

            SimpleDateFormat ds = new SimpleDateFormat("HH:mm:ss");
            String startTime = ds.format(shiftset.getStartTime());
            String endTime = ds.format(shiftset.getEndTime());
            queryInterval.setStartTime(queryInterval.getStartTime() + " " + startTime);
            queryInterval.setEndTime(queryInterval.getEndTime() + " " + endTime);
        }
        if (queryInterval.getStartTime().equals(queryInterval.getEndTime())) {
            queryInterval.setEndTime(queryInterval.getEndTime() + " 23:59:59");
        }
        List<Integer> maList = superviseRegularMapper.getLineDataMa(queryInterval);
        return maList;
    }

    @Override
    public List<SuperviseIntervalVO> getLineData(queryInterval queryInterval) {
        List<SuperviseIntervalVO> superviseIntervalVOS = superviseRegularMapper.getLineData(queryInterval);
        return superviseIntervalVOS;
    }

    @Override
    public List<Integer> getMaIdsByTargetDay(String targetDay) {
        return superviseIntervalMapper.getMaIdsByTargetDay(targetDay);
    }

    @Override
    public List<Map<String, Integer>> productivityOfToday() {
        String startWorkTime = " " + workbatchShiftsetMapper.getStartWorkTime();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime;
        String endTime;
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDate nowDate = LocalDate.now();
        LocalDateTime rezo = LocalDateTime.parse(nowDate + " 00:00:00", fmt);
        LocalDateTime eight = LocalDateTime.parse(nowDate + startWorkTime, fmt);
        // 如果是晚上12点到早上八点
        if (nowDateTime.isAfter(rezo) && nowDateTime.isBefore(eight)) {
            startTime = nowDate.minusDays(1) + startWorkTime;
            endTime = nowDate + startWorkTime;
        } else {
            startTime = nowDate + startWorkTime;
            endTime = nowDate + " 23:59:59";
        }
        return superviseIntervalMapper.productivityOfToday(startTime, endTime);
    }

    @Override
    public List<SuperviseTowMonthVO> productivityOfTowMonth(String maType) {
        LocalDate curMonth = LocalDate.now();
        LocalDate lastMonth = curMonth.minusMonths(1);
        List<SuperviseTowMonthVO> curSum = superviseIntervalMapper.sumByDateMatype(curMonth.toString(), maType);
        List<SuperviseTowMonthVO> lastSum = superviseIntervalMapper.sumByDateMatype(lastMonth.toString(), maType);
        HashMap<Integer, SuperviseTowMonthVO> map = new HashMap<>();
        for (SuperviseTowMonthVO su : curSum) {
            map.put(su.getId(), su);
        }
        for (SuperviseTowMonthVO sup : lastSum) {
            if (map.containsKey(sup.getId())) {
                map.get(sup.getId()).setLastMonthSum(sup.getCurMonthSum());
            } else {
                sup.setLastMonthSum(sup.getCurMonthSum());
                sup.setCurMonthSum(0);
                map.put(sup.getId(), sup);
            }
        }
        return new ArrayList<>(map.values());
    }


}
