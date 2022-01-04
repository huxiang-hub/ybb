package com.yb.workbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.vo.TakeStockVO;
import com.yb.statis.request.DeviceCurrentOrderRequest;
import com.yb.statis.vo.DeviceCurrentOrderVO;
import com.yb.statis.vo.GetShiftreachVO;
import com.yb.statis.vo.ShiftreachListVO;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface WorkbatchShiftMapper extends BaseMapper<WorkbatchShift> {

//    List<WorkbatchShiftVO> selectWorkBatchShiftVoByCondition(String ckName, String dateStr);

    WorkbatchShiftVO getWorkShiftInfoBySdId(Integer sdId);

    List<Integer> getByTime(@Param("time") Date time);

    List<WorkbatchMachShiftVO> findByMaId(@Param("maId") Integer maId);

    List<WorkbatchMachShiftVO> findByMaIds(@Param("maIds") String maIds);

    List<WorkbatchMachShiftVO> findList();

    WorkbatchMachShiftVO findShiftByMaIdAndWsId(@Param("maId") Integer maId, @Param("wsId") Integer wsId);

    WorkbatchMachShiftVO getById(@Param("id") Integer id);

    WorkbatchShiftVO getBywfId(Integer wfId);

    WorkbatchShift getWbshiftByBfId(Integer wfId);


    WorkbatchShift getBySdDateAndSdId(String sdDate, Integer sdId, Integer wsId, Integer maId);

    //该设备的某天某班次的排产顺序信息
    List<WorkbatchShift> getShiftByMaid(@Param("sdDate") String sdDate, @Param("wsId") Integer wsId, @Param("maId") Integer maId);

    /**
     * 根据设备id和时间查询班次id
     *
     * @param maId
     * @param wsFormat
     * @return
     */
    WorkbatchMachShiftVO findByMaIdWsTime(@Param("maId") Integer maId, @Param("wsFormat") String wsFormat);


    WorkbatchShift getBySdDateAndSdId(@Param("sdDate") String sdDate, @Param("sdId") Integer sdId);

    Integer getStaycount(@Param("planNum") Integer planNum, @Param("sdId") Integer sdId);

    WorkbatchShift getWsid(@Param("sdDate") String sdDate, @Param("sdId") Integer sdId, @Param("wsId") Integer wsId, @Param("maId") Integer maId);

    List<WorkbatchShift> reschedule(@Param("sdId") Integer sdId);

    int getWshiftCount(Integer sdId);

    int lockWfsort(List<Integer> wfIds, Integer islock);


    List<WorkbatchShift> getProOrder(@Param("madId") Integer madId);

    Integer getYetNum(@Param("sdId") Integer sdId);

    Integer getPlanNumBySdId(@Param("sdId") Integer sdId);

    /**
     * 根据设备班次id查询下个班次信息
     *
     * @param wsId
     * @param maId
     * @return
     */
    WorkbatchMachShiftVO getNextWorkbatchShift(Integer wsId, Integer maId);

    /**
     * 锁定解锁顺序
     *
     * @param ids
     * @param wfsortIslock
     * @return
     */
    Integer shiftLockSort(@Param("ids") List<Integer> ids, Integer wfsortIslock);

    /**
     * 根据id集排序查询
     *
     * @param wfIds
     * @return
     */
    List<WorkbatchShift> anewWfsDSort(@Param("wfIds") List<Integer> wfIds);

    /**
     * 查询该设备,日期班次下的可接排产单
     *
     * @param maId
     * @param wsId
     * @param sdDate
     * @return
     */
    List<WorkbatchShift> selectSdSortList(Integer maId, Integer wsId, String sdDate);

    /**
     * 查询所有已保存顺序的排产单
     *
     * @param maId
     * @param wsId
     * @param sdDate
     * @return
     */
    List<WorkbatchShift> selectworkbatchShiftList(Integer maId, Integer wsId, String sdDate);

    /**
     * 获取所选日期班次的班次排产个数
     *
     * @param sdDate
     * @param wsId
     * @return
     */
    Integer getShiftCountNum(@Param("sdDate") String sdDate, @Param("wsId") Integer wsId, @Param("maId") Integer maId);


    List<SlideShiftDetailsVO> findSlideShiftDetails(@Param("sdId") Integer sdId);

    List<SworkbatchShiftVO> findShiftBysdId(Integer sdId);

    /**
     * 获取设备当前订单进度信息
     *
     * @param request
     * @return
     */
    List<DeviceCurrentOrderVO> getDeviceCurrentOrder(DeviceCurrentOrderRequest request);

    WorkbatchShift getNowOrder(@Param("maId") Integer maId);

    int getOrdeNum(@Param("maId") Integer maId, @Param("wsIds") List<Integer> wsId, @Param("sdDate") String sdDate, @Param("shiftStatus") Integer shiftStatus, @Param("status") String status);

    Integer getStopNum(@Param("wsIds") List<Integer> wsIds, @Param("sdDate") String targetDay, @Param("status") String status);

    /**
     * 根据设备id，开始和结束时间获取其中有排查的日期
     *
     * @param maId
     * @param beginDayOfMonth
     * @param endDayOfMonth
     * @return
     */
    List<String> existingSchedule(@Param("maId") Integer maId, @Param("beginDayOfMonth") String beginDayOfMonth, @Param("endDayOfMonth") String endDayOfMonth);

    /**
     * 查询当班所有排产单的已完成和正在运行总数
     *
     * @param maId
     * @param wsId
     * @param sdDate
     * @return
     */
    Integer getCompleteNum(Integer maId, Integer wsId, String sdDate);

    List<WorkbatchShiftVO> todyShift(Integer shiftStatus);

    /**
     * 获取当前时间所在班次最早开始时间之后的未下发、未接单、未生产中、未上报数据合计
     *
     * @param sdId
     * @param shiftStratTime
     * @return
     */
    Integer getNotReportedNum(Integer sdId, String shiftStratTime);

    /**
     * 根据工单id获取上报良品数据合计
     *
     * @param sdId
     * @return
     */
    Integer getReportedNum(Integer sdId);

    WorkbatchShiftVO getRepeat(@Param("sdId") Integer sdId, @Param("maId") Integer maId, @Param("wsId") Integer wsId, @Param("sdDate") String sdDate);

    List<WorkbatchShift> workbatchShiftBysdDate(@Param("todaySdDate")String todaySdDate,@Param("yesSdDate")String yesSdDate,@Param("beforSdDate")String beforSdDate);

    int setFinishNum(Integer wfid,Integer finishnum);

    List<TakeStockVO> selectByIds(IPage<TakeStockVO> page, List<Integer> wfIds, String wbNo, Integer maId, Integer prId, Integer exStatus);

    /**
     * 查询计划数(按班次分组)
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getWsSumPlanNum(@Param("getShiftreachVO") GetShiftreachVO getShiftreachVO);
    /**
     * 查询计划数
     * @param getShiftreachVO
     * @return
     */
    ShiftreachListVO getSumPlanNum(@Param("getShiftreachVO") GetShiftreachVO getShiftreachVO);

    /**
     * 查询总计划数(按设备,班次,日期分组)
     * @param getShiftreachVO
     * @return
     */
    List<ShiftreachListVO> getPlanShiftreachList(@Param("getShiftreachVO")GetShiftreachVO getShiftreachVO);

    WorkbatchShiftDataVO getShiftData(Integer wfId);
}
