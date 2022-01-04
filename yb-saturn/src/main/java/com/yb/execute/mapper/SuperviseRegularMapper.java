package com.yb.execute.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.execute.entity.SuperviseRegular;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.response.MachineSpeedChangePortVO;
import com.yb.machine.vo.TodayCapacityVO;
import com.yb.supervise.request.PerformanceAnalyRequest;
import com.yb.supervise.vo.PerformanceAnalyVOListVO;
import com.yb.supervise.vo.SuperviseIntervalVO;
import com.yb.supervise.vo.queryInterval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 设备状态间隔表
 * @Author my
 * @Date Created in 2020/6/9
 */
@Mapper
public interface SuperviseRegularMapper extends BaseMapper<SuperviseRegular> {

    /**
     * 根据当前小时和当前日期获取
     *
     * @param targetDay
     * @param targetHour
     * @return
     */
    List<SuperviseRegular> findByTargetDayAndTargetHour(String targetDay, Integer targetHour);

    List<SuperviseRegular> findByPcountAndStartTimeAndEndTime(Date startTime, Date endTime);

    Integer getPcountByStartTimeAndEndTimeAndMaid(Date startTime, Date endTime, Integer maId);

    SuperviseRegular getByMaid(@Param("maId") Integer maId);

    SuperviseRegular findByMaidHour(String targetDay, Integer targetHour, Integer maId);

    List<Map<String, Object>> findByMaidDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("maId") Integer maId);

    List<PerformanceAnalyVOListVO> getPerformanceAnaly(@Param("request") PerformanceAnalyRequest request, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    List<Integer> getLineDataMa(@Param("data") queryInterval queryInterval);


    List<SuperviseIntervalVO> getLineData(@Param("data") queryInterval queryInterval);

    Integer getWsYield(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maId") Integer maId);

    List<MachineSpeedChangePortVO> getSpeedReport(@Param("request") MachineReportRequest request);

    List<TodayCapacityVO> todayCapacity();


}

