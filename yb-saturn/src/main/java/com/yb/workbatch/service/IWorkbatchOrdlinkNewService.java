package com.yb.workbatch.service;

import com.yb.machine.entity.MachineMainfo;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.request.*;
import com.yb.workbatch.vo.ProductionSchedulingDetailsParam;
import com.yb.workbatch.vo.SaturabilityVO;
import com.yb.workbatch.vo.UpProcessScheduleVO;

import java.text.ParseException;
import java.util.List;

/**
 * @Description: 新排产
 * @Author my
 * @Date Created in 2020/7/25 10:55
 */
public interface IWorkbatchOrdlinkNewService {

    String updateStatus(WorkbatchOrdlinkStatusUpdateRequest request) throws ParseException;

    /**
     * 更新精益生产参数
     */
    void updateLeanSet(WorkBatchOrdlinkUpdateLeanSet request);

    /**
     * 更新备注和主料辅料入库时间和交期
     *
     * @param request
     */
    void updateMaterialAndRemark(WorkBatchOrdlinkMaterialUpdateRequest request);

    /**
     * 修改达成率计划数量
     *
     * @param request
     */
    void updateOrdReach(WorkBatchOrdReachUpdateRequest request);

    /**
     * 修改排产排序
     *
     * @param request
     */
    void updateWorkBatchSort(WorkBatchSortUpdateRequest request);

    /**
     * 获取同类型设备列表
     *
     * @param maType
     * @return
     */
    List<MachineMainfo> getSimilarDeviceList(String maType);

    void bindWorkBatch(WorkBatchBindRequest request) throws ParseException;

    List<WorkbatchShift> reschedule(Integer sdId);

    /****
     * 设定计划
     * @param wsId
     * @param sdDate
     * @param maId
     * @param reachIslock
     */
    Integer lockordreach(Integer wsId, String sdDate, Integer maId, Integer reachIslock);

    /****
     * 设定锁定排序内容
     * @param wsId
     * @param sdDate
     * @param maId
     * @param wfsortIslock
     */
    void lockWfsort(Integer wsId, String sdDate, Integer maId, Integer wfsortIslock);

    void refreshRealcount(Integer wsId, String sdDate, Integer maId);

    /**
     * 强制结束
     *
     * @param sdId
     */
    void forcedEnd(Integer sdId);

    /**
     * 获取上工序进度
     *
     * @param wfId
     */
    UpProcessScheduleVO upProcessSchedule(Integer wfId);

    /**
     * 根据设备日期获取有排产的日期
     *
     * @param maId
     * @param sdDate
     * @return
     */
    List<String> existingSchedule(Integer maId, String sdDate) throws ParseException;

    void setWorksNum(Integer sdId, Integer maId, String status);

    void setOrdShift(Integer sdId);

    /**
     * 获取排产饱和度
     * @param detailsParam
     * @return
     */
    List<SaturabilityVO> getSaturability(ProductionSchedulingDetailsParam detailsParam);
}
