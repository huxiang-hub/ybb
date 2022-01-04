package com.yb.statis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.statis.entity.StatisMachreach;
import com.yb.statis.request.DeviceCapacityProgressRequest;
import com.yb.statis.request.HourPlanRateRequest;
import com.yb.statis.request.StatisMachreachListRequest;
import com.yb.statis.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface StatisMachreachMapper extends BaseMapper<StatisMachreach> {
    /*设备达成率查询*/
    List<StatisMachreach> selectStatisMachreach(IPage page, StatisMachreachVO statisMachreach);

    /*设备达成率详情*/
    List<StatisMachreachVO> selectlist(Integer seId);

    /**
     * 计划达成率
     *
     * @param request
     * @return
     */
    List<StatisMachreachListVO> planRate(@Param("page") IPage page, @Param("request") StatisMachreachListRequest request);

    /**
     * 月计划达成率
     *
     * @param startTime
     * @param endTime
     * @param maType
     * @param wsId
     * @return
     */
    List<MonthStatisMachreachListVO> monthPlanRate(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maType") String maType, @Param("groupBy") String groupBy, @Param("wsId") Integer wsId);

    List<DayStatisMachreachListVO> hourPlanRate(HourPlanRateRequest request);

    StatisMachreach getLastBySdId(@Param("wfId") Integer wfId, @Param("maId") Integer maId, @Param("wsId") Integer wsId, @Param("sdDate") String sdDate);


    List<DeviceCapacityProgressListVO> deviceCapacityProgress(DeviceCapacityProgressRequest request);

    List<DeviceOrderNumProgressListVO> deviceOrderNumProgress(DeviceCapacityProgressRequest request);


    DeviceOrderNumProgressListVO getOrderNum(@Param("targetDay") String targetDay, @Param("wsId") Integer wsId, @Param("maId") Integer maId);
}
