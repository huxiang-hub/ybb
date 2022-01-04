package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.supervise.entity.SuperviseWarning;
import com.yb.supervise.service.ISuperviseStopService;
import com.yb.supervise.service.ISuperviseWarningService;
import com.yb.supervise.vo.SuperviseWarningVO;
import com.yb.supervise.wrapper.SuperviseWarningWrapper;
import com.yb.workbatch.entity.SuperviseExestop;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 预警控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/stopWarn")
@Api(value = "停机预警", tags = "停机预警")
public class SuperviseStopController extends BladeController {

    private ISuperviseStopService superviseStopService;

    /**
     * 详情
     */
    @GetMapping("/stopInfo")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "stopInfo")
    public R<SuperviseExestop> stopInfo(Integer maId) {

        SuperviseExestop superviseExestop = superviseStopService.stopInfo(maId);

        return R.data(superviseExestop);
    }


    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseWarning")
    public R save(@Valid @RequestBody SuperviseWarning superviseWarning) {
        return null;
    }
}
