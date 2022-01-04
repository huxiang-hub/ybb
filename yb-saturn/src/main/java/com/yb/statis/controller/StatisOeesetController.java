/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.statis.entity.StatisOeeset;
import com.yb.statis.service.IStatisOeesetService;
import com.yb.statis.vo.StatisOeesetVO;
import com.yb.statis.wrapper.StatisOeesetWrapper;
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
 *  控制器
 *
 * @author Blade
 * @since 2020-04-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisoeeset")
@Api(value = "", tags = "接口")
public class StatisOeesetController extends BladeController {

	private IStatisOeesetService statisOeesetService;

	/**
	* 详情
	*/
	@GetMapping("/detail")
    @ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入statisOeeset")
	public R<StatisOeesetVO> detail(StatisOeeset statisOeeset) {
		StatisOeeset detail = statisOeesetService.getOne(Condition.getQueryWrapper(statisOeeset));
		return R.data(StatisOeesetWrapper.build().entityVO(detail));
	}

	/**
	* 分页 
	*/
	@GetMapping("/list")
    @ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入statisOeeset")
	public R<IPage<StatisOeesetVO>> list(StatisOeeset statisOeeset, Query query) {
		IPage<StatisOeeset> pages = statisOeesetService.page(Condition.getPage(query), Condition.getQueryWrapper(statisOeeset));
		return R.data(StatisOeesetWrapper.build().pageVO(pages));
	}

	/**
	* 自定义分页 
	*/
	@GetMapping("/page")
    @ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入statisOeeset")
	public R<IPage<StatisOeesetVO>> page(StatisOeesetVO statisOeeset, Query query) {
		IPage<StatisOeesetVO> pages = statisOeesetService.selectStatisOeesetPage(Condition.getPage(query), statisOeeset);
		return R.data(pages);
	}

	/**
	* 新增 
	*/
	@PostMapping("/save")
    @ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入statisOeeset")
	public R save(@Valid @RequestBody StatisOeeset statisOeeset) {
		return R.status(statisOeesetService.save(statisOeeset));
	}

	/**
	* 修改 
	*/
	@PostMapping("/update")
    @ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入statisOeeset")
	public R update(@Valid @RequestBody StatisOeeset statisOeeset) {
		return R.status(statisOeesetService.updateById(statisOeeset));
	}

	/**
	* 新增或修改 
	*/
	@PostMapping("/submit")
    @ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入statisOeeset")
	public R submit(@Valid @RequestBody StatisOeeset statisOeeset) {
		return R.status(statisOeesetService.saveOrUpdate(statisOeeset));
	}

	
	/**
	* 删除 
	*/
	@PostMapping("/remove")
    @ApiOperationSupport(order = 7)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(statisOeesetService.removeByIds(Func.toIntList(ids)));
	}

	
}
