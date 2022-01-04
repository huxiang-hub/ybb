package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.supervise.vo.SuperviseExecuteVO;
import com.yb.supervise.wrapper.SuperviseExecuteWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 设备订单状态表execut-视图 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/superviseexecut")
@Api(value = "设备订单状态表execut-视图", tags = "设备订单状态表execut-视图接口")
public class SuperviseExecuteController extends BladeController {

    @Autowired
    private ISuperviseExecuteService superviseExecutService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseExecut")
    public R<SuperviseExecuteVO> detail(SuperviseExecute superviseExecut) {
        SuperviseExecute detail = superviseExecutService.getOne(Condition.getQueryWrapper(superviseExecut));
        return R.data(SuperviseExecuteWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备订单状态表execut-视图
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入superviseExecut")
    public R<IPage<SuperviseExecuteVO>> list(SuperviseExecuteVO superviseExecuteVO, Query query) {
        Integer current = (query.getCurrent() - 1) * query.getSize();
        IPage<SuperviseExecuteVO> pages = superviseExecutService.findExecuteOrderStatus(current, query.getSize(), superviseExecuteVO);
        return R.data(pages);
    }

    /**
     * 自定义分页 设备订单状态表execut-视图
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseExecut")
    public R<IPage<SuperviseExecuteVO>> page(SuperviseExecuteVO superviseExecut, Query query) {
        IPage<SuperviseExecuteVO> pages = superviseExecutService.selectSuperviseExecutePage(Condition.getPage(query), superviseExecut);
        return R.data(pages);
    }

    /**
     * 新增 设备订单状态表execut-视图
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseExecut")
    public R save(@Valid @RequestBody SuperviseExecute superviseExecut) {
        return R.status(superviseExecutService.save(superviseExecut));
    }

    /**
     * 修改 设备订单状态表execut-视图
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseExecut")
    public R update(@Valid @RequestBody SuperviseExecute superviseExecut) {
        return R.status(superviseExecutService.updateById(superviseExecut));
    }

    /**
     * 新增或修改 设备订单状态表execut-视图
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseExecut")
    public R submit(@Valid @RequestBody SuperviseExecute superviseExecut) {
        return R.status(superviseExecutService.saveOrUpdate(superviseExecut));
    }


    /**
     * 删除 设备订单状态表execut-视图
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseExecutService.removeByIds(Func.toIntList(ids)));
    }

    @GetMapping("/getStartNum")
    @ApiOperation(value = "获取当前设备开始接单的计数")
    public R<Map<String, Integer>> getStartNum(Integer maId) {

        Map<String, Integer> map = superviseExecutService.getStartNum(maId);

        return R.data(map);
    }

}
