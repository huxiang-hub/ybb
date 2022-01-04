package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.execute.wrapper.ExecuteInfoWrapper;
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
 * 设备停机故障记录表_yb_execute_info 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executeInfo")
@Api(value = "执行单_yb_execute_info", tags = "执行单_yb_execute_info接口")
public class ExecuteInfoController extends BladeController {

    private IExecuteInfoService executeInfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executeInfo")
    public R<ExecuteInfoVO> detail(ExecuteInfo executeInfo) {
        ExecuteInfo detail = executeInfoService.getOne(Condition.getQueryWrapper(executeInfo));
        return R.data(ExecuteInfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备停机故障记录表_yb_execute_info
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeInfo")
    public R<IPage<ExecuteInfoVO>> list(ExecuteInfo executeInfo, Query query) {
        IPage<ExecuteInfo> pages = executeInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(executeInfo));
        return R.data(ExecuteInfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备停机故障记录表_yb_execute_info
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeInfo")
    public R<IPage<ExecuteInfoVO>> page(ExecuteInfoVO executeInfo, Query query) {
        IPage<ExecuteInfoVO> pages = executeInfoService.selectExecuteInfoPage(Condition.getPage(query), executeInfo);
        return R.data(pages);
    }

    /**
     * 新增 设备停机故障记录表_yb_execute_info
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executeInfo")
    public R save(@Valid @RequestBody ExecuteInfo executeInfo) {
        return R.status(executeInfoService.save(executeInfo));
    }

    /**
     * 修改 设备停机故障记录表_yb_execute_info
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executeInfo")
    public R update(@Valid @RequestBody ExecuteInfo executeInfo) {
        return R.status(executeInfoService.updateById(executeInfo));
    }

    /**
     * 新增或修改 设备停机故障记录表_yb_execute_info
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executeInfo")
    public R submit(@Valid @RequestBody ExecuteInfo executeInfo) {
        return R.status(executeInfoService.saveOrUpdate(executeInfo));
    }


    /**
     * 删除 设备停机故障记录表_yb_execute_info
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeInfoService.removeByIds(Func.toIntList(ids)));
    }


}
