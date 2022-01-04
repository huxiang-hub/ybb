package com.yb.supervise.controller.app;

import com.yb.supervise.request.GetYieldStatisticsRequest;
import com.yb.supervise.request.PerformanceAnalyRequest;
import com.yb.supervise.service.ISuperviseIntervalService;
import com.yb.supervise.vo.YieldStatisticsVO;
import com.yb.supervise.vo.PerformanceAnalyVO;
import com.yb.workbatch.service.IWorkbatchShiftsetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

/**
 * 设备状态间隔表interval- 印聊
 *
 * @author my
 * @since 2020-07-31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/app/superviseinterval")
@Api(value = "设备状态间隔表interval", tags = "设备状态间隔表interval")
@Slf4j
public class AppSuperviseIntervalController extends BladeController {

    private ISuperviseIntervalService superviseIntervalService;

    private IWorkbatchShiftsetService workbatchShiftsetService;


    @PostMapping("/yieldStatistics")
    @ApiOperation(value = "获取印聊产量分析")
    public R<YieldStatisticsVO> getYieldStatistics(@RequestBody @Validated GetYieldStatisticsRequest request) throws ParseException {
        log.info("获取印聊产量分析:[request:{}]", request);

        YieldStatisticsVO yieldStatistics = superviseIntervalService.getYieldStatistics(request);

        log.info("获取    印聊产量分析成功");
        return R.data(yieldStatistics);
    }

    @PostMapping("/performanceAnaly")
    @ApiOperation(value = "获取印聊绩效统计")
    public R<PerformanceAnalyVO> performanceAnaly(@RequestBody @Validated PerformanceAnalyRequest request) {
        log.info("获取印聊获取绩效统计:[request:{}]", request);

        PerformanceAnalyVO performanceAnaly = superviseIntervalService.getPerformanceAnaly(request);

        log.info("获取印聊获取绩效统计成功");
        return R.data(performanceAnaly);
    }


}
