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
package com.yb.supervise.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.supervise.entity.SuperviseWarning;
import com.yb.supervise.service.ISuperviseWarningService;
import com.yb.supervise.vo.SuperviseWarningVO;
import com.yb.supervise.wrapper.SuperviseWarningWrapper;
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
 * 预警定义_yb_supervise_warning 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/supervisewarning")
@Api(value = "预警定义_yb_supervise_warning", tags = "预警定义_yb_supervise_warning接口")
public class SuperviseWarningController extends BladeController {

    private ISuperviseWarningService superviseWarningService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseWarning")
    public R<SuperviseWarningVO> detail(SuperviseWarning superviseWarning) {
        SuperviseWarning detail = superviseWarningService.getOne(Condition.getQueryWrapper(superviseWarning));
        return R.data(SuperviseWarningWrapper.build().entityVO(detail));
    }

    /**
     * 分页 预警定义_yb_supervise_warning
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入superviseWarning")
    public R<IPage<SuperviseWarningVO>> list(SuperviseWarning superviseWarning, Query query) {
        IPage<SuperviseWarning> pages = superviseWarningService.page(Condition.getPage(query), Condition.getQueryWrapper(superviseWarning));
        return R.data(SuperviseWarningWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 预警定义_yb_supervise_warning
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseWarning")
    public R<IPage<SuperviseWarningVO>> page(SuperviseWarningVO superviseWarning, Query query) {
        IPage<SuperviseWarningVO> pages = superviseWarningService.selectSuperviseWarningPage(Condition.getPage(query), superviseWarning);
        return R.data(pages);
    }

    /**
     * 新增 预警定义_yb_supervise_warning
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseWarning")
    public R save(@Valid @RequestBody SuperviseWarning superviseWarning) {
        return R.status(superviseWarningService.save(superviseWarning));
    }

    /**
     * 修改 预警定义_yb_supervise_warning
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseWarning")
    public R update(@Valid @RequestBody SuperviseWarning superviseWarning) {
        return R.status(superviseWarningService.updateById(superviseWarning));
    }

    /**
     * 新增或修改 预警定义_yb_supervise_warning
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseWarning")
    public R submit(@Valid @RequestBody SuperviseWarning superviseWarning) {
        return R.status(superviseWarningService.saveOrUpdate(superviseWarning));
    }


    /**
     * 删除 预警定义_yb_supervise_warning
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseWarningService.removeByIds(Func.toIntList(ids)));
    }


}
