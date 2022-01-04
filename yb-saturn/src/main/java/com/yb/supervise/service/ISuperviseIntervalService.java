package com.yb.supervise.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.supervise.entity.SuperviseInterval;
import com.yb.supervise.request.GetYieldStatisticsRequest;
import com.yb.supervise.request.PerformanceAnalyRequest;
import com.yb.supervise.vo.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备清零日志表 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface ISuperviseIntervalService extends IService<SuperviseInterval> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseInterval
     * @return
     */
    IPage<SuperviseIntervalVO> selectSuperviseIntervalPage (IPage<SuperviseIntervalVO> page, SuperviseIntervalVO superviseInterval);

    List<SuperviseIntervalVO> getDayResultByCheckDay(String targetDate,String dpId,String proType);

    List<SuperviseIntervalVO> getHourResultByCheckDay(String targetDate, String dpId,String proType,Integer startHour,Integer endHour);

    List<SuperviseIntervalVO> getWeekResultByCheckDay(String targetDate, String dpId, String proType) throws ParseException;

    List<SuperviseIntervalVO> getMonthResultByCheckDay(String targetDate,String dpId, String proType) throws ParseException;

    Integer getSumTimeByMaId(Integer status, Integer mtId, Date startTime, Date endTime);

    /**
     * 按照班次去统计数据
     * @param targetDate
     * @param dpId
     * @param proType
     * @return
     */
    List<SuperviseIntervalVO> getCalculateCkNameResult(String targetDate, String dpId, String proType);

    /**
     * 超过一天的班次的计数
     * @param targetDate
     * @param tomorrowDay
     * @param dpId
     * @param proType
     * @param startHour
     * @param endHour
     * @return
     */
    List<SuperviseIntervalVO> getHourResutPassDay(String targetDate, String tomorrowDay, String dpId, String proType, Integer startHour, Integer endHour);

    /**
     * 查询设备采集数
     * @param maId
     * @param classStartTime
     * @param classEndTime
     * @return
     */
    Integer SumBoxNumber(Integer maId, Date classStartTime, Date classEndTime);


    /**
     * 印聊效率分析
     * @param request
     * @return
     */
    YieldStatisticsVO getYieldStatistics(GetYieldStatisticsRequest request);

    /**
     * 印聊绩效统计
     * @param request
     * @return
     */
    PerformanceAnalyVO getPerformanceAnaly(PerformanceAnalyRequest request);

    List<Integer> getLineDataMa(queryInterval queryInterval);

    List<SuperviseIntervalVO> getLineData(queryInterval queryInterval);

    /**
     * 根据日期查询设备id集
     * @param targetDay
     * @return
     */
    List<Integer> getMaIdsByTargetDay(String targetDay);

    List<Map<String, Integer>> productivityOfToday();

    List<SuperviseTowMonthVO> productivityOfTowMonth(String maType);
}
