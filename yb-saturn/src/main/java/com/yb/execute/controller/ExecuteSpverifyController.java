package com.yb.execute.controller;

import com.yb.execute.entity.ExecuteSpverify;
import com.yb.execute.service.IExecuteSpverifyService;
import com.yb.execute.vo.ExecuteSpverifyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.boot.ctrl.BladeController;

/**
 * 审核记录_yb_execute_spverify 控制器
 *
 * @author BladeX
 * @since 2021-03-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executespverify")
@Api(value = "审核记录_yb_execute_spverify", tags = "审核记录_yb_execute_spverify接口")
public class ExecuteSpverifyController extends BladeController {

	private final IExecuteSpverifyService executeSpverifyService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入executeSpverify")
	public R<ExecuteSpverify> detail(ExecuteSpverify executeSpverify) {
		ExecuteSpverify detail = executeSpverifyService.getOne(Condition.getQueryWrapper(executeSpverify));
		return R.data(detail);
	}

	/**
	 * 分页 审核记录_yb_execute_spverify
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入executeSpverify")
	public R<IPage<ExecuteSpverify>> list(ExecuteSpverify executeSpverify, Query query) {
		IPage<ExecuteSpverify> pages = executeSpverifyService.page(Condition.getPage(query), Condition.getQueryWrapper(executeSpverify));
		return R.data(pages);
	}

	/**
	 * 自定义分页 审核记录_yb_execute_spverify
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入executeSpverify")
	public R<IPage<ExecuteSpverifyVO>> page(ExecuteSpverifyVO executeSpverify, Query query) {
		IPage<ExecuteSpverifyVO> pages = executeSpverifyService.selectExecuteSpverifyPage(Condition.getPage(query), executeSpverify);
		return R.data(pages);
	}

	/**
	 * 新增 审核记录_yb_execute_spverify
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入executeSpverify")
	public R save(@Valid @RequestBody ExecuteSpverify executeSpverify) {
		return R.status(executeSpverifyService.save(executeSpverify));
	}

	/**
	 * 修改 审核记录_yb_execute_spverify
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入executeSpverify")
	public R update(@Valid @RequestBody ExecuteSpverify executeSpverify) {
		return R.status(executeSpverifyService.updateById(executeSpverify));
	}

	/**
	 * 新增或修改 审核记录_yb_execute_spverify
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入executeSpverify")
	public R submit(@Valid @RequestBody ExecuteSpverify executeSpverify) {
		return R.status(executeSpverifyService.saveOrUpdate(executeSpverify));
	}

	
	/**
	 * 删除 审核记录_yb_execute_spverify
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(executeSpverifyService.removeByIds(Func.toLongList(ids)));
	}

	
}
