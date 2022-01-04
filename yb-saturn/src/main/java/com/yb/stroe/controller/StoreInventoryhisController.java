/*
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
package com.yb.stroe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.stroe.entity.StoreInventoryhis;
import com.yb.stroe.service.IStoreInventoryhisService;
import com.yb.stroe.vo.StoreInventoryhisVO;
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
import org.springblade.core.boot.ctrl.BladeController;

/**
 *  仓库历史台账控制器
 *
 * @author BladeX
 * @since 2021-01-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/storeinventoryhis")
@Api(value = "", tags = "仓库历史台账接口")
public class StoreInventoryhisController extends BladeController {

	private final IStoreInventoryhisService storeInventoryhisService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入storeInventoryhis")
	public R<StoreInventoryhis> detail(StoreInventoryhis storeInventoryhis) {
		StoreInventoryhis detail = storeInventoryhisService.getOne(Condition.getQueryWrapper(storeInventoryhis));
		return R.data(detail);
	}

	/**
	 * 分页 
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入storeInventoryhis")
	public R<IPage<StoreInventoryhis>> list(StoreInventoryhis storeInventoryhis, Query query) {
		IPage<StoreInventoryhis> pages = storeInventoryhisService.page(Condition.getPage(query), Condition.getQueryWrapper(storeInventoryhis));
		return R.data(pages);
	}

	/**
	 * 自定义分页 
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入storeInventoryhis")
	public R<IPage<StoreInventoryhisVO>> page(StoreInventoryhisVO storeInventoryhis, Query query) {
		IPage<StoreInventoryhisVO> pages = storeInventoryhisService.selectStoreInventoryhisPage(Condition.getPage(query), storeInventoryhis);
		return R.data(pages);
	}

	/**
	 * 新增 
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入storeInventoryhis")
	public R save(@Valid @RequestBody StoreInventoryhis storeInventoryhis) {
		return R.status(storeInventoryhisService.save(storeInventoryhis));
	}

	/**
	 * 修改 
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入storeInventoryhis")
	public R update(@Valid @RequestBody StoreInventoryhis storeInventoryhis) {
		return R.status(storeInventoryhisService.updateById(storeInventoryhis));
	}

	/**
	 * 新增或修改 
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入storeInventoryhis")
	public R submit(@Valid @RequestBody StoreInventoryhis storeInventoryhis) {
		return R.status(storeInventoryhisService.saveOrUpdate(storeInventoryhis));
	}

	
	/**
	 * 删除 
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(storeInventoryhisService.removeByIds(Func.toLongList(ids)));
	}

	
}
