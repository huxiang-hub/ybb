package com.yb.panelapi.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.common.DateUtil;
import com.yb.common.constant.OrderStatusConstant;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.mapper.*;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteExamineIpcVO;
import com.yb.order.mapper.OrderOrdinfoMapper;
import com.yb.panelapi.order.entity.OrderReportNewVO;
import com.yb.panelapi.order.entity.OrderReportVo;
import com.yb.panelapi.order.service.IPApiOrderReportService;
import com.yb.panelapi.user.utils.R;
import com.yb.panelapi.waste.mapper.PApiWasteMapper;
import com.yb.panelapi.waste.vo.QualityBfwasteVO;
import com.yb.statis.controller.StatisLeanOeeController;
import com.yb.statis.dto.WorkBatchSortUpdateDTO;
import com.yb.statis.service.IStatisMachreachService;
import com.yb.statis.service.IStatisMachsingleService;
import com.yb.statis.service.IStatisOrdsingleService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.vo.InInventoryByNumberVO;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.supervise.mapper.SuperviseScheduleMapper;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.entity.WorkbatchShiftset;
import com.yb.workbatch.entity.WorkbatchUnlock;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchProgressMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.mapper.WorkbatchUnlockMapper;
import com.yb.workbatch.request.WorkBatchSortUpdateRequest;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import com.yb.workbatch.vo.AnewWfsDSortVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Service
@Slf4j
public class PApiOrderReportServiceImpl implements IPApiOrderReportService {

    @Autowired
    private PApiWasteMapper wasteMapper;
    @Autowired
    private ExecuteBrieferMapper brieferMapper;
    @Autowired
    private ExecuteExamineMapper examineMapper;
    @Autowired
    private SuperviseScheduleMapper scheduleMapper;
    @Autowired
    private ExecuteStateMapper stateMapper;
    @Autowired
    private WorkbatchOrdlinkMapper ordlinkMapper;
    @Autowired
    private SuperviseExecuteMapper executeMapper;
    @Autowired
    private IStatisMachsingleService machsingleService;
    @Autowired
    private IStatisOrdsingleService iStatisOrdsingleService;
    @Autowired
    private IStatisMachreachService iStatisMachreachService;
    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;
    @Autowired
    private ExecuteInfoMapper executeInfoMapper;
    @Autowired
    private StatisLeanOeeController statisLeanOeeController;
    @Autowired
    private WorkbatchProgressMapper workbatchProgressMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;
    @Autowired
    private WorkbatchUnlockMapper workbatchUnlockMapper;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private IWorkbatchShiftsetService iWorkbatchShiftsetService;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private IExecuteWasteService exewasteService;

    private DecimalFormat df = new DecimalFormat("0.00");


