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
package com.yb.exeset.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.exeset.entity.ExesetQuality;
import com.yb.exeset.service.IExesetQualityService;
import com.yb.exeset.vo.ExesetQualityVO;
import com.yb.exeset.wrapper.ExesetQualityWrapper;
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
 * 质量巡检设置_yb_exeset_quality 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exesetquality")
@Api(value = "质量巡检设置_yb_exeset_quality", tags = "质量巡检设置_yb_exeset_quality接口")
public class ExesetQualityController extends BladeController {

    private IExesetQualityService exesetQualityService;


    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入exesetQuality")
    public R<ExesetQualityVO> detail(ExesetQuality exesetQuality) {
        ExesetQuality detail = exesetQualityService.getOne(Condition.getQueryWrapper(exesetQuality));
        return R.data(ExesetQualityWrapper.build().entityVO(detail));
    }

    /**
     * 分页 质量巡检设置_yb_exeset_quality
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入exesetQuality")
    public R<IPage<ExesetQualityVO>> list(ExesetQuality exesetQuality, Query query) {
        IPage<ExesetQuality> pages = exesetQualityService.page(Condition.getPage(query), Condition.getQueryWrapper(exesetQuality));
        return R.data(ExesetQualityWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 质量巡检设置_yb_exeset_quality
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入exesetQuality")
    public R<IPage<ExesetQualityVO>> page(ExesetQualityVO exesetQuality, Query query) {
        IPage<ExesetQualityVO> pages = exesetQualityService.selectExesetQualityPage(Condition.getPage(query), exesetQuality);
        return R.data(pages);
    }

    /**
     * 新增 质量巡检设置_yb_exeset_quality
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入exesetQuality")
    public R save(@Valid @RequestBody ExesetQuality exesetQuality) {
        return R.status(exesetQualityService.save(exesetQuality));
    }

    /**
     * 修改 质量巡检设置_yb_exeset_quality
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入exesetQuality")
    public R update(@Valid @RequestBody ExesetQuality exesetQuality) {
        return R.status(exesetQualityService.updateById(exesetQuality));
    }

    /**
     * 新增或修改 质量巡检设置_yb_exeset_quality
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入exesetQuality")
    public R submit(@Valid @RequestBody ExesetQuality exesetQuality) {
        return R.status(exesetQualityService.saveOrUpdate(exesetQuality));
    }


    /**
     * 删除 质量巡检设置_yb_exeset_quality
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(exesetQualityService.removeByIds(Func.toIntList(ids)));
    }

}
