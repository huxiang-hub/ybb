/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.supervise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.response.MachineStatusChangePortVO;
import com.yb.panelapi.request.MachineStopListRequest;
import com.yb.panelapi.vo.MachineStopListVO;
import com.yb.supervise.entity.SuperviseInterval;
import com.yb.supervise.vo.SuperviseIntervalVO;
import com.yb.supervise.vo.SuperviseTowMonthVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备清零日志表 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseIntervalMapper extends BaseMapper<SuperviseInterval> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseInterval
     * @return
     */
    List<SuperviseIntervalVO> selectSuperviseIntervalPage(IPage page, SuperviseIntervalVO superviseInterval);

    /**
     * 通过日期查询某日的
     *
     * @param targetDate
     * @return
     */
    List<SuperviseIntervalVO> getDayResultByCheckDay(String targetDate, String dpId, String proType);

    /**
     * 通过日期查询每个小时的result
     *
     * @param targetDate
     * @return
     */
    List<SuperviseIntervalVO> getHourResultByCheckDay(String targetDate, String dpId, String proType, Integer startTime, Integer endTime);

    /**
     * 按照每周的去进行查询
     *
     * @param dpId
     * @param proType
     * @return
     */
    List<SuperviseIntervalVO> getWeekResultByCheckDay(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("dpId") String dpId, @Param("proType") String proType);

    /**
     * 设备时间
     *
     * @param status
     * @param mtId
     * @param startTime
     * @param endTime
     * @return
     */
    Integer getSumTimeByMaId(Integer status, Integer mtId, Date startTime, Date endTime);

    /**
     * 按照班次去统计数据
     *
     * @param startTimeformat
     * @param endTimeFormat
     * @param dpId
     * @param proType
     * @return
     */
    List<SuperviseIntervalVO> getCalculateCkNameResult(String startTimeformat, String endTimeFormat, String dpId, String proType);

    /**
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
     *
     * @param maId
     * @param classStartTime
     * @param classEndTime
     * @return
     */
    Integer SumBoxNumber(Integer maId, Date classStartTime, Date classEndTime);

    List<MachineStopListVO> stopList(@Param("request") MachineStopListRequest request, @Param("condition") String condition);

    /**
     * 根据日期查询设备id集
     *
     * @param targetDay
     * @return
     */
    List<Integer> getMaIdsByTargetDay(String targetDay);

    List<Map<String, Integer>> productivityOfToday(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<SuperviseTowMonthVO> sumByDateMatype(@Param("curMonth") String curMonth, @Param("maType") String maType);

    Integer getNowWsOpenTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maId") Integer maId);

    List<MachineStatusChangePortVO> getStatusChangePort(@Param("request") MachineReportRequest request);
}
