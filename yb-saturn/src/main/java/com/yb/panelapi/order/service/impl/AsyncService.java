package com.yb.panelapi.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.*;
import com.yb.execute.mapper.ExecuteExamineMapper;
import com.yb.execute.mapper.ExecuteInfoMapper;
import com.yb.execute.mapper.ExecuteStateMapper;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.vo.ExecuteExamineIpcVO;
import com.yb.panelapi.common.SendMsgUtils;
import com.yb.panelapi.common.TempEntity;
import com.yb.panelapi.common.UpdateStateUtils;
import com.yb.panelapi.order.entity.OrderReportVo;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.panelapi.waste.mapper.PApiWasteMapper;
import com.yb.statis.controller.StatisLeanOeeController;
import com.yb.statis.dto.WorkBatchSortUpdateDTO;
import com.yb.statis.service.IStatisMachreachService;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.entity.SuperviseSchedule;
import com.yb.supervise.mapper.SuperviseBoxinfoMapper;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.supervise.mapper.SuperviseScheduleMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchProgress;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.mapper.*;
import com.yb.workbatch.request.WorkBatchSortUpdateRequest;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.vo.WorkbatchMachShiftVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springblade.common.constant.GlobalConstant;
import org.springblade.common.tool.ChatType;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/9/15 11:53
 */
@Component
@Slf4j
public class AsyncService {

    @Autowired
    private IStatisMachreachService iStatisMachreachService;
    @Autowired
    private StatisLeanOeeController statisLeanOeeController;
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftsetMapper workbatchShiftsetMapper;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;
    @Autowired
    private IExecuteInfoService executeInfoService;
    @Autowired
    private PApiWasteMapper wasteMapper;
    @Autowired
    private ExecuteExamineMapper examineMapper;
    @Autowired
    private SuperviseScheduleMapper scheduleMapper;
    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private WorkbatchOrdlinkMapper ordlinkMapper;
    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Autowired
    private SuperviseBoxinfoMapper superviseBoxinfoMapper;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private IStoreInlogService storeInlogService;

    private DecimalFormat df = new DecimalFormat("0.00");

