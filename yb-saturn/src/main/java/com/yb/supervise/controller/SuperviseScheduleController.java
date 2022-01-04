package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.supervise.entity.SuperviseSchedule;
import com.yb.supervise.request.SuperviseOrderScheduleRequest;
import com.yb.supervise.service.ISuperviseScheduleService;
import com.yb.supervise.vo.SuperviseScheduleBatchVO;
import com.yb.supervise.vo.SuperviseScheduleOrderVO;
import com.yb.supervise.vo.SuperviseScheduleVO;
import com.yb.supervise.wrapper.SuperviseScheduleWrapper;
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
import java.util.List;

/**
 * 订单进度表（进度表-执行） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/superviseschedule")
@Api(value = "订单进度表（进度表-执行）", tags = "订单进度表（进度表-执行）接口")
public class SuperviseScheduleController extends BladeController {

    private ISuperviseScheduleService superviseScheduleService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseSchedule")
    public R<SuperviseScheduleVO> detail(SuperviseSchedule superviseSchedule) {
        SuperviseSchedule detail = superviseScheduleService.getOne(Condition.getQueryWrapper(superviseSchedule));
        return R.data(SuperviseScheduleWrapper.build().entityVO(detail));
    }

    /**
     * 分页 订单进度表（进度表-执行）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入superviseSchedule")
    public R<IPage<SuperviseScheduleVO>> list(SuperviseSchedule superviseSchedule, Query query) {
        IPage<SuperviseSchedule> pages = superviseScheduleService.page(Condition.getPage(query), Condition.getQueryWrapper(superviseSchedule));
        return R.data(SuperviseScheduleWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 订单进度表（进度表-执行）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseSchedule")
    public R<IPage<SuperviseScheduleVO>> page(SuperviseScheduleVO superviseSchedule, Query query) {
        IPage<SuperviseScheduleVO> pages = superviseScheduleService.selectSuperviseSchedulePage(Condition.getPage(query), superviseSchedule);
        return R.data(pages);
    }

    /**
     * 新增 订单进度表（进度表-执行）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseSchedule")
    public R save(@Valid @RequestBody SuperviseSchedule superviseSchedule) {
        return R.status(superviseScheduleService.save(superviseSchedule));
    }

    /**
     * 修改 订单进度表（进度表-执行）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseSchedule")
    public R update(@Valid @RequestBody SuperviseSchedule superviseSchedule) {
        return R.status(superviseScheduleService.updateById(superviseSchedule));
    }

    /**
     * 新增或修改 订单进度表（进度表-执行）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseSchedule")
    public R submit(@Valid @RequestBody SuperviseSchedule superviseSchedule) {
        return R.status(superviseScheduleService.saveOrUpdate(superviseSchedule));
    }


    /**
     * 删除 订单进度表（进度表-执行）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseScheduleService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 获取订单批次的进度
     */
    @PostMapping("/batchSchedule")
    public R batchSchedule(Integer odId) {
        List<SuperviseScheduleVO> batchSchedule =
                superviseScheduleService.getBatchSchedule(odId);
        return R.data(batchSchedule);
    }

    /**
     * 获取订单工序进度
     */
    @PostMapping("/processSchedule")
    public R processSchedule(Integer batchId) {
        List<SuperviseScheduleVO> processSchedule =
                superviseScheduleService.getProcessSchedule(batchId);
        return R.data(processSchedule);
    }


    /**
     * 订单进度表按订单
     */
    @PostMapping("orderScheduleByOrder")
    @ApiOperation(value = "订单进度表按订单")
    public R<IPage<SuperviseScheduleOrderVO>> orderScheduleByOrder(SuperviseOrderScheduleRequest request, Query query) {
        IPage<SuperviseScheduleOrderVO> pages = superviseScheduleService.orderScheduleByOrder(Condition.getPage(query), request);
        return R.data(pages);
    }

    /**
     * 订单进度表按批次
     */
    @PostMapping("orderScheduleByBatch")
    @ApiOperation(value = "订单进度表按批次")
    public R<IPage<List<SuperviseScheduleBatchVO>>> orderScheduleByBatch(SuperviseOrderScheduleRequest request, Query query) {
        IPage<List<SuperviseScheduleBatchVO>> pages = superviseScheduleService.orderScheduleByBatch(Condition.getPage(query), request);
        return R.data(pages);
    }
}
