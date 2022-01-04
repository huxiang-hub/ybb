package com.yb.workbatch.controller;/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */

import com.yb.workbatch.entity.Ordexecute;
import com.yb.workbatch.service.IOrdexecuteService;
import com.yb.workbatch.vo.OrdexecuteVO;
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
 * VIEW 控制器
 *
 * @author BladeX
 * @since 2021-01-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/ordexecute")
@Api(value = "VIEW", tags = "VIEW接口")
public class OrdexecuteController extends BladeController {

	private final IOrdexecuteService ordexecuteService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入ordexecute")
	public R<Ordexecute> detail(Ordexecute ordexecute) {
		Ordexecute detail = ordexecuteService.getOne(Condition.getQueryWrapper(ordexecute));
		return R.data(detail);
	}

	/**
	 * 分页 VIEW
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入ordexecute")
	public R<IPage<Ordexecute>> list(Ordexecute ordexecute, Query query) {
		IPage<Ordexecute> pages = ordexecuteService.page(Condition.getPage(query), Condition.getQueryWrapper(ordexecute));
		return R.data(pages);
	}

	/**
	 * 自定义分页 VIEW
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入ordexecute")
	public R<IPage<OrdexecuteVO>> page(OrdexecuteVO ordexecute, Query query) {
		IPage<OrdexecuteVO> pages = ordexecuteService.selectOrdexecutePage(Condition.getPage(query), ordexecute);
		return R.data(pages);
	}

	/**
	 * 新增 VIEW
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入ordexecute")
	public R save(@Valid @RequestBody Ordexecute ordexecute) {
		return R.status(ordexecuteService.save(ordexecute));
	}

	/**
	 * 修改 VIEW
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入ordexecute")
	public R update(@Valid @RequestBody Ordexecute ordexecute) {
		return R.status(ordexecuteService.updateById(ordexecute));
	}

	/**
	 * 新增或修改 VIEW
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入ordexecute")
	public R submit(@Valid @RequestBody Ordexecute ordexecute) {
		return R.status(ordexecuteService.saveOrUpdate(ordexecute));
	}

	
	/**
	 * 删除 VIEW
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(ordexecuteService.removeByIds(Func.toLongList(ids)));
	}

	
}