    /**
     * 上报
     *
     * @param reportVo
     * @return
     */
    @Override
    public R orderReport(OrderReportVo reportVo) {
        String projectCode = DBIdentifier.getProjectCode();
        log.info("开始上报数据:[mysql:{}]", projectCode);
        Date currTime = new Date();//执行操作时间
        WorkbatchOrdlinkVO ordlinkVO = reportVo.getOrdlinkVO();
        Integer isNext = reportVo.getIsNext();
        ExecuteBriefer briefer1 = reportVo.getBriefer();   //执行表上报数据
        ExecuteExamineIpcVO examine1 = reportVo.getExamine();   //上报审核表数据
        Integer maId = ordlinkVO.getMaId();  //设备id4
        //根据设备id查询设备状态表数据
        SuperviseExecute executeOrder = executeMapper.getExecuteOrder(maId);
        Integer productNum = briefer1.getProductNum();
        //排产id
        Integer sdId = reportVo.getOrdlinkVO().getId();
        ExecuteBriefer briefer = brieferMapper.selectById(reportVo.getBid());
        briefer.setStatus(briefer1.getStatus());   //生产完成状态
        briefer.setProductNum(productNum);  //作业数量
        briefer.setCountNum(briefer1.getCountNum());  //良品数
        briefer.setWasteNum(briefer1.getWasteNum());  //废品数量
        briefer.setUpwasteNum(briefer1.getUpwasteNum());//上工序废品数
        briefer.setUppwasteNum(briefer1.getUppwasteNum());//上上工序废品数
        briefer.setHandle(1);  //已上报处理
        briefer.setHandleTime(currTime);   //订单处理上报结束时间
        briefer.setHandleUsid(examine1.getReportUserid());   //上报人员
        briefer.setUsIds(ordlinkVO.getUsIds());    //所有生产人员
        briefer.setSdId(sdId);
        //返回执行上报表的id
        if (brieferMapper.updateById(briefer) <= 0) {
            return R.error("保存失败");
        }

        //判断当前工单的已被结束
        WorkbatchOrdlink ordlink = ordlinkMapper.selectById(ordlinkVO.getId());
        ordlink = (ordlink == null) ? new WorkbatchOrdlink() : ordlink;
        //更新完成数量
        if (ordlink.getCompleteNum() == null) {
            ordlink.setCompleteNum(0);
        }
        if (briefer.getProductNum() == null) {
            briefer.setProductNum(0);
        }
        ordlink.setCompleteNum(ordlink.getCompleteNum() + briefer.getCountNum());
        //未完成数量
        if (ordlink.getPlanNum() == null) {
            ordlink.setPlanNum(0);
        }
        ordlink.setIncompleteNum(ordlink.getPlanNum() - ordlink.getCompleteNum());
        if (ordlink.getWaste() == null) {
            ordlink.setWaste(0);
        }
        if (briefer.getWasteNum() == null) {
            briefer.setWasteNum(0);
        }
        ordlink.setWaste(ordlink.getWaste() + briefer.getWasteNum());  //废品数
        //更新排产表信息
        ordlink.setEndTime(currTime);  //当前结束
        //如果完成数大于等于计算数，表示该工单生产完成
        if (briefer1.getStatus() == 1 ||
                ordlink.getCompleteNum() >= ordlink.getPlanNum()) {   //当前工单已完成
            ordlink.setStatus(OrderStatusConstant.STATUS_COMPLETE);
            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_COMPLETE);
            ordlink.setEndTime(new Date());
        } else {
            // [新排产]之前1，现在9]
            ordlink.setStatus("9");   //重新接单
            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_WAITING);
        }
        //更新排产信息状态
        Integer wfId = ordlinkVO.getWfId();
        List<ExecuteBriefer> executeBrieferList =
                brieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId).notIn("id", reportVo.getBid()));
        Integer num = briefer.getProductNum();
        for (ExecuteBriefer executeBriefer : executeBrieferList) {
            Integer brieferProductNum = executeBriefer.getProductNum();
            brieferProductNum = brieferProductNum == null ? 0 : brieferProductNum;
            num += brieferProductNum;
        }
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        if (workbatchShift != null) {
            if (num < workbatchShift.getPlanNum() && briefer1.getStatus() != 1) {
                //未完成已上报
                workbatchShift.setShiftStatus(4);
                // [新排产]之前9，现在1
                workbatchShift.setStatus("1");
            } else {
                //已完成
                workbatchShift.setShiftStatus(2);
                workbatchShift.setStatus(OrderStatusConstant.STATUS_COMPLETE);
            }
            workbatchShift.setFinishNum(workbatchShift.getFinishNum() + reportVo.getBriefer().getCountNum());
            workbatchShiftMapper.updateById(workbatchShift);
        }
        if (ordlinkMapper.updateById(ordlink) <= 0) {
            log.warn("上报出现异常:[ordlink:{}]", ordlink);
        }

        WorkbatchShiftset workbatchShiftset = iWorkbatchShiftsetService.selectByMaid(workbatchShift.getWsId(), maId);

        Integer exId = briefer.getExId(); //执行单id信息;上报表进行修改
        WorkbatchOrdlink finalOrdlink = ordlink;
        new Thread(() -> {
            //防止数据库连接中断
            DBIdentifier.setProjectCode(projectCode);
            asyncService.report(workbatchShiftset, reportVo, currTime, ordlinkVO, briefer1, examine1, maId, executeOrder,
                    productNum, sdId, briefer, exId, reportVo.getBid(), isNext, finalOrdlink, workbatchShift);
        }).start();

        log.info("上报审核进行待排数量计算，执行修改数据操作；---------------------sdId：");
        workbatchOrdlinkNewService.setWorksNum(workbatchShift.getSdId(), workbatchShift.getMaId(), null);

        if (briefer.getCountNum() != 0) {
            InInventoryByNumberVO inInventoryRequest = new InInventoryByNumberVO();
            inInventoryRequest.setExId(exId);
            inInventoryRequest.setNumber(briefer.getCountNum());
            inInventoryRequest.setStType(1);
            storeInventoryService.inInventoryByNumber(inInventoryRequest);
        }
        return R.ok();
    }


    /**
     * 上报【重新上报程序】
     *
     * @param reportVo
     * @return
     */
    @Override
    public R orderReportNew(OrderReportNewVO reportVo) {
        String projectCode = DBIdentifier.getProjectCode();
        log.info("开始上报数据:[mysql:{}]", projectCode);

        ExecuteBriefer exBriefer = null;
        //更新排产数据
        WorkbatchShift shiftinfo = setWorkbatchShift(reportVo);
        if (shiftinfo == null || shiftinfo.getId() == null)
            return R.error("没有该上报单数据信息，请核对上报清单数据");

        Integer shiftStatus = (shiftinfo != null) ? shiftinfo.getShiftStatus() : 4;//获取对应的数据状态，默认状态为4-已上报未完成
        //修改bf表和执行单的info表相关信息
        R st = setBrieferInfo(reportVo, shiftStatus);
        if (st.get("code") == null || (Integer) st.get("code") != 200) {
            return R.error("上报表更新错误，请联系管理员");
        } else {
            exBriefer = (st != null && (Integer) st.get("code") == 200) ? (ExecuteBriefer) st.get("data") : null;
        }
        //执行废品上报表内容信息
        setWasteInfo(reportVo, exBriefer.getExId());

        //出入库
        log.info("上报数据，执行半成品出入库库inoutNum；---------------------exId：");
        storeInvent(exBriefer);//半成品出入库操作
        log.info("上报审核进行待排数量计算，执行修改数据操作；---------------------sdId：");
        workbatchOrdlinkNewService.setWorksNum(exBriefer.getSdId(), reportVo.getMaId(), null);
        return R.ok();
    }

    /****
     * 设置废品数量信息
     * @param reportVo
     * @author by wyn
     */
    private void setWasteInfo(OrderReportNewVO reportVo, Integer exId) {
        List<QualityBfwasteVO> wastels = reportVo.getWastes();
        ArrayList<ExecuteWaste> executeWastes = new ArrayList<>();
        Date currDate = new Date();
        if (wastels != null && wastels.size() > 0) {
            for (QualityBfwasteVO wastedb : wastels) {
                ExecuteWaste waste = new ExecuteWaste();
                waste.setWsType("4");//质检类型1\订单巡检2\车间巡检3\公司巡检\0自检\4上报废品
                waste.setExId(exId);
                waste.setExPrid(wastedb.getExPrid());//设定为本道工序的工序id信息
                waste.setPrId(wastedb.getPrId());//设置废品上面的工序
                waste.setMfId(wastedb.getWasteType());//设置本工序废品类型
                waste.setWaste(wastedb.getWasteNum());//设置废品数量
                waste.setUsId(reportVo.getUsId());
                waste.setCreateAt(currDate);
                executeWastes.add(waste);
            }
            exewasteService.saveBatch(executeWastes);//批量执行新增数据
        }
    }

    /****
     * 更新上报表数据信息
     * @param reportVo
     * @param shiftStatus
     * @author by wyn
     */
    private R setBrieferInfo(OrderReportNewVO reportVo, Integer shiftStatus) {
        //更新上报表数据
        ExecuteBriefer exBriefer = brieferMapper.getBybfId(reportVo.getBfId());
        if (exBriefer == null || exBriefer.getId() == null) {
            return R.error("上报数据不存在，返回管理查找；");
        }
        Date currtime = new Date();
        //更新对应上报表数据信息
        exBriefer.setProductNum(reportVo.getProductNum());//设置上报作业数（机台默认为盒子数）
        exBriefer.setCountNum(reportVo.getCountNum());//设置上报良品数据
        exBriefer.setWasteNum(reportVo.getWasteNum());//设置上报废品数据
        exBriefer.setHandle(1);//0未上报1已上报 设置已上报状态
        exBriefer.setHandleTime(currtime);
        exBriefer.setHandleUsid(reportVo.getUsId());//执行上报人员，操作人员信息//一般默认为第一个登录人员，按照机长、助理、机员级别进行执行操作人
        exBriefer.setUsIds(reportVo.getUsIds());//设定对应的执行班组的人员信息，中间用逗号分隔保存
        //Integer shiftStatus = (shiftinfo != null) ? shiftinfo.getShiftStatus() : 4; //，2：生产完成 4：未完成（已上报）默认状态
        if (shiftStatus != null && 2 == shiftStatus) {
            exBriefer.setStatus(1);//生产状态：1 已完成0未完成;
        } else if (4 == shiftStatus) {
            //如果不是2就表示还未完成该排程单信息
            exBriefer.setStatus(0);//生产状态：1 已完成0未完成;
        }
        //设定修改上报表对应的数据进行修改操作；
        if (brieferMapper.updateById(exBriefer) <= 0) {
            return R.error("上报表更新错误，请联系管理员");
        }

        //更新执行表的状态信息内容为2执行完成
        ExecuteInfo exinfo = executeInfoMapper.selectById(exBriefer.getExId());
        if (exinfo != null && exinfo.getId() != null) {
            exinfo.setStatus(2);//执行状态：状态：0、接单1、执行中2、执行完成3、执行结束
            exinfo.setUpdateAt(currtime);//最后更新时间
            executeInfoMapper.updateById(exinfo);
        }
        return R.ok(exBriefer);
    }

    /*****
     * 根据上报数据执行半成品出入库操作
     * @param exBriefer
     * @author by wyn
     */
    private void storeInvent(ExecuteBriefer exBriefer) {
        //库存管理操作
        if (exBriefer != null && exBriefer.getProductNum() != null) {
            //上道工序的作业数作为出库数据进行半成品恒定。
            //出库出上工序的
            Integer upSdId = workbatchOrdlinkMapper.getUpProcessSdIdByWfId(exBriefer.getWfId());
            storeInventoryService.outInventory(upSdId, exBriefer.getProductNum());
        }
        //进行入库管理操作
        if (exBriefer != null && exBriefer.getCountNum() != 0) {
            InInventoryByNumberVO inInventoryRequest = new InInventoryByNumberVO();
            inInventoryRequest.setExId(exBriefer.getExId());
            inInventoryRequest.setNumber(exBriefer.getCountNum());
            inInventoryRequest.setStType(1); //设定为半成品类型
            storeInventoryService.inInventoryByNumber(inInventoryRequest);
        }
    }

    /****
     * 根据上报数据，进行排产工单状态的更新操作；
     * @param reportVo
     * @author by wyn
     */
    private WorkbatchShift setWorkbatchShift(OrderReportNewVO reportVo) {
        //当有自动上报需要更新到排程表中去。
        WorkbatchShift shiftinfo = workbatchShiftMapper.getWbshiftByBfId(reportVo.getBfId());
        if (shiftinfo == null || shiftinfo.getId() == null) {
            return null;
        }

        //返回上报数据进行数据更新内容
        if (shiftinfo != null && shiftinfo.getId() != null) {
            if (reportVo != null && reportVo.getIsAuto() != null && 1 == reportVo.getIsAuto()) {
                shiftinfo.setIsAuto(1);//设定自动化数据内容
            }
        }
        //更新完成数据信息
        Integer finishnum = (shiftinfo != null && shiftinfo.getFinishNum() != null) ? shiftinfo.getFinishNum() : 0;
        Integer countnum = (reportVo.getCountNum() != null) ? reportVo.getCountNum() : 0;
        finishnum = finishnum + countnum;//上报的良品数据累加到已完成数量中
        //计划作业数（包含最大报废数，所以要减去最大报废数）得到生产良品数
        Integer plannum = (shiftinfo != null && shiftinfo.getPlanNum() != null) ? shiftinfo.getPlanNum() : 0;
        Integer wasteNum = (shiftinfo != null && shiftinfo.getWasteNum() != null) ? shiftinfo.getWasteNum() : 0;
        //判断上报数据是否已经达到完成数据：比较良品数据达到完成状态
        if ((plannum - wasteNum) <= finishnum) {
            shiftinfo.setShiftStatus(2);//0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
            //已完成
            shiftinfo.setStatus(OrderStatusConstant.STATUS_COMPLETE);
        } else {
            shiftinfo.setShiftStatus(4);//已上报，未完成
            // [新排产]之前9，现在1
            shiftinfo.setStatus("1");
        }
        shiftinfo.setFinishNum(finishnum);//更新完成数据
        workbatchShiftMapper.updateById(shiftinfo);//更新班次信息内容
        return shiftinfo;
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean anewWfsDSort(AnewWfsDSortVO anewWfsDSortVO) {

        Integer maId = anewWfsDSortVO.getMaId();//设备id
        Integer wsId = anewWfsDSortVO.getWsId();//班次id
        String wsSdDate = anewWfsDSortVO.getWsSdDate();//排产日期
        Integer preWfId = anewWfsDSortVO.getPreWfId();//前排产单id
        Integer suffixWfId = anewWfsDSortVO.getSuffixWfId();//后排产单id
        Integer isSwap = anewWfsDSortVO.getIsSwap();//调整顺序方式, 是否交换1是交换2是插入

        Date changeDay = DateUtil.changeDay(wsSdDate);
        String mMdd = DateUtil.format(changeDay, "MMdd");
        /*查询该设备,日期班次下的可接排产单*/
        List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectSdSortList(maId, wsId, wsSdDate);
        WorkbatchShift preWorkbatchShift = workbatchShiftMapper.selectById(preWfId);//前一个单子
        WorkbatchShift suffixWorkbatchShift = workbatchShiftMapper.selectById(suffixWfId);//后一个单子
        if (preWorkbatchShift == null || suffixWorkbatchShift == null) {
            return false;
        }
        String preSdSort = preWorkbatchShift.getSdSort();//前一个单子的排序
        String suffixSdSort = suffixWorkbatchShift.getSdSort();//后一个单子的排序
        if (preSdSort.compareTo(suffixSdSort) > 0) {//如果前端传入的前一个单子排序大于后一个则交换顺序
            String sort = suffixSdSort;
            suffixSdSort = preSdSort;
            preSdSort = sort;
            Integer wfId = suffixWfId;
            suffixWfId = preWfId;
            preWfId = wfId;
        }
//        Integer n = 1;
        Integer i = 0;
        Integer sort;
        Boolean status = false;
        for (WorkbatchShift workbatchShift : workbatchShiftList) {
            String WfSdSort = workbatchShift.getSdSort();
            sort = Integer.valueOf(WfSdSort.substring(5));
//            if(i == 0){//第一个排序的sort
//                n = sort;
//            }
            Integer shiftId = workbatchShift.getId();
            if (isSwap.equals(1)) {//交换
                if (preWfId.equals(shiftId)) {
                    i++;
                    workbatchShift.setSdSort(suffixSdSort);
                    workbatchShiftMapper.updateById(workbatchShift);//修改前一个单子排序到后一个单子
                }
                if (suffixWfId.equals(shiftId)) {
                    i++;
                    workbatchShift.setSdSort(preSdSort);
                    workbatchShiftMapper.updateById(workbatchShift);//修改后一个单子排序到前一个单子
                    break;
                }
            }
            if (isSwap.equals(2)) {//插入
                if (preWfId.equals(shiftId)) {//如果找到插入位置,后面向后挪
                    status = true;
                    sort++;//向后挪一位
                }
                if (status) {//后面依次挪一位
                    i++;
                    if (!shiftId.equals(suffixWfId)) {
                        StringBuffer sdSort = new StringBuffer(mMdd);
                        sdSort = sdSort.append("-");
                        if (sort < 10) {//排序格式
                            sdSort = sdSort.append("0");
                        }
                        sdSort = sdSort.append(sort);
                        workbatchShift.setSdSort(sdSort.toString());
                    } else {
                        workbatchShift.setSdSort(preSdSort);
                        workbatchShiftMapper.updateById(workbatchShift);
                        break;
                    }
                    workbatchShiftMapper.updateById(workbatchShift);
                }
            }
        }
        /*查询所有已保存排序的排产单*/
        workbatchShiftList = workbatchShiftMapper.selectworkbatchShiftList(maId, wsId, wsSdDate);
        creatWfSdSort(workbatchShiftList, maId, 1);
        /*调顺序日志表对象*/
        WorkbatchUnlock workbatchUnlock = new WorkbatchUnlock();
        Integer usId = anewWfsDSortVO.getUsId();//操作人id
        String userName = anewWfsDSortVO.getUserName();//操作人姓名
        workbatchUnlock.setUsId(usId);
        workbatchUnlock.setUsName(userName);
        workbatchUnlock.setWfId(suffixWfId);//被调整排产单
        workbatchUnlock.setBeforeSort(suffixSdSort);//调整前顺序
        workbatchUnlock.setAfterSort(preSdSort);//调整后顺序
        workbatchUnlock.setSwapWfid(preWfId);//调整对象wfid
        workbatchUnlock.setSwapSort(preSdSort);//调整对象之前顺序
        workbatchUnlock.setSwapNum(i);//调整记录数量
        workbatchUnlock.setIsSwap(isSwap);//是否交换1是交换2是插入
        Boolean aBoolean = addUnLockLog(workbatchUnlock);//新增调整顺序记录
        if (!aBoolean) {
            return false;
        }
        return true;
    }

    /**
     * 新增调整顺序记录
     *
     * @param workbatchUnlock
     * @return
     */
    public Boolean addUnLockLog(WorkbatchUnlock workbatchUnlock) {
        /*SaUser user = SaSecureUtil.getUser();
        Integer userId = null;
        String userName = null;
        if (user != null) {
            userId = user.getUserId();//用户id
            userName = user.getName();//用户名

        }*/
    /*    workbatchUnlock.setUsId(userId);
        workbatchUnlock.setUsName(userName);*/
        workbatchUnlock.setCreateAt(new Date());
        return workbatchUnlockMapper.insert(workbatchUnlock) > 0 ? true : false;
    }

    /**
     * 采用异步处理上报后续存储
     *
     * @param reportVo
     * @param currTime
     * @param ordlinkVO
     * @param briefer1
     * @param examine1
     * @param maId
     * @param executeOrder
     * @param productNum
     * @param sdId
     * @param briefer
     * @param exId
     * @param bId
     */
//    void report(OrderReportVo reportVo, Date currTime, WorkbatchOrdlinkVO ordlinkVO, ExecuteBriefer briefer1, ExecuteExamine examine1, Integer maId, SuperviseExecute executeOrder, Integer productNum, Integer sdId, ExecuteBriefer briefer, Integer exId, Integer bId, Integer isNext) {
//        if (isNext != null && isNext == 1) {//判断标识位是否为1,如果为1则调用
//            isNext(reportVo);
//        }
//
//        ExecuteInfo executeInfo = executeInfoMapper.selectById(exId);
//        String targetDay;
//        Integer wsId;//班次id
//        if (executeInfo != null) {//如果执行单存在则用执行单中日期,否则用当前日期
//            targetDay = executeInfo.getTargetDay();
//            wsId = executeInfo.getWsId();//班次id
//        } else {
//            targetDay = DateUtil.refNowDay();
//            List<WorkbatchShift> workbatchShiftList = workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("sd_id", sdId));
//            if (!workbatchShiftList.isEmpty()) {
//                wsId = workbatchShiftList.get(0).getWsId();
//            } else {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//                String s = simpleDateFormat.format(new Date());
//                wsId = workbatchShiftMapper.findByMaIdWsTime(maId, s).getId();
//            }
//        }
//        /*修改主计划*/
//        List<WorkbatchProgress> gressList =
//                workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("sd_id", sdId).eq("wp_type", 3));
//        if (!gressList.isEmpty()) {
//            WorkbatchProgress workbatchProgress = gressList.get(0);
//            String wbNo = workbatchProgress.getWbNo();//工单编号
//            String ptName = workbatchProgress.getPtName();//部件名称
//            /*查询部件*/
//            List<WorkbatchProgress> workbatchProgressList = workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>()
//                    .eq("wb_no", wbNo).eq("pt_name", ptName).eq("wp_type", 2));
//            /*查询编号*/
//            List<WorkbatchProgress> progressList =
//                    workbatchProgressMapper.selectList(new QueryWrapper<WorkbatchProgress>().eq("wb_no", wbNo).eq("wp_type", 1));
//
//            Integer planCount = workbatchProgress.getPlanCount();//计划总数
//            Double rate = 0.0;
//            if (productNum < planCount) {
//                if (planCount != null && planCount != 0) {
//                    rate = productNum / (double) planCount;
//                }
//                Integer totalTime = workbatchProgress.getTotalTime();//计划总时长
//                totalTime = totalTime == null ? 0 : totalTime;
//                Integer time = totalTime - (int) (totalTime * rate);//剩余时长
//                workbatchProgress.setStayTime(time);//剩余时长
//                workbatchProgress.setStatus(3);//部分完成
//            } else {
//                workbatchProgress.setStatus(4);//全部完成
//                workbatchProgress.setStayTime(0);
//            }
//
//            if (!workbatchProgressList.isEmpty()) {
//                WorkbatchProgress progress = workbatchProgressList.get(0);
//                Integer realCount = progress.getRealCount();//已完成数
//                realCount = realCount == null ? 0 : realCount;
//                realCount += productNum;//本身的已完成数加上这次的完成数
//                progress.setRealCount(realCount);//修改已完成数
//                progress.setUpdateAt(new Date());
//                workbatchProgressMapper.updateById(progress);//修改部件主计划
//            }
//            if (!progressList.isEmpty()) {
//                WorkbatchProgress progress = progressList.get(0);
//                Integer realCount = progress.getRealCount();//已完成数
//                realCount = realCount == null ? 0 : realCount;
//                realCount += productNum;//本身的已完成数加上这次的完成数
//                progress.setRealCount(realCount);//修改已完成数
//                progress.setUpdateAt(new Date());
//                workbatchProgressMapper.updateById(progress);//修改工单编号主计划
//            }
//            workbatchProgress.setRealCount(productNum);//上报作业数
//            workbatchProgress.setUpdateAt(new Date());
//            workbatchProgressMapper.updateById(workbatchProgress);//修改工序的主计划
//        }
//
//
//        ExecuteState oldState = stateMapper.selectById(briefer.getEsId());
//        //执行状态表 生产结束上报
//        ExecuteState state = new ExecuteState();
//        //结束生产结束状态
//        state.setStatus(GlobalConstant.ProType.AFTERPRO_STATUS.getType());
//        state.setEvent(GlobalConstant.ProType.REPORT_EVENT.getType());
//        state.setMaId(ordlinkVO.getMaId());   //设备id
//        state.setSdId(sdId);   //排产id
//        state.setTeamId(String.valueOf(oldState.getUsId()));
//        state.setWbId(oldState.getWbId());   //批次id
//        state.setUsId(examine1.getReportUserid());
//        state.setStartAt(currTime);
//        state.setCreateAt(currTime);
//        state.setEndAt(currTime);
//        UpdateStateUtils.reportUpdateSupervise(state, oldState);
//
//        //获取当前的排产工单信息
//        //判断当前工单的已被结束
//        WorkbatchOrdlink ordlink = ordlinkMapper.selectById(ordlinkVO.getId());
//        //更新完成数量
//        if (ordlink.getCompleteNum() == null) {
//            ordlink.setCompleteNum(0);
//        }
//        if (briefer.getProductNum() == null) {
//            briefer.setProductNum(0);
//        }
//        ordlink.setCompleteNum(ordlink.getCompleteNum() + briefer.getProductNum());
//        //未完成数量
//        if (ordlink.getPlanNum() == null) {
//            ordlink.setPlanNum(0);
//        }
//        ordlink.setIncompleteNum(ordlink.getPlanNum() - ordlink.getCompleteNum());
//        if (ordlink.getWaste() == null) {
//            ordlink.setWaste(0);
//        }
//        if (briefer.getWasteNum() == null) {
//            briefer.setWasteNum(0);
//        }
//        ordlink.setWaste(ordlink.getWaste() + briefer.getWasteNum());  //废品数
//        //更新排产表信息
//        ordlink.setEndTime(currTime);  //当前结束
//        //如果完成数大于等于计算数，表示该工单生产完成
//        if (briefer1.getStatus() == 1 ||
//                ordlink.getCompleteNum() >= ordlink.getPlanNum()) {   //当前工单已完成
//            ordlink.setStatus(OrderStatusConstant.STATUS_COMPLETE);
//            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_COMPLETE);
//            ordlink.setEndTime(new Date());
//        } else {
//            ordlink.setStatus(OrderStatusConstant.STATUS_WAITING);   //重新接单
//            ordlink.setRunStatus(OrderStatusConstant.RUN_STATUS_WAITING);
//        }
//        //更新排产信息状态
//        Integer wfId = ordlinkVO.getWfId();
//        List<ExecuteBriefer> executeBrieferList =
//                brieferMapper.selectList(new QueryWrapper<ExecuteBriefer>().eq("wf_id", wfId).notIn("id", bId));
//        Integer num = briefer.getProductNum();
//        for (ExecuteBriefer executeBriefer : executeBrieferList) {
//            Integer brieferProductNum = executeBriefer.getProductNum();
//            brieferProductNum = brieferProductNum == null ? 0 : brieferProductNum;
//            num += brieferProductNum;
//        }
//        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
//        if (workbatchShift != null) {
//            if (num < workbatchShift.getPlanNum() && briefer1.getStatus() != 1) {
//                //未完成已上报
//                workbatchShift.setShiftStatus(4);
//                workbatchShift.setStatus(OrderStatusConstant.STATUS_WAITING);
//            } else {
//                //已完成
//                workbatchShift.setShiftStatus(2);
//                workbatchShift.setStatus(OrderStatusConstant.STATUS_COMPLETE);
//            }
//            workbatchShift.setFinishNum(num);
//            workbatchShiftMapper.updateById(workbatchShift);
//        }
//
//        if (ordlinkMapper.updateById(ordlink) <= 0) {
//            log.warn("上报出现异常:[ordlink:{}]", ordlink);
//        }
//        //是否还有未完成排产单
//        List<WorkbatchOrdlink> undoneOrdLink = ordlinkMapper.selectList(new QueryWrapper<WorkbatchOrdlink>().ne("status", "3"));
//        if (undoneOrdLink == null) {
//            //所有排产已完成修改订单为已完成
//            OrderOrdinfo order = orderOrdinfoMapper.selectOne(new QueryWrapper<OrderOrdinfo>().eq("od_no", ordlink.getOdNo()));
//            order.setProductionState(OrderStatusConstant.RUN_STATUS_COMPLETE);
//            orderOrdinfoMapper.updateById(order);
//        }
//        //废品表
//        List<QualityBfwaste> wastes = reportVo.getWastes();
//        //保存废品
//        for (QualityBfwaste reportWaste : wastes) {
//            if (reportWaste.getWasteNum() != 0) {
//                reportWaste.setBfId(briefer.getId());  //执行上报表id
//                reportWaste.setReportTime(currTime);   //上报时间
//                reportWaste.setMaId(maId);
//                reportWaste.setOrderId(ordlinkVO.getId());  //工单id
//                wasteMapper.insert(reportWaste);
//            }
//        }
//
//        //todo 记录
//        //插入订单进度表中每个工序的记录
//        //根据批次id获取订单id
//        Integer odId = ordlinkMapper.getOdIdByWbId(ordlink.getWbId());
//        //查询当前工单是否有进度，
//        SuperviseSchedule schedule = scheduleMapper.getSchedule(odId, ordlink.getWbId(),
//                ordlink.getPtId(), ordlink.getPrId(), ordlink.getMaId());
//        if (schedule == null) {
//            SuperviseSchedule newSchedule = new SuperviseSchedule();
//            newSchedule.setOrderId(odId);     //订单id
//            newSchedule.setBatchId(ordlink.getWbId());     //批次id
//            newSchedule.setPtId(ordlink.getPtId());      //部件id
//            newSchedule.setPrId(ordlink.getPrId());      //工序id
//            newSchedule.setMId(ordlink.getMaId());      //设备id
//            newSchedule.setReportNum(briefer1.getCountNum());    //生产数量
//            newSchedule.setWasteNum(briefer1.getWasteNum());   //废品数量
//            newSchedule.setStartTime(ordlink.getStartTime());
//            newSchedule.setEndTime(currTime);         //结束时间
//            //total百分比
//            String total = df.format((float) briefer1.getCountNum() / ordlink.getPlanNum());
//            newSchedule.setTotal(new BigDecimal(total)); //进度百分比
//            scheduleMapper.insert(newSchedule);
//        } else {
//            schedule.setReportNum(briefer1.getCountNum() + schedule.getReportNum());   //生产数量
//            schedule.setWasteNum(briefer1.getWasteNum() + schedule.getWasteNum());   //废品数量
//            String total = df.format((float) schedule.getReportNum() / ordlink.getPlanNum());
//            schedule.setTotal(new BigDecimal(total));   //进度百分比
//            schedule.setEndTime(currTime);    //结束时间
//            scheduleMapper.updateById(schedule);
//        }
//
//        //插入上报审核表数据，上报审核表
//        ExecuteExamine examine = new ExecuteExamine();
//        //如果是下班上报 还需要再执行表中增加一条数据
//        examine.setReportUserid(ordlinkVO.getUsId());
//        examine.setBfId(briefer.getId());   // 执行上报表id
//        examine.setExamineStatus(0);   //没有审核
//        examine.setReportUserid(examine1.getReportUserid());
//        examine.setReportTime(currTime);
//        //是否为下班上报  1 下班上报  2 换单上报
//        if (examine1.getReportType() == 1) {
//            state.setStatus(GlobalConstant.ProType.PERSONNEL_STATUS.getType());
//            state.setEvent(GlobalConstant.ProType.OFFWORK_EVENT.getType());   //下班
//            state.setTeamId(briefer1.getUsIds());
//            examine.setReportType(1);  //下班上报
//            UpdateStateUtils.updateSupervise(state, null);
//            TempEntity temp = new TempEntity();
//            temp.setStartTime(currTime);
//            temp.setOrderName(ordlinkMapper.getOrderName(sdId));
//            SendMsgUtils.sendToUser(executeOrder.getUsIds(), JSON.toJSONString(temp),
//                    ChatType.MAC_REPORTORDER.getType());
//            //这里生成设备的reprot
//            try {
//                // machsingleService.generateMachineOeeReport(reportVo.getExamine().getReportUserid(), reportVo.getOrdlinkVO().getMaId(), new Date(), null,exId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            //这里是每一个排查的嵌入
//            try {
//                //  iStatisOrdsingleService.generateOrderOEEReport(reportVo.getExamine().getReportUserid(), reportVo.getOrdlinkVO().getMaId(), reportVo.getOrdlinkVO().getId(), dutyNumber, null, exId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //这里进行达成率的更新
//            try {
//                iStatisMachreachService.updateStatisMachreachObj(briefer.getId(), ordlink, workbatchShift);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            //换单上报
//            examine.setReportType(2);   //换单上报
//            //这里进行达成率的更新
//            try {
//                iStatisMachreachService.updateStatisMachreachObj(briefer.getId(), ordlink, workbatchShift);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        // TODO 这里也需要测试一下 然后分批次进行嵌入
//        if (examineMapper.insert(examine) <= 0) {
//            log.error("上报出现异常:[examine:{}]", examine);
//        }
//        statisLeanOeeController.setMachoee(maId, targetDay, wsId);
//    }
}