    void report(WorkbatchShiftset shiftset, OrderReportVo reportVo, Date currTime, WorkbatchOrdlinkVO ordlinkVO, ExecuteBriefer briefer1, ExecuteExamineIpcVO examine1, Integer maId, SuperviseExecute executeOrder, Integer productNum, Integer sdId, ExecuteBriefer briefer, Integer exId, Integer bId, Integer isNext, WorkbatchOrdlink ordlink, WorkbatchShift workbatchShift) {
        log.info("开始异步处理上报信息:[mysql:{}]", DBIdentifier.getProjectCode());

        {
            if (isNext != null && isNext == 1) {//判断标识位是否为1,如果为1则调用
                isNext(reportVo);
            }

            /*删除多余的托盘,释放库位*/
            Integer wfId = ordlinkVO.getWfId();
            /*查询未打印过的标识卡*/
            List<Integer> NoPrintetIdList = executeTraycardMapper.getNoPrintEtIdList(wfId);
            if (!NoPrintetIdList.isEmpty()) {
                /*删除多余的托盘*/
//                executeTraycardService.removeByIds(etIdList);
                int deleteNum = executeTraycardMapper.deleteBatchIds(NoPrintetIdList);
                /*删除多余的台账*/
                storeInventoryService.deleteByEtIdList(NoPrintetIdList);
//                /*删除入库日志*/
//                storeInlogService.deleteNoEtIdList(etIdList);

                Integer totalNum = 0;
                /*查询剩余的托盘*/
                List<ExecuteTraycard> executeTraycardList =
                        executeTraycardMapper.selectList(new QueryWrapper<ExecuteTraycard>().eq("wf_id", wfId));
                if (!executeTraycardList.isEmpty()) {
                    totalNum = executeTraycardList.get(0).getTotalNum() - deleteNum;
                }
                Integer trayNo = totalNum;
                for (ExecuteTraycard executeTraycard : executeTraycardList) {
                    /*修改托盘总数和编号*/
                    executeTraycard.setTotalNum(totalNum);
                    executeTraycard.setTrayNo(trayNo);
                    executeTraycardMapper.updateById(executeTraycard);
                    trayNo--;
                }
            }

            ExecuteState oldState = stateMapper.selectById(briefer.getEsId());
            //执行状态表 生产结束上报
            ExecuteState state = new ExecuteState();
            //结束生产结束状态
            state.setStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
            state.setEvent(GlobalConstant.ProType.REPORT_EVENT.getType());
            state.setMaId(ordlinkVO.getMaId());   //设备id
            state.setSdId(sdId);   //排产id
            state.setTeamId(String.valueOf(oldState.getUsId()));
            state.setWbId(oldState.getWbId());   //批次id
            state.setUsId(examine1.getReportUserid());
            state.setStartAt(currTime);
            state.setCreateAt(currTime);
            state.setEndAt(currTime);
            UpdateStateUtils.reportUpdateSupervise(executeOrder, state, oldState);

//            ExecuteInfo executeInfo = executeInfoMapper.selectById(exId);

            //更新执行单表中的数据
            ExecuteInfo executeInfo = executeInfoService.getById(exId);
            if (executeInfo != null) {
                //上报完成执行修改状态操作
//                executeInfo.setEndTime(currTime);//D1执行完成状态
                executeInfo.setStatus(2);  //当前当次的排产生产完成。//0、接单1、执行中2、执行完成3、执行结束未上报
                executeInfoService.upFinishTime(exId, currTime);//结束的对象直接更新状态为3，
            }


//            if(wsId == null){
//                if (executeInfo != null) {//如果执行单存在则用执行单中日期,否则用当前日期
//                    wsId = executeInfo.getWsId();//班次id
//                } else {
//                    WorkbatchShift shift = workbatchShiftMapper.selectById(workbatchShift.getId());
//                        wsId = shift.getWsId();
//                    }
//            }

            /*修改主计划*/
            List<WorkbatchProgress> gressList =
                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
            if (!gressList.isEmpty()) {
                WorkbatchProgress workbatchProgress = gressList.get(0);
                String wbNo = workbatchProgress.getWbNo();//工单编号
                String ptName = workbatchProgress.getPtName();//部件名称
                /*查询部件*/
                List<WorkbatchProgress> workbatchProgressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
                        .eq("wb_no", wbNo).eq("pt_name", ptName).eq("wp_type", 2));
                /*查询编号*/
                List<WorkbatchProgress> progressList =
                        workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));

                Integer planCount = workbatchProgress.getPlanCount();//计划总数
                Double rate = 0.0;
                if (productNum < planCount) {
                    if (planCount != null && planCount != 0) {
                        rate = productNum / (double) planCount;
                    }
                    Integer totalTime = workbatchProgress.getTotalTime();//计划总时长
                    totalTime = totalTime == null ? 0 : totalTime;
                    Integer time = totalTime - (int) (totalTime * rate);//剩余时长
                    workbatchProgress.setStayTime(time);//剩余时长
                    workbatchProgress.setStatus(3);//部分完成
                } else {
                    workbatchProgress.setStatus(4);//全部完成
                    workbatchProgress.setStayTime(0);
                }

