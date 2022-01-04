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
package com.yb.workbatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.common.entity.WorkbatchOrdlinkYS;
import com.yb.execute.vo.ExecuteBrieferOrdlinkVO;
import com.yb.panelapi.common.TempEntity;
import com.yb.panelapi.request.OrderAcceptRequest;
import com.yb.panelapi.user.utils.R;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.vo.*;

import java.util.List;
import java.util.Map;

/**
 * 生产排产表yb_workbatch_ordlink 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IWorkbatchOrdlinkService extends IService<WorkbatchOrdlink> {

    WorkbatchOrdlinkShiftVO getById(Integer id);

    WorkbatchOrdlink getOrdById(Integer sdId);

    /**
     * 自定义分页
     *
     * @param page
     * @param workbatchOrdlink
     * @return
     */
    IPage<WorkbatchOrdlinkVO> selectWorkbatchOrdlinkPage(IPage<WorkbatchOrdlinkVO> page, WorkbatchOrdlinkVO workbatchOrdlink);

    /**
     * 排产义分页
     *
     * @param page
     * @param workbatchOrdlink
     * @return
     */
    IPage<PlanProduceModelVO> planProducePage(IPage<PlanProduceModelVO> page, WorkbatchOrdlinkVO workbatchOrdlink);

    IPage<PlanProduceModelVO> planProduceToActSetPage(IPage<PlanProduceModelVO> page, WorkbatchOrdlinkVO workbatchOrdlink);

    List<WorkbatchOrdlinkVO> getOrderList(WorkbatchOrdlink workbatchOrdlink);

    List<WorkbatchShiftListVO> getOrderListNew(Integer maId, String startDate, String endDate);

    List<WorkbatchOrdlinkVO> getOrderListByMatype(WorkbatchOrdlink workbatchOrdlink);

    List<WorkbatchShiftListVO> getOrderListByPrids(Integer maId, String startDate, String endDate);

    WorkbatchOrdlinkVO getRunOrder(Integer maId);

    //接受订单
    R acceptOrder(WorkbatchOrdlinkVO ordlinkVO);

    R acceptOrderNew(WorkbatchOrdlinkVO ordlinkVO);

    R acceptOrderBywfid(OrderAcceptRequest orderAccept);

    //正式生产订单
    R proOrder(WorkbatchOrdlinkVO ordlinkVO);

    //正式生产订单
    R proOrderNew(Integer maId);

    //结束生产
    R finishOrder(Integer maId, Integer sdId, Integer usId, Integer infoIgetUpProcessSdIdByWfIdd, Integer wfId, Integer exStatus);

    //结束生产
    R finishOrderNew(Integer maId);


    //获取当前订单生产数量
    int getCurrNum(Integer maId);

    //获取当前订单生产数量
    String getOrderName(Integer sdId);

    List<WorkbatchOrdlinkVO> getNoReportOrder(Integer maId);

    List<ExecuteBrieferOrdlinkVO> getNoReportOrderNew(Integer maId);

    List<TempEntity> getFaultAndQuality(Integer maId, String event);

    /**
     * 设备某天的排产数
     *
     * @param startTime
     * @param maId
     * @param ckName
     * @return
     */
    List<WorkbatchOrdlinkVO> getMaAssignment(String startTime, Integer maId, String ckName);

    /**
     * 新增排产数据
     *
     * @param workbatchOrdoeeVO
     * @return
     */
    Integer insertOrdOEEOrdlink(WorkbatchOrdoeeVo workbatchOrdoeeVO);

    /**
     * 查询未提交自审核或审核失败的排产数据
     */
    List<WorkbatchOeeShiftinVO> getWorkbatchOrdlinkVOList(String ckName);

    /**
     * 修改排产,排产oee,排产班次数据
     *
     * @param workbatchOrdoeeVO
     * @return
     */
    Boolean updataOrdOEEOrdlink(WorkbatchOrdoeeVo workbatchOrdoeeVO);

    /**
     * 根据sdID 把所有的东西查询完毕
     *
     * @param sdId
     * @return
     */
    WorkbatchOrdlinkVO getWorkBatchInfoBySdId(Integer sdId);

    WorkbatchOrdoeeVo getWorkbatchOEEById(Integer id);

    /**
     * 排产导入huxiang
     *
     * @param workbatchOrdlinkVO
     * @param page
     * @return
     */
    IPage<WorkbatchOrdlinkVO> WorkbatchOrdlinkoeeExcel(WorkbatchOrdlinkVO workbatchOrdlinkVO, IPage<WorkbatchOrdlinkVO> page);

    /**
     * 导出排产和排产oee数据
     *
     * @param ids
     */
    void WorkbatchOrdlinkExcelExport(List<Integer> ids);


    PlanProduceModelVO planProduceDetail(WorkbatchOrdlinkVO workbatchOrdlink);

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
     * 查询用户正在生产的工单信息
     *
     * @param usId
     * @return
     */
    List<WorkbatchOrdlinkVO> getUserRunOrder(Integer usId);

    /**
     * 查询用户待上报工单
     *
     * @param usId
     * @return
     */
    List<WorkbatchOrdlinkVO> getUserBrieferOrder(Integer usId);

    /**
     * 扫码获取可接工单
     *
     * @param maId
     * @return
     */
    List<WorkbatchOrdlink> getSdOrderList(Integer maId);

    /**
     * 获取排序最近的上一批数据
     */
    List<WorkbatchOrdlink> getSortUP(Integer id);

    /**
     * 获取排序最近的下一批数据
     */
    List<WorkbatchOrdlink> getSortDown(Integer id);

    /**
     * 查询排序
     */
    String getSdSort();

    /**
     * 传入印刷排期表数据新增排产相关数据
     *
     * @param workbatchOrdlinkYSList
     */
    String saveOrdlinkYS(List<WorkbatchOrdlinkYS> workbatchOrdlinkYSList);

    /**
     * 获取已接单但未点正式生产的订单
     *
     * @param maId
     * @return
     */
    WorkbatchOrdlinkVO getReceivedOrder(Integer maId);

    WorkbatchOrdlinkVO getReceivedOrderBywfId(Integer wfId);


    List<WorkbatchOrdlinkShiftVO> getOrdLinkBysdate(String sdDate, Integer maId, Integer wsId);

    /**
     * 查询待排产排产
     */
    List<WorkbatchOrdlinkVO> getwaitWorkBatchOrd(WorkbatchOrdlinkVO workbatchOrdlinkVO);

    /**
     * 查询三天内的数据量
     *
     * @param maId
     * @return
     */
    Map<String, Integer> getDayList(Integer maId, Integer dpId, Integer wsId);

    /**
     * 查询生产准备排产单
     *
     * @param maId
     * @return
     */
    WorkbatchOrdlinkVO getPudectOrder(Integer maId);

    /**
     * 查询待排产排产
     */
    IPage<WorkbatchOrdlinkVO> getyetWorkBatchOrd(IPage<WorkbatchOrdlinkVO> page, WorkbatchOrdlinkVO workbatchOrdlinkVO);

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
     * @param targetDay
     * @return
     */
    List<Integer> getBySdDate(String targetDay, Integer wsId);

    /**
     * 根据日期,班次,设备导出排产数据
     *
     * @param sdDate
     * @param maId
     * @param wsId
     */
    void XYWorkbatchExcelExport(String sdDate, Integer maId, Integer wsId);

    int andProductionScheduling(WorkbatchOrdlinkVO workbatchOrdlinkVO);

    int updateOrdStatusBySdsort(WorkbatchOrdlink ordlink);

    int updateOrdStatus(WorkbatchOrdlink ordlink);

    int updateBindByMaId(WorkbatchOrdlink ordlink);

    int updatePlannumById(WorkbatchOrdlink ordlink);

    WorkbatchOrdlinkOeeVO getOrdlinkOeeBySdId(Integer sdId);

    int updateOtherInfo(WorkbatchOrdlink ordlink);

    /**
     * 查询在制品的客户名称和订单信息
     *
     * @param articlesBeingProcessedParmVO
     * @return
     */
    List<ArticlesCmNameOdNoVO> getArticlesCmNameOdNoVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO);

    /**
     * 查询在制品的批次和工序信息
     *
     * @param articlesBeingProcessedParmVO
     * @param odNo
     * @return
     */
    List<ArticlesWbProcessVO> getArticlesWbProcessVOList(ArticlesBeingProcessedParmVO articlesBeingProcessedParmVO, String odNo);

    /**
     * 查询在制品班次排产信息
     *
     * @param sdId
     * @return
     */
    List<ArticlesShiftVO> getArticlesShiftList(Integer sdId);

    /**
     * 获取当前日期的所有wfId
     *
     * @param targetDay
     * @return
     */
    List<Integer> getWfIdList(String targetDay, Integer wsId);

    /**
     * 根据设备id获取排产单
     *
     * @param machineId
     * @return
     */
    IPage<WorkbatchOrdlink> getByMachineId(Integer machineId, IPage<WorkbatchOrdlink> page);

    /**
     * 根据工序id获取排产单
     *
     * @param machineId
     * @return
     */
    IPage<WorkbatchOrdlink> getByProcessId(Integer machineId, IPage<WorkbatchOrdlink> page);

    /**
     * 根据工单id查询未排产数
     *
     * @param sdId
     * @return
     */
    Integer worksNum(Integer sdId, Integer maId);

    IPage<WorksTempoVO> worksTempo(IPage<WorksTempoVO> page);

    Integer updateByworksNum(Integer sdId, Integer wknum, String status);

    Integer setCompleteNum(Integer sdId, Integer completeNum, Integer incompleteNum);

    Integer hasShiftThreeDay(Integer maId);

    /**
     * 查询排产详情图所需数据
     *
     * @param detailsParam
     * @return
     */
    List<ProductionSchedulingDetailsVO> productionSchedulingDetails(ProductionSchedulingDetailsParam detailsParam);
}
