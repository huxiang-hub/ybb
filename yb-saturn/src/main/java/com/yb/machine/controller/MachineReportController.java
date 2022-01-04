package com.yb.machine.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.execute.vo.TraycardMaterialsVO;
import com.yb.machine.request.MachineReportRequest;
import com.yb.machine.request.OrderDetailRequest;
import com.yb.machine.request.OrderProcessScheduleRequest;
import com.yb.machine.request.ProcessFlowPageRequest;
import com.yb.machine.response.MachineReportVO;
import com.yb.machine.response.OrderDetailVO;
import com.yb.machine.response.OrderProcessScheduleVO;
import com.yb.machine.response.ProcessFlowVO;
import com.yb.machine.service.MachineReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description: 设备报表 controller
 * @Author my
 */
@RestController
@RequestMapping("/report")
@Api(tags = "设备报表")
@Slf4j
public class MachineReportController {

    @Autowired
    private MachineReportService machineReportService;

    /**
     * 详情
     */
    @GetMapping("/machineReport")
    @ApiOperation(value = "设备报表信息")
    public R<MachineReportVO> machineReport(@Validated MachineReportRequest request) {

        MachineReportVO vo = machineReportService.machineReport(request);

        return R.data(vo);
    }


    /**
     * 分页
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页")
    public R<IPage<ProcessFlowVO>> list(ProcessFlowPageRequest request) {
        IPage<ProcessFlowVO> pages = machineReportService.page(Condition.getPage(request), request);
        return R.data(pages);
    }

    /**
     * 工单详情
     */
    @GetMapping("/orderDetail")
    @ApiOperation(value = "工单详情")
    public R<List<OrderDetailVO>> orderDetail(@Validated OrderDetailRequest request) {
        List<OrderDetailVO> vos = machineReportService.orderDetail(request);
        return R.data(vos);
    }


    /**
     * 工单工序进度
     */
    @GetMapping("/orderProcessSchedule")
    @ApiOperation(value = "工单工序进度")
    public R<List<OrderProcessScheduleVO>> orderProcessSchedule(@Validated OrderProcessScheduleRequest request) {
        List<OrderProcessScheduleVO> vos = machineReportService.orderProcessSchedule(request);
        return R.data(vos);
    }


    @GetMapping("getTraycardMaterials")
    @ApiOperation(value = "根据wfId查询托盘及上料相关信息")
    public R<List<TraycardMaterialsVO>> getTraycardMaterials(@RequestParam("wfId") Integer wfId) {
        List<TraycardMaterialsVO> traycardMaterialsVOList = machineReportService.getTraycardMaterials(wfId);
        return R.data(traycardMaterialsVOList);
    }

    /******
     * 执行单_yb_execute_info 数据的关联查询
     * @param wfId
     * @return
     */
    @GetMapping("getExecutinfo")
    @ApiOperation(value = "根据wfId排程ID查询执行单详情情况，是否上报等相关数据")
    public R<List<ExecuteInfoVO>> getExecutinfoList(@RequestParam("wfId") Integer wfId) {
        List<ExecuteInfoVO> executeInfolist = machineReportService.getExecutinfoList(wfId);
        return R.data(executeInfolist);
    }

}