                if (!workbatchProgressList.isEmpty()) {
                    WorkbatchProgress progress = workbatchProgressList.get(0);
                    Integer realCount = progress.getRealCount();//已完成数
                    realCount = realCount == null ? 0 : realCount;
                    realCount += productNum;//本身的已完成数加上这次的完成数
                    progress.setRealCount(realCount);//修改已完成数
                    progress.setUpdateAt(new Date());
                    workbatchProgressMapper.updateById(progress);//修改部件主计划
                }
                if (!progressList.isEmpty()) {
                    WorkbatchProgress progress = progressList.get(0);
                    Integer realCount = progress.getRealCount();//已完成数
                    realCount = realCount == null ? 0 : realCount;
                    realCount += productNum;//本身的已完成数加上这次的完成数
                    progress.setRealCount(realCount);//修改已完成数
                    progress.setUpdateAt(new Date());
                    workbatchProgressMapper.updateById(progress);//修改工单编号主计划
                }
                workbatchProgress.setRealCount(productNum);//上报作业数
                workbatchProgress.setUpdateAt(new Date());
                workbatchProgressMapper.updateById(workbatchProgress);//修改工序的主计划
            }


            //废品表
            List<QualityBfwaste> wastes = reportVo.getWastes();
            if (!wastes.isEmpty()) {
                //保存废品
                for (QualityBfwaste reportWaste : wastes) {
                    if (reportWaste.getWasteNum() != 0) {
                        reportWaste.setBfId(briefer.getId());  //执行上报表id
                        reportWaste.setReportTime(currTime);   //上报时间
                        reportWaste.setMaId(maId);
                        reportWaste.setOrderId(ordlinkVO.getId());  //工单id
                        wasteMapper.insert(reportWaste);
                    }
                }
            }


            //todo 记录
            //插入订单进度表中每个工序的记录
            //根据批次id获取订单id
            Integer odId = ordlinkMapper.getOdIdByWbId(ordlink.getWbId());
            //查询当前工单是否有进度，
            SuperviseSchedule schedule = scheduleMapper.getSchedule(odId, ordlink.getWbId(),
                    ordlink.getPtId(), ordlink.getPrId(), ordlink.getMaId());
            if (schedule == null) {
                SuperviseSchedule newSchedule = new SuperviseSchedule();
                newSchedule.setOrderId(odId);     //订单id
                newSchedule.setBatchId(ordlink.getWbId());     //批次id
                newSchedule.setPtId(ordlink.getPtId());      //部件id
                newSchedule.setPrId(ordlink.getPrId());      //工序id
                newSchedule.setMId(ordlink.getMaId());      //设备id
                newSchedule.setReportNum(briefer1.getCountNum());    //生产数量
                newSchedule.setWasteNum(briefer1.getWasteNum());   //废品数量
                newSchedule.setStartTime(ordlink.getStartTime());
                newSchedule.setEndTime(currTime);         //结束时间
                //total百分比
                String total = df.format((float) briefer1.getCountNum() / ordlink.getPlanNum());
                newSchedule.setTotal(new BigDecimal(total)); //进度百分比
                scheduleMapper.insert(newSchedule);
            } else {
                schedule.setReportNum(briefer1.getCountNum() + schedule.getReportNum());   //生产数量
                schedule.setWasteNum(briefer1.getWasteNum() + schedule.getWasteNum());   //废品数量
                String total = df.format((float) schedule.getReportNum() / ordlink.getPlanNum());
                schedule.setTotal(new BigDecimal(total));   //进度百分比
                schedule.setEndTime(currTime);    //结束时间
                scheduleMapper.updateById(schedule);
            }

            //插入上报审核表数据，上报审核表
            // 上报不添加修改确认表
//            ExecuteExamine examine = new ExecuteExamine();
            //如果是下班上报 还需要再执行表中增加一条数据
//            examine.setRptUserid(ordlinkVO.getUsId());
//            examine.setBfId(briefer.getId());   // 执行上报表id
//            examine.setExStatus(0);   //没有审核
//            examine.setRptUserid(examine1.getReportUserid());
//            examine.setRptTime(currTime);
            //是否为下班上报  1 下班上报  2 换单上报
//            if (examine1.getRptType() == 1) {
//                examine.setRptType(1);  //下班上报
            //UpdateStateUtils.updateSupervise(state, null);
            //这里进行达成率的更新
            try {
                iStatisMachreachService.updateStatisMachreachObj(briefer.getId(), ordlink, workbatchShift);
            } catch (Exception e) {
                log.warn("达成率更新出现错误", e.getMessage());
            }
//            } else {
//                //换单上报
//                examine.setRptType(2);   //换单上报
            //这里进行达成率的更新
            try {
                iStatisMachreachService.updateStatisMachreachObj(briefer.getId(), ordlink, workbatchShift);
            } catch (Exception e) {
                log.warn("换单上报达成率更新出现错误", e.getMessage());
            }
//            }
            // TODO 这里也需要测试一下 然后分批次进行嵌入
            // 上报不添加修改确认表
