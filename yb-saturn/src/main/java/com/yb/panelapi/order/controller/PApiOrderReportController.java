package com.yb.panelapi.order.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteFaultService;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.service.impl.ExecuteBrieferServiceImpl;
import com.yb.execute.vo.ExecuteExamineIpcVO;
import com.yb.panelapi.order.entity.*;
import com.yb.panelapi.order.service.IPApiOrderReportService;
import com.yb.panelapi.request.OrderReportRequest;
import com.yb.panelapi.user.utils.R;
import com.yb.statis.service.IStatisMachsingleService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.timer.AutomaticStop;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.vo.AnewWfsDSortVO;
import com.yb.workbatch.vo.WorkbatchOrdlinkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springblade.common.resubmit.annotion.ReSubmit;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@RestController
@RequestMapping("/plapi/order")
@Api(tags = "上报相关接口")
public class PApiOrderReportController {

    @Autowired
    private IPApiOrderReportService orderReportService;

    @Autowired
    private ISuperviseBoxinfoService boxinfoService;

    @Autowired
    private IExecuteFaultService faultService;

    @Autowired
    private IExecuteWasteService wasteService;

    @Autowired
    private IBaseStaffinfoService staffinfoService;

    @Autowired
    private IExecuteBrieferService executeBrieferService;

    @Autowired
    private IStoreInventoryService storeInventoryService;

    @Autowired
    private ISuperviseExecuteService superviseExecuteService;

    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;

    @Autowired
    private IExecuteStateService executeStateService;

    /**
     * 上报数据的保存
     *
     * @param reportVo
     * @return
     */
    @PostMapping("/report")
    @ResponseBody
    @ReSubmit
    public R orderForm(@RequestBody OrderReportVo reportVo) {
        return orderReportService.orderReport(reportVo);
    }

    /**
     * 上报数据的保存
     *
     * @param reportVo
     * @return
     */
    @PostMapping("/reportNew")
    @ResponseBody
    @ReSubmit
    public R reportNew(@RequestBody OrderReportNewVO reportVo) {
        return orderReportService.orderReportNew(reportVo);
    }


    @PostMapping("/forcedEnd")
    @ApiOperation(value = "强制上报")
    @ReSubmit
    public R forcedEnd(ForcedEnd forcedEnd) {
        OrderReportVo reportVo = new OrderReportVo();
        reportVo.setBriefer(new ExecuteBriefer());
        reportVo.setOrdlinkVO(new WorkbatchOrdlinkVO());
        reportVo.setExamine(new ExecuteExamineIpcVO());
        reportVo.setWastes(new ArrayList<>());
        Integer countNum = forcedEnd.getCountNum();
        reportVo.getBriefer().setExId(forcedEnd.getExId());
        reportVo.getBriefer().setProductNum(forcedEnd.getProductNum());
        reportVo.getBriefer().setCountNum(countNum);
        reportVo.getBriefer().setWasteNum(forcedEnd.getWasteNum());
        reportVo.getBriefer().setUpwasteNum(forcedEnd.getUpwasteNum());
        reportVo.getBriefer().setUppwasteNum(forcedEnd.getUppwasteNum());
        Integer reportUserid = forcedEnd.getReportUserid();
        if (reportUserid == null) {
            reportUserid = SaSecureUtil.getUserId();
        }
        reportVo.getExamine().setReportUserid(reportUserid);
        reportVo.setBid(forcedEnd.getBid());
        reportVo.getOrdlinkVO().setEsId(forcedEnd.getEsId());
        reportVo.getOrdlinkVO().setId(forcedEnd.getId());
        reportVo.getOrdlinkVO().setUsIds(forcedEnd.getUsIds());
        Integer wfId = forcedEnd.getWfId();
        reportVo.getOrdlinkVO().setWfId(wfId);
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        if (workbatchShift == null) {
            return R.error("wfId不正确,未查到排产单");
        }
        Integer planNum = workbatchShift.getPlanNum();
        Integer finishNum = workbatchShift.getFinishNum();
        if(planNum == null){
            planNum = 0;
        }
        if(finishNum == null){
            finishNum = 0;
        }
        int n = planNum - finishNum - countNum;
        Integer wsId = workbatchShift.getWsId();
        reportVo.getOrdlinkVO().setWsId(wsId);
        Integer maId = forcedEnd.getMaId();
        reportVo.getOrdlinkVO().setMaId(maId);
        if (maId == null) {
            maId = workbatchShift.getMaId();
            reportVo.getOrdlinkVO().setMaId(maId);
        }
//        SuperviseExecute superviseExecute =
//                superviseExecuteService.getOne(new QueryWrapper<SuperviseExecute>().eq("ma_id", maId));
//        if(superviseExecute == null){
//            return R.error("设备实时执行表未查到该设备数据");
//        }
//        reportVo.getBriefer().setExId(superviseExecute.getExId());
        if (reportVo.getBid() == null || reportVo.getBid() == 0) {
            ExecuteBriefer executeBriefer = new ExecuteBriefer();
            executeBriefer.setCreateAt(new Date());
            executeBriefer.setWfId(wfId);
            executeBriefer.setExId(forcedEnd.getExId());
            List<ExecuteState> executeStateList = executeStateService.list(new QueryWrapper<ExecuteState>()
                    .eq("ma_id", maId)
                    .eq("wf_id", wfId)
                    .isNull("end_at")
                    .orderByDesc("create_at"));
            if (!executeStateList.isEmpty()) {
                ExecuteState executeState = executeStateList.get(0);
                executeBriefer.setEsId(executeState.getId());
                reportVo.getOrdlinkVO().setEsId(executeState.getId());
            }
            executeBrieferService.save(executeBriefer);
            reportVo.setBid(executeBriefer.getId());
        }

        return orderReportService.orderReport(reportVo);
    }

