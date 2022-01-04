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
package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.vo.*;
import com.yb.machine.request.OrderProcessScheduleRequest;
import com.yb.machine.response.OrderProcessScheduleVO;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.supervise.vo.PerformanceAnalyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 生产执行上报信息_yb_execute_briefer Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecuteBrieferMapper extends BaseMapper<ExecuteBriefer> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeBriefer
     * @return
     */
    List<ExecuteBrieferVO> selectExecuteBrieferPage(IPage page, ExecuteBrieferVO executeBriefer);


    List<ExecuteBriefer> getNoReportOrder();

    ExecuteBriefer selectByExId(Integer exId);

    /*分页查询生产订单数*/
    List<ExecuteBrieferVO> pageFindList(Integer current, Integer size, Integer orderId, String orderName);

    /*生产订单总数*/
    Integer executeBrieferCount(Integer orderId, String orderName);

    /*查询班次时间*/
    ExecuteBriefer getStartTimeByCondition(Integer exId);
    /*根据订单id或名称查询*/
    /* List<ExecuteBrieferVO> findOrderByOne(Integer orderId, String orderName);*/

    /**
     * 查询对应作业批次的对应部件生产状况
     */
    List<ExecuteBriefer> detailBatchNo(ProdPartsinfoVo partsinfoVo);

    ExecuteBrieferVO getExecuteVoByEsId(Integer Id);

    /**
     * 根据exId获取上报信息
     *
     * @param exId
     * @return
     */
    ExecuteBriefer getByExId(@Param("exId") Integer exId);

    /**
     * 获取班次的上报作业总数,良品总数等
     *
     * @param exIds
     * @return
     */
    List<ExecuteBriefer> getNumberByExId(@Param("exIds") List<Integer> exIds);


    PerformanceAnalyVO getCountNumAndWasteNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("maType") String maType);

    ExecuteBriefer getBybfId(Integer bfId);

    Integer getShiftLastOrder(@Param("maId") Integer maId, @Param("wsId") Integer wsId);

    Integer getCountNumBySdId(@Param("sdId") Integer sdId, @Param("exId") Integer exId);

    Integer updateCheck(@Param("bfer") ExecuteBriefer bfer);

    Integer getTotalByWfid(Integer wfid);

    Integer getTotalBySdid(Integer sdid);

    /**
     * 查询本设备已上报数据
     *
     * @param targetDay 日期
     * @param maId      设备id
     * @param wsId      班次id
     * @return
     */
    List<ReportedVO> getExecuteBrieferList(@Param("targetDay") String targetDay,
                                           @Param("maId") Integer maId,
                                           @Param("wsId") Integer wsId);

    List<OrderProcessScheduleVO> orderProcessSchedule(@Param("request") OrderProcessScheduleRequest request);

    /**
     * 机台上报列表详情
     *
     * @param id
     * @return
     */
    ExecuteBrieferDetailVO getExecuteBrieferDetail(Integer id);

    /**
     * 查询上报历史数据的托盘清单
     *
     * @param id
     * @return
     */
    List<TraycardDetailedVO> traycardDetailedVOList(@Param("id") Integer id);

    /**
     * 查询未上报的数据
     *
     * @param notExecuteBrieferRequest
     * @param page
     * @return
     */
    List<ExecuteExamineVO> notExecuteBrieferList(@Param("notExecuteBrieferRequest") NotExecuteBrieferRequest notExecuteBrieferRequest,
                                                 @Param("page") IPage<ExecuteExamineVO> page);

    List<ExecuteBrieferOrdlinkVO> notReportBrieferList(Integer maId);
}