//            if (examineMapper.insert(examine) <= 0) {
//                log.error("上报出现异常:[examine:{}]", examine);
//            }
            //Oee的统计分析
            //String targetDay = DateUtil.refNowDay();
            String targetDay = workbatchShift.getSdDate();
            Integer wsId = workbatchShift.getWsId();//班次id
            statisLeanOeeController.setMachoee(shiftset, maId, targetDay, wsId);
        }
    }

    /***
     * 处理继续新增数据内容。
     * @param reportVo
     */
    private void isNext(OrderReportVo reportVo) {
        WorkbatchOrdlinkVO ordlinkVO = reportVo.getOrdlinkVO();
//        ExecuteBriefer briefer = reportVo.getBriefer();
//        Integer productNum = briefer.getProductNum();
//        productNum = productNum == null ? 0 : productNum;
        Integer wfId = ordlinkVO.getWfId();
        Integer maId = ordlinkVO.getMaId();//设备id
        /*当前上报的排产班次信息*/
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        Integer finishNum = workbatchShift.getFinishNum() == null ? 0 : workbatchShift.getFinishNum();
        Integer speed = workbatchShift.getSpeed();
        String sdDate = null;
        Integer sdId = null;
        Integer wsId = null;
        Integer planNum = null;
        WorkbatchShiftset workbatchShiftset = null;
        if (workbatchShift != null) {
            planNum = workbatchShift.getPlanNum();
            planNum = planNum == null ? 0 : planNum;
            sdId = workbatchShift.getSdId();
            wsId = workbatchShift.getWsId();//班次id
            sdDate = workbatchShift.getSdDate();//排产日期
            /*根据班次id查询当前班次*/
            workbatchShiftset = workbatchShiftsetMapper.selectByMaid(wsId, maId);
        }
        Integer number = planNum - finishNum;//- productNum 剩余未完成数
        if (number <= 0) {
            return;
        }
        if (workbatchShiftset != null) {
            Date startTime = workbatchShiftset.getStartTime();//班次开始时间
            Date endTime = workbatchShiftset.getEndTime();//班次结束时间
            if (startTime.getTime() > endTime.getTime() && !StringUtil.isEmpty(sdDate)) {//如果开始时间大于结束时间说明是晚班,日期需要加一天
                sdDate = DateUtil.refNowDay(DateUtil.addDayForDate(DateUtil.changeDay(sdDate), 1));//加一天
            }
            /*根据设备班次id查询下个班次信息*/
            WorkbatchMachShiftVO workbatchMachShiftVO = null;
            if (wsId != null && maId != null) {
                workbatchMachShiftVO = workbatchShiftMapper.getNextWorkbatchShift(wsId, maId);
            }
            if (workbatchMachShiftVO != null) {
                wsId = workbatchMachShiftVO.getId();
                WorkbatchShift shift = null;
                try {
                    shift =
                            workbatchShiftMapper.selectOne(new QueryWrapper<WorkbatchShift>()
                                    .eq("sd_date", sdDate)
                                    .eq("ws_id", wsId)
                                    .eq("sd_id", sdId)
                                    .eq("ma_id", maId));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("同一天相同班次出现多个相同的sdId排产单");
                }
                /*查询移交的这天的这个班次是否有相同sdId的单子,如果有则数量相加,如果没有则新增*/
                if (shift != null) {
                    int num = shift.getPlanNum() != null ? shift.getPlanNum() : 0;
                    shift.setPlanNum(num + number);
                    shift.setUpdateAt(new Date());//修改时间
                    shift.setIsAuto(1);//自动接单标识符
                    shift.setStatus("1");
                    shift.setShiftStatus(0);
                    workbatchShiftMapper.updateById(shift);
                    wfSdSort(maId, wsId, sdDate, shift);
                } else {
                    String ckName = workbatchMachShiftVO.getCkName();
                    Date shiftVOStartTime = workbatchMachShiftVO.getStartTime();
                    Date shiftVOEndTime = workbatchMachShiftVO.getEndTime();
                    shiftVOStartTime = DateUtil.toDate(sdDate + " " + DateUtil.format(shiftVOStartTime, "HH:mm:ss"), null);
                    shiftVOEndTime = DateUtil.toDate(sdDate + " " + DateUtil.format(shiftVOEndTime, "HH:mm:ss"), null);
                    workbatchShift = new WorkbatchShift();
                    workbatchShift.setSpeed(speed);
                    workbatchShift.setMouldStay(0);
                    workbatchShift.setSdDate(sdDate);//排产日期
                    workbatchShift.setWsId(wsId);//班次id
                    workbatchShift.setStartTime(shiftVOStartTime);
                    workbatchShift.setStatus("1");
                    workbatchShift.setEndTime(shiftVOEndTime);
                    workbatchShift.setCkName(ckName);//班次名称
                    workbatchShift.setCreateAt(new Date());
                    workbatchShift.setPlanNum(number);
                    workbatchShift.setMaId(maId);
                    workbatchShift.setWasteNum(0);
                    workbatchShift.setShiftStatus(0);
                    workbatchShift.setSdId(sdId);
                    workbatchShift.setPlanType("1");
                    workbatchShift.setIsAuto(1);//自动接单标识符
                    workbatchShift.setUpdateAt(new Date());//创建时间
                    workbatchShiftMapper.insert(workbatchShift);
                    wfSdSort(maId, wsId, sdDate, workbatchShift);
                }
            }
        }
    }

    /**
     * 创建排序所需对象,把传人的对象放在第一个
     *
     * @param maId
     * @param wsId
     * @param sdDate
     * @param workbatchShift
     */
    public void wfSdSort(Integer maId, Integer wsId, String sdDate, WorkbatchShift workbatchShift) {
        /*查询该天该班次同设备下已排产的单子*/
        List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.getShiftByMaid(sdDate, wsId, maId);
        if (workbatchShiftList.isEmpty()) {
            workbatchShiftList = new ArrayList<>();
        }
        workbatchShiftList.add(0, workbatchShift);//把这个单子放在第一个
        creatWfSdSort(workbatchShiftList, maId, 0);
    }

    /**
     * 创建排序所需对象,重新进行排序
     *
     * @param workbatchShiftList
     * @param maId
     * @param n
     */
    public void creatWfSdSort(List<WorkbatchShift> workbatchShiftList, Integer maId, Integer n) {
        WorkbatchShift workbatchShift = null;
        if (!workbatchShiftList.isEmpty()) {
            workbatchShift = workbatchShiftList.get(0);
        }
        if (workbatchShift != null) {
            /*创建排序所需对象*/
            Integer wsId = workbatchShift.getWsId();
            String sdDate = workbatchShift.getSdDate();
            WorkBatchSortUpdateRequest workBatchSortUpdateRequest = new WorkBatchSortUpdateRequest();
            workBatchSortUpdateRequest.setWsId(wsId);
            workBatchSortUpdateRequest.setSdDate(sdDate);
            workBatchSortUpdateRequest.setMaId(maId);
            workBatchSortUpdateRequest.setType(0);//默认无下工序
            List<WorkBatchSortUpdateDTO> workBatchSortUpdateDTOS = new ArrayList<>();
            WorkBatchSortUpdateDTO workBatchSortUpdateDTO;
            Date changeDay = DateUtil.changeDay(sdDate);
            String mMdd = DateUtil.format(changeDay, "MMdd");
            for (WorkbatchShift shift : workbatchShiftList) {//处理排序所需对象
                Integer wkId = shift.getSdId();
                if (wkId == null) {
                    continue;
                }
                workBatchSortUpdateDTO = new WorkBatchSortUpdateDTO();
                workBatchSortUpdateDTO.setSdId(wkId);
                StringBuffer sdSort = new StringBuffer(mMdd);
                sdSort = sdSort.append("-");
                if (n < 10) {//排序格式
                    sdSort = sdSort.append("0");
                }
                sdSort = sdSort.append(n);
                workBatchSortUpdateDTO.setSdSort(sdSort.toString());
                workBatchSortUpdateDTOS.add(workBatchSortUpdateDTO);
                n++;
            }
            workBatchSortUpdateRequest.setWorkBatchSortUpdateDTOS(workBatchSortUpdateDTOS);
            workbatchOrdlinkNewService.updateWorkBatchSort(workBatchSortUpdateRequest);//调排序接口
        }

    }


}
