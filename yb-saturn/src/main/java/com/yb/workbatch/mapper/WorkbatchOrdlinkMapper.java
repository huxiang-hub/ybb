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
package com.yb.workbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.machine.request.OrderDetailRequest;
import com.yb.machine.response.OrderDetailVO;
import com.yb.panelapi.common.TempEntity;
import com.yb.statis.vo.MasterPlanExeVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.request.WaitOrderRequest;
import com.yb.workbatch.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 生产排产表yb_workbatch_ordlink Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface WorkbatchOrdlinkMapper extends BaseMapper<WorkbatchOrdlink> {

    /****
     *
     * @param id
     * @return
     * @@ deprecated 谨慎使用该方法，会关联其他表内容数据
     */
    WorkbatchOrdlinkShiftVO getOrdshiftById(Integer id);

    WorkbatchOrdlink getOrdById(Integer id);

    WorkbatchOrdlinkShiftVO getWorkbatchOrd(Integer id, String sdDate, Integer wsId, Integer maId, Integer wfId);

    WorkbatchOrdlink getBySdDateAndSdIdAndWsId(@Param("sdId") Integer sdId);

    List<WorkbatchOrdlinkShiftVO> selectBatchIdsAndWfIds(@Param("ids") List<Integer> ids, @Param("wfIds") List<Integer> wfIds);


    List<WorkbatchOrdlinkShiftVO> selectBatchIds(@Param("ids") List<Integer> ids);

    List<WorkbatchOrdlinkShiftVO> selectBatchWsIds(@Param("wsIds") List<Integer> wsIds);

    /**
     * 自定义分页
     *
     * @param page
     * @param workbatchOrdlink
     * @return
     */
    List<WorkbatchOrdlinkVO> selectWorkbatchOrdlinkPage(IPage page, WorkbatchOrdlinkVO workbatchOrdlink);

    /**
     * 排产自定义分页
     *
     * @param page
     * @param workbatchOrdlink
     * @return
     */
    List<PlanProduceModelVO> planProducePage(IPage page, WorkbatchOrdlinkVO workbatchOrdlink);

    /**
     * 查询某一排产
     *
     * @param workbatchOrdlink
     * @return
     */
    PlanProduceModelVO planProduceDetail(@Param("workbatchOrdlink") WorkbatchOrdlinkVO workbatchOrdlink);

    /**
     * 需要提交待审核的数据
     *
     * @param page
     * @param workbatchOrdlink
     * @return
     */
    List<PlanProduceModelVO> planProduceToActSetPage(IPage page, WorkbatchOrdlinkVO workbatchOrdlink);


    /**
     * 查询待生产的订单
     *
     * @param waitOrder
     * @return
     */
    List<WorkbatchOrdlink> getOrderList(WorkbatchOrdlink waitOrder);

    /**
     * 获取当前设备运行的订单
     *
     * @param runOrder
     * @return
     */
    WorkbatchOrdlinkVO getRunOrder(WorkbatchOrdlink runOrder);

    List<WorkbatchOrdlinkVO> getNoReportOrder(Integer maId);

    List<WorkbatchOrdlinkVO> getNoReportOrderByIsRecepro(Integer maId);

    List<TempEntity> getFaultAndQuality(@PathParam("maId") Integer maId,
                                        @PathParam("event") String event);

    List<WorkbatchOrdlink> getOrderListByOrderId(Integer id);

    String getOrderName(Integer sdId);

    List<WorkbatchOrdlinkVO> getMaAssignment(String startTime, Integer maId, String ckName);

    /**
     * 根据班次名称查询对应的排产信息
     *
     * @param ckName
     * @return
     */
    List<WorkbatchOeeShiftinVO> getWorkbatchOrdlinkVOList(String ckName);

    Integer getOdIdByWbId(Integer wbId);

    WorkbatchOrdlinkVO getWorkBatchInfoBySdId(Integer sdId);

    WorkbatchOrdoeeVo getWorkbatchOEEById(Integer id);

    /**
     * 排产导入huxiang
     *
     * @param workbatchOrdlinkVO
     * @return
     */
    List<WorkbatchOrdlinkVO> workbatchOrdlinkoeeExcel(IPage page, WorkbatchOrdlinkVO workbatchOrdlinkVO);


    /**
     * 获取没有h指定设备的工单
     *
     * @param waitOrder
     * @return
     */
    List<WorkbatchOrdlink> findNotHaveMaidWorkbatchOrdlink(@Param("waitOrder") WorkbatchOrdlink waitOrder, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("ids") List<Integer> ids);

    /**
     * 获取没有指定设备的工单
     *
     * @param waitOrder
     * @return
     */
    List<WorkbatchOrdlinkVO> findWorkbatchOrdlink(@Param("waitOrder") WorkbatchOrdlink waitOrder, @Param("startDate") String startDate, @Param("endDate")String endDate);

    List<WorkbatchShiftListVO> findWorkbatchOrdlinkNew(@Param("waitOrder") WaitOrderRequest waitOrder, @Param("startDate") String startDate, @Param("endDate")String endDate);

    List<WorkbatchOrdlinkVO> findWorkbatchOrdlinkByMatype(@Param("waitOrder") WorkbatchOrdlink waitOrder, String startDate, String endDate);

    List<WorkbatchShiftListVO>  getOrderListByPrids(String prIds,String startDate, String endDate);

    List<Map> findWorkbatchOrdlinkCount(@Param("waitOrder") WorkbatchOrdlink waitOrder, String startDate, String endDate);

    /**
     * 查询部门下全部设备上的排产记录
     *
     * @param startTime
     * @param dpId
     * @param ckName
     * @return
     */
    List<WorkbatchOrdlinkVO> getDeptMachineAll(String startTime, Integer dpId, String ckName);

    /**
     * 查询用户下正在生产工单
     *
     * @param runOrder
     * @return
     */
    List<WorkbatchOrdlinkVO> getUserRunOrder(WorkbatchOrdlink runOrder);

    /**
     * 查询用户下待上报工单
     *
     * @param usId
     * @return
     */
    List<WorkbatchOrdlinkVO> getUserBrieferOrder(Integer usId);

    /**
     * 获取未绑定设备的可接工单
     *
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> getNoMachineSdOrderList(Integer maId);

    /**
     * 获取绑定设备可接工单
     *
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> getMachineSdOrderList(Integer maId);

    /**
     * 获取排序最近的上一批数据
     */
    List<WorkbatchOrdlink> getSortUP(@Param("id") Integer id);

    /**
     * 获取排序最近的下一批数据
     */
    List<WorkbatchOrdlink> getSortDown(@Param("id") Integer id);

    /**
     * 查询排序
     */
    String getSdSort();

    /**
     * 查询时序图计划时间段
     *
     * @param wsId
     * @param maType
     * @param startTime
     * @param endTime
     * @param maIdSet
     * @return
     */
    List<PlanStateTime> selectPlanMainch(
            @Param("wsId") Integer wsId,
            @Param("maType") Integer maType,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("maIdSet") Set<Integer> maIdSet);


    /**
     * 获取已接单但未点正式生产的订单
     *
     * @param maId
     * @return
     */
    WorkbatchOrdlinkVO getReceivedOrder(Integer maId);

    /**
     * 查询已排产的排产单
     *
     * @param workbatchOrdlinkVO
     * @return
     */
    List<WorkbatchOrdlinkVO> getwaitWorkBatchOrd(WorkbatchOrdlinkVO workbatchOrdlinkVO);

    /**
     * 查询待排产的排产单
     *
     * @param workbatchOrdlinkVO
     * @return
     */
    List<WorkbatchOrdlinkVO> getyetWorkBatchOrd(@Param("page") IPage page, @Param("parm") WorkbatchOrdlinkVO workbatchOrdlinkVO, @Param("prIds") List<Integer> prIds);

    /**
     * 根据设备id获取排产单信息
     *
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> findByMaId(Integer maId);

    List<WorkbatchOrdlink> findByWsIds(@Param("wsIds") List wsIds);


    /****
     * 根据查询条件返回对应的排产班次的排产单列表
     * @param worklink
     * @return
     */
    List<WorkbatchOrdlinkShiftVO> getOrdLinkBysdate(WorkbatchOrdlinkShiftVO worklink);

    /****
     * 根据日期和班次确定设备列表
     * @param targetDay
     * @param wsId
     * @return
     */
    List<Integer> getMachineBytargetDay(String targetDay, Integer wsId);

    /****
     * 根据日期查询设备信息
     * @param targetDay
     * @return
     */
    List<Integer> getMachineBySaDate(String targetDay);

    /***
     * 根据排产日期班次设备获取排产信息
     * @param targetDay
     * @param wsId
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> selectListBytargetDay(String targetDay, Integer wsId, Integer maId);

    List<Integer> getMaIdByday(String targetDay, Integer wsId);

    /**
     * 查询三天内的数据量
     *
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> getDayList(String startDate, String endDate, Integer maId);


    List<MasterPlanExeVO> findOrdLinkByWbNoAndPrIdAndDate(@Param("wbId") Integer wbId, @Param("prId") Integer prId, @Param("date") Date date);


    List<WorkbatchOrdlinkShiftVO> getListByshift(String targetDay, Integer wsId, Integer maId);

    WorkbatchShift getWorkShiftBymaIdwsId(String targetDay, Integer wsId, Integer maId);

    /**
     * 获取当前班次的所有排产信息
     *
     * @param sdDate
     * @param wsId
     * @return
     */
    List<WorkbatchOrdlink> getMaIdList(String sdDate, Integer wsId);


    /**
     * 根据当前日期查询设备id
     *
     * @param sdDate
     * @return
     */
    List<Integer> getBySdDate(String sdDate, Integer wsId);


    /****
     * 查询下工序的对象内容
     * @param ordlink
     * @return
     */
    List<WorkbatchOrdlink> workbathByDownOrd(@Param("ordlink") WorkbatchOrdlink ordlink);

    int updateBysdsort(String sdSort, Integer id);

    /**
     * 根据日期,班次.设备id查询排产信息
     *
     * @param sdDate
     * @param maId
     * @param wsId
     * @return
     */
    List<WorkbatchOrdlinkVO> selectBySdDateMaIdWsId(String sdDate, Integer maId, Integer wsId);

    Integer getSumPlanNum(@Param("sdId") Integer sdId, @Param("sdDate") String sdDate, @Param("wsId") Integer wsId);


    WorkbatchOrdlink findByWbNoAndPtIdAndPrId(@Param("wbNo") String wbNo, @Param("ptId") Integer ptId, @Param("prId") Integer prId);

    int updateOrdStatusBySdsort(Integer id, String sdDate, String sdSort, String status);

    int updateOrdStatus(Integer id, String status, Integer maId);

    int updateBindByMaId(Integer id, Integer maId, String status);

    int updatePlannumById(Integer id, Integer planNum);

    WorkbatchOrdlinkOeeVO getOrdlinkOeeBySdId(Integer sdId);

    int updateOtherInfo(Integer id, String finalTime, String ingredientTime, String remarks, String secondRemark);

    int updateAcceptByUsId(Integer id, Integer runStatus);

    /**
     * 查询平方数所需值
     *
     * @param maId
     * @param wsId
     * @param targetDay
     * @return
     */
    List<SquareVO> selectSquareList(Integer maId, Integer wsId, String targetDay);

    /**
     * 获取已接单但未点正式生产的订单
     *
     * @param maId
     * @return
     */
    WorkbatchOrdlinkVO getReceivedOrderByMaInfo(Integer maId);

    WorkbatchOrdlinkVO getReceivedOrderBywfId(Integer wfId);

    List<WorkbatchOrdlink> ruleQuery(@Param("whereSql") String whereSql);

    /**
     * 根据工序名称,工单编号查询上工序信息
     *
     * @param wbNo
     * @param prName
     * @return
     */
    WorkbatchOrdlink findByWbNoAndPrName(String wbNo, String prName);
    /*
     *//**
     * 根据wfId查询排产相关信息
     * @param voWfId
     * @return
     *//*
    WorkbatchOrdlinkVO getOneByWfId(Integer voWfId);*/

    /**
     * 根据wfId集合查询排产相关信息
     *
     * @param wfIdSet
     * @return
     */
    List<WorkbatchOrdlinkVO> getWorkbatchOrdlinkVOListByWfIdList(@Param("wfIdSet") Set<Integer> wfIdSet);

    /**
     * 查询在制品的客户名称和订单信息
     *
     * @param articlesBeingProcessedParmVO
     * @return
     */
    List<ArticlesOrderVO> getArticlesCmNameOdNoVOList(@Param("articlesBeingProcessedParmVO") ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO);

    /**
     * 查询在制品的批次和工序信息
     *
     * @param articlesBeingProcessedParmVO
     * @param odNo
     * @return
     */
    List<ArticlesProcessVO> getArticlesWbProcessVOList(@Param("articlesBeingProcessedParmVO") ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO, @Param("odNo") String odNo);

    /**
     * 查询在制品班次排产信息
     *
     * @param sdId
     * @return
     */
    List<ArticlesShiftVO> getArticlesShiftList(@Param("sdId") Integer sdId);

    /**
     * 获取当前日期的所有wfId
     *
     * @param targetDay
     * @return
     */
    List<Integer> getWfIdList(@Param("targetDay") String targetDay, @Param("wsId") Integer wsId);
    @Select("SELECT DISTINCT od_no, cm_name AS customer, pd_name AS prodName FROM yb_workbatch_ordlink")
    IPage<WorksTempoVO> selectWorsTempo(IPage<WorksTempoVO> page);

    //修改工单的待排数量和待排产状态9 ordId
    Integer updateByworksNum(Integer sdId, Integer wknum, String status);

    //设置完成数量信息
    Integer setCompleteNum(Integer sdId, Integer completeNum, Integer incompleteNum);

    List<OrderDetailVO> orderDetail(@Param("request") OrderDetailRequest request);

    /**
     * 获取上工序的sdId
     * @param wfId
     * @return
     */
    Integer getUpProcessSdIdByWfId(Integer wfId);

    /**
     * 查询排产详情图所需数据
     * @param detailsParam
     * @return
     */
    List<WorkbatchShiftDetailVO> productionSchedulingDetails(@Param("detailsParam") ProductionSchedulingDetailsParam detailsParam);

    /**
     * 获取排产饱和度
     * @param detailsParam
     * @return
     */
    List<SaturabilityVO> getSaturability(@Param("detailsParam") ProductionSchedulingDetailsParam detailsParam);
}