    /**
     * 机台排序
     *
     * @param anewWfsDSortVO
     * @return
     */

    @RequestMapping("/anewWfsDSort")
    public R anewWfsDSort(@RequestBody AnewWfsDSortVO anewWfsDSortVO) {
        if (orderReportService.anewWfsDSort(anewWfsDSortVO)) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 停机记录上报
     */
    @PostMapping("/downFaultReport")
    @ResponseBody
    public R faultReport(@RequestBody DownFaultReportDto faultReportDto) {

        return faultService.faultReport(faultReportDto);
    }

    /**
     * 停机记录上报
     */
    @PostMapping("/qualityWasteReport")
    @ResponseBody
    public R faultReport(@RequestBody QualityWasteReportDto wasteReportDto) {

        return wasteService.wasteReport(wasteReportDto);
    }

    /**
     * 获取盒子的计数  (订单开始时获取订单结束获取)
     *
     * @param
     * @return
     */
    @PostMapping("/boxNum")
    @ResponseBody
    public R getBoxNum(@RequestParam String mId) {
        Integer boxNum = boxinfoService.getBoxNum(mId);
        if (boxNum == null) {
            return R.error(400, "当前设备没有生产数据");
        }
        return R.ok().put("data", boxNum);
    }

    /**
     * 获取当前设备上的用户
     */
    @GetMapping("/getMacUser")
    @ResponseBody
    public R getMacUser(@RequestParam String mId) {
        String macUser = boxinfoService.getMacUser(mId);
        String[] split = macUser.split("\\|");
        List<BaseStaffinfo> list = new ArrayList<>();
        for (String str : split) {
            Integer userId = Integer.valueOf(str);
            BaseStaffinfo staffinfo = staffinfoService.getById(userId);
            list.add(staffinfo);
        }
        return R.ok(list);
    }

    @Autowired
    public com.yb.timer.AutomaticStop AutomaticStop;

    @GetMapping("/AutomaticStop")
    @ApiOperation(value = "测试自动停止订单")
    public R AutomaticStop() {
        try {
            AutomaticStop.machineAutomaticStop();
            return R.ok();
        } catch (Exception e) {
            return R.error();
        }

    }

}
