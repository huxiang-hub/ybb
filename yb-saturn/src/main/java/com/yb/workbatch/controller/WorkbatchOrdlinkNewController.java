package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.common.DateUtil;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.workbatch.entity.*;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.request.*;
import com.yb.workbatch.service.*;
import com.yb.workbatch.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

/**
 * 生产排产表yb_workbatch_ordlink 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbatchordlinknew")
@Api(tags = "新生产排产接口")
@Slf4j
public class WorkbatchOrdlinkNewController extends BladeController {
    @Autowired
    private IWorkbatchOrdlinkService workbatchOrdlinkService;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private WorkbatchShiftService workbatchShiftService;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;


    @PostMapping("/getSaturability")
    @ApiOperation(value = "获取排产饱和度")
    public R<List<SaturabilityVO>> getSaturability(ProductionSchedulingDetailsParam detailsParam) {
        return R.data(workbatchOrdlinkNewService.getSaturability(detailsParam));
    }

    @GetMapping("/productionSchedulingDetails")
    @ApiOperation(value = "排产详情图接口")
    public R<List<ProductionSchedulingDetailsVO>> productionSchedulingDetails(ProductionSchedulingDetailsParam detailsParam) {

        return R.data(workbatchOrdlinkService.productionSchedulingDetails(detailsParam));
    }


    /**
     * 新增停机保养排产
     */
    @PostMapping("/newSoptWorkbatch")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "新增停机保养排产", notes = "传入workbatchOrdlink")
    public R newSoptWorkbatch(@RequestBody WorkbatchShift workbatchShift) {
        String sdDate = workbatchShift.getSdDate();
        Date proBeginTime = workbatchShift.getProBeginTime();
        Date proFinishTime = workbatchShift.getProFinishTime();
        if (proBeginTime != null && proFinishTime != null) {
            proBeginTime = DateUtil.toDate(sdDate + " " + DateUtil.format(proBeginTime, "HH:mm:ss"), null);
            proFinishTime = DateUtil.toDate(sdDate + " " + DateUtil.format(proFinishTime, "HH:mm:ss"), null);
        }
        String planType = workbatchShift.getPlanType();
        switch (planType) {
            case "plan_stop": {//停机3
                planType = "3";
                break;
            }
            case "protect": {//保养2
                planType = "2";
                break;
            }
        }
        Date date = new Date();
        workbatchShift.setPlanType(planType);
        workbatchShift.setShiftStatus(-1);
        workbatchShift.setStatus("1");
        workbatchShift.setCreateAt(date);
        workbatchShift.setUpdateAt(date);
        workbatchShift.setProBeginTime(proBeginTime);
        workbatchShift.setProFinishTime(proFinishTime);
        return R.status(workbatchShiftService.save(workbatchShift));
    }

    /**
     * 再排产-设定
     */
    @PostMapping("/andProductionScheduling")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "再排产", notes = "传入workbatchOrdlink")
    public R andProductionScheduling(@RequestBody AndProductionVO andProductionVO) {
        WorkbatchOrdlinkVO workbatchOrdlinkVO = new WorkbatchOrdlinkVO();
        workbatchOrdlinkVO.setId(andProductionVO.getId());
        workbatchOrdlinkVO.setPlanNum(andProductionVO.getPlanNum());
        workbatchOrdlinkVO.setMouldStay(andProductionVO.getMouldStay());
        workbatchOrdlinkVO.setSpeed(andProductionVO.getSpeed());
        workbatchOrdlinkVO.setWsId(andProductionVO.getWsId());
        workbatchOrdlinkVO.setSdDate(andProductionVO.getSdDate());
        workbatchOrdlinkVO.setMaId(andProductionVO.getMaId());

        int insert = workbatchOrdlinkService.andProductionScheduling(workbatchOrdlinkVO);
        if (insert > 0) {
            return R.success("操作成功");
        }
        return R.success("选择日期的班次已存在该工单,不能重新排产");
    }

    /**
     * 查询已排产的信息
     */
    @GetMapping("/waitList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = " 查询已排产的信息", notes = "传入workbatchOrdlink")
    public R waitList(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
        return R.data(workbatchOrdlinkService.getwaitWorkBatchOrd(workbatchOrdlinkVO));
    }

    /**
     * 查询未排产的信息
     */
    @GetMapping("/yetList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "查询未排产的信息", notes = "传入workbatchOrdlink")
    public R yetList(WorkbatchOrdlinkVO workbatchOrdlinkVO, Query query) {
        IPage<WorkbatchOrdlinkVO> workBatech = workbatchOrdlinkService.getyetWorkBatchOrd(Condition.getPage(query), workbatchOrdlinkVO);
        return R.data(workBatech);
    }


    /**
     * 修改状态（点击排产）
     *
     * @deprecated 停用方法
     */
//    @GetMapping("/update")
//    @ApiOperationSupport(order = 2)
//    @ApiOperation(value = "分页", notes = "传入workbatchOrdlink")
//    public R update(WorkbatchOrdlinkVO workbatchOrdlinkVO) {
//        WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
//        BeanUtils.copyProperties(workbatchOrdlinkVO, workbatchOrdlink);
//        return R.status(workbatchOrdlinkService.updateById(workbatchOrdlink));
//    }

    /**
     * 保存最新数据
     */
    @GetMapping("/setList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "批量保存", notes = "传入workbatchOrdlink")
    public R setList(List<WorkbatchOrdlinkVO> workbatchOrdlinkVOList) {
        List<WorkbatchOrdlink> list = new ArrayList();
        for (WorkbatchOrdlinkVO workbatchOrdlinkVO : workbatchOrdlinkVOList) {
            WorkbatchOrdlink workbatchOrdlink = new WorkbatchOrdlink();
            workbatchOrdlink.setPlanNum(workbatchOrdlinkVO.getAllPlanNum());
            BeanUtils.copyProperties(workbatchOrdlinkVO, workbatchOrdlink);
            list.add(workbatchOrdlink);
        }
        workbatchOrdlinkService.saveOrUpdateBatch(list);
        return R.fail("保存成功");
    }

    /**
     * 批量下发/挂起/废弃/驳回/排产
     */
    @PostMapping("updateStatus")
    @ApiOperation(value = "批量修改 下发/挂起/废弃/驳回/排产", notes = "传入workbatchOrdlink")
    public R updateStatus(@RequestBody @Validated WorkbatchOrdlinkStatusUpdateRequest request) throws ParseException {
        log.debug("开始批量修改排产单状态:[request:{}]", request);

        String result = workbatchOrdlinkNewService.updateStatus(request);
        if (result != null && result.length() > 0) {
            log.info("error:" + result);
            return R.fail(result);
        } else {
            log.info("批量修改排产单状态成功");
            return R.success(result);
        }
    }

    @PostMapping("lockordreach")
    @ApiOperation(value = "锁定小时达成率的排序信息", notes = "传入修改状态相同的对象信息")
    public R lockordreach(Integer wsId, String sdDate, Integer maId, Integer reachIslock) {
        log.debug("开始批量修改排产单状态:[lockordreach:{}]" + maId);
        workbatchOrdlinkNewService.lockordreach(wsId, sdDate, maId, reachIslock);
        log.debug("锁定小时达成率的排序信息");
        return R.success("已经锁定达成率：sdDate：" + sdDate + "::班次:wsId:" + wsId + ":设备：maId:" + maId + "::是否锁定:" + reachIslock);
    }

    @PostMapping("lockWfsort")
    @ApiOperation(value = "锁定小时达成率的排序信息", notes = "传入修改状态相同的对象信息")
    public R lockWfsort(Integer wsId, String sdDate, Integer maId, Integer wfsortIslock) {
        log.debug("开始批量修改排产单状态:[lockWfsort:{}]", maId);
        workbatchOrdlinkNewService.lockWfsort(wsId, sdDate, maId, wfsortIslock);
        log.debug("锁定小时达成率的排序信息");
        return R.success("已经锁定达成率：sdDate：" + sdDate + "::班次:wsId:" + wsId + ":设备：maId:" + maId + "::是否锁定:" + wfsortIslock);
    }

    @PostMapping("refreshRealcount")
    @ApiOperation(value = "小时达成率的实际数据补充", notes = "小时达成率的实际数据补充")
    public R refreshRealcount(Integer wsId, String sdDate, Integer maId) {
        log.debug("小时达成率的实际数据补充:[refreshRealcount:{}]", maId);
        workbatchOrdlinkNewService.refreshRealcount(wsId, sdDate, maId);
        log.debug("小时达成率的实际数据补充");
        return R.success("实际数据补充：sdDate：" + sdDate + "::班次:wsId:" + wsId + ":设备：maId:" + maId);
    }

    /**
     * 修改排产精益设置信息
     */
    @PostMapping("updateLeanSet")
    @ApiOperation(value = "修改排产精益设置信息")
    public R updateLeanSet(@RequestBody @Validated WorkBatchOrdlinkUpdateLeanSet request) {
        log.debug("开始修改排产精益设置信息:[request:{}]", request);

        workbatchOrdlinkNewService.updateLeanSet(request);

        log.debug("修改修改排产精益设置信息成功");
        return R.success("修改成功");
    }


    /**
     * 修改排产精益设置信息
     */
    @PostMapping("updateMaterialAndRemark")
    @ApiOperation(value = "修改主料/辅料入库时间,备注")
    public R updateMaterialAndRemark(@RequestBody @Validated WorkBatchOrdlinkMaterialUpdateRequest request) {
        log.debug("修改主料/辅料入库时间,备注信息:[request:{}]", request);

        workbatchOrdlinkNewService.updateMaterialAndRemark(request);

        log.debug("修改主料/辅料入库时间,备注信息");
        return R.success("修改成功");
    }

    /**
     * 修改达成率计划数量
     */
    @PostMapping("updateOrdReach")
    @ApiOperation(value = "修改达成率计划数量")
    public R updateOrdReach(@RequestBody @Validated WorkBatchOrdReachUpdateRequest request) {
        log.debug("开始修改达成率计划数量:[request:{}]", request);
        workbatchOrdlinkNewService.updateOrdReach(request);
        log.debug("修改达成率计划数量成功");
        return R.success("修改成功");
    }

    /**
     * 修改排查工序//排产保存操作
     */
    @PostMapping("updateWorkBatchSort")
    @ApiOperation(value = "修改排产顺序；保存顺序")
    public R updateWorkBatchSort(@RequestBody @Validated WorkBatchSortUpdateRequest request) {
        log.debug("开始修改排查顺序:[request:{}]", request);
        workbatchOrdlinkNewService.updateWorkBatchSort(request);
        log.debug("修改排产顺序成功");
        return R.success("修改成功");
    }

    /**
     * 给设备绑定排产单
     */
    @PostMapping("bindWorkBatch")
    @ApiOperation(value = "绑定排产单给设备")
    public R bindWorkBatch(@RequestBody @Validated WorkBatchBindRequest request) throws ParseException {
        log.debug("开始绑定排产单给设备:[request:{}]", request);
        workbatchOrdlinkNewService.bindWorkBatch(request);
        log.debug("排产单绑定设备成功");
        return R.success("修改成功");
    }


    @GetMapping("getSimilarDeviceList")
    @ApiOperation(value = "获取同类设备列表")
    public R getSimilarDeviceList(String maType) {
        log.debug("开始获取同类设备列表:[maType:{}]", maType);
        List<MachineMainfo> list = workbatchOrdlinkNewService.getSimilarDeviceList(maType);
        log.debug("开始获取同类设备列表成功");
        return R.data(list);
    }

    @GetMapping("reschedule")
    @ApiOperation(value = "获取再排产详情信息")
    public R<List<WorkbatchShift>> reschedule(Integer wfId) {
        log.debug("获取再排产详情信息:[wfId:{}]", wfId);
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        List<WorkbatchShift> reschedule = new ArrayList<>();
        if (workbatchShift != null) {
            reschedule = workbatchOrdlinkNewService.reschedule(workbatchShift.getSdId());
        }
        log.debug("获取再排产详情信息成功");
        return R.data(reschedule);
    }


    /**
     * 强制结束
     */
    @GetMapping("/forcedEnd")
    @ApiOperation(value = "强制结束")
    public R forcedEnd(Integer sdId) {
        log.debug("开始强制结束排产单:[sdId:{}]", sdId);

        workbatchOrdlinkNewService.forcedEnd(sdId);

        log.debug("强制结束排产单成功 :[sdId:{}]", sdId);
        return R.success("保存成功");
    }

    /**
     * 获取上工序进度
     */
    @GetMapping("/upProcessSchedule")
    @ApiOperation(value = "获取上工序进度")
    public R<UpProcessScheduleVO> upProcessSchedule(Integer wfId) {
        log.debug("开始获取上工序进度:[wfId:{}]", wfId);

        UpProcessScheduleVO upProcessScheduleVO = workbatchOrdlinkNewService.upProcessSchedule(wfId);

        log.debug("获取上工序进度成功 :[wfId:{}]", wfId);
        return R.data(upProcessScheduleVO);
    }

    @GetMapping("scheduleDetails")
    @ApiOperation(value = "滑动详情信息")
    public R<List<SlideShiftDetailsVO>> scheduleDetails(Integer sdId) {
        List<SlideShiftDetailsVO> slideShiftDetails = workbatchShiftMapper.findSlideShiftDetails(sdId);
        return R.data(slideShiftDetails);
    }


    @PostMapping("existingSchedule")
    @ApiOperation(value = "获取有排产的日期")
    public R existingSchedule(@ApiParam(value = "设备id", required = true) Integer maId,
                              @ApiParam(value = "日期", required = true) String sdDate) throws ParseException {
        log.debug("获取有排产的日期:[maId:{}, sdDate:{}]", maId, sdDate);

        if (sdDate == null || sdDate.length() <= 0) {
            sdDate = DateUtil.refNowDay();//如果为空就传入当天的日期信息内容
        }
        List<String> date = workbatchOrdlinkNewService.existingSchedule(maId, sdDate);

        log.debug("获取有排产的日期成功");
        return R.data(date);
    }

    @ApiOperation("获取待排产列表")
    @GetMapping("/works")
    public R getWorks(@ApiParam("设备id") @RequestParam(value = "machineId") Integer machineId,
                      @ApiParam("1:按机台显示，2:按工序显示") @RequestParam(value = "type") String type, Query query) {
        MachineMainfo machineMainfo = machineMainfoMapper.selectOne(Wrappers.<MachineMainfo>lambdaQuery().eq(MachineMainfo::getId, machineId).select(MachineMainfo::getProId));
        IPage<WorkbatchOrdlink> page = Condition.getPage(query);
        IPage<WorkbatchOrdlink> workbatchOrdlinks1 = workbatchOrdlinkService.getByMachineId(machineId, page);
        IPage<WorkbatchOrdlink> workbatchOrdlinks2 = workbatchOrdlinkService.getByProcessId(machineMainfo.getProId(), page);
        if ("1".equals(type)) {
            return R.data(workbatchOrdlinks1);
        } else if ("2".equals(type)) {
            return R.data(workbatchOrdlinks2);
        }
        return R.data(null);
    }


    @ApiOperation("根据工单id获取未排产数量")
    @GetMapping("worksNum")
    public R worksNum(Integer sdId, Integer maId) {
        return R.data(workbatchOrdlinkService.worksNum(sdId, maId));
    }

    @ApiOperation("查询工单生产进度")
    @GetMapping("worksTempo")
    public R worksTempo(Query query) {
        IPage<WorksTempoVO> page = Condition.getPage(query);
        IPage<WorksTempoVO> list = workbatchOrdlinkService.worksTempo(page);
        return R.data(list);
    }
}
