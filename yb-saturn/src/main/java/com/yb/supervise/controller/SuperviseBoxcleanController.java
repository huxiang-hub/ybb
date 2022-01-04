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
import com.yb.supervise.entity.SuperviseBoxclean;
import com.yb.supervise.service.ISuperviseBoxcleanService;
import com.yb.supervise.vo.SuperviseBoxcleanVO;
import com.yb.supervise.wrapper.SuperviseBoxcleanWrapper;
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
 * 设备清零日志表 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/superviseboxclean")
@Api(value = "设备清零日志表", tags = "设备清零日志表接口")
public class SuperviseBoxcleanController extends BladeController {

    private ISuperviseBoxcleanService superviseBoxcleanService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入superviseBoxclean")
    public R<SuperviseBoxcleanVO> detail(SuperviseBoxclean superviseBoxclean) {
        SuperviseBoxclean detail = superviseBoxcleanService.getOne(Condition.getQueryWrapper(superviseBoxclean));
        return R.data(SuperviseBoxcleanWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备清零日志表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入superviseBoxclean")
    public R<IPage<SuperviseBoxcleanVO>> list(SuperviseBoxclean superviseBoxclean, Query query) {
        IPage<SuperviseBoxclean> pages = superviseBoxcleanService.page(Condition.getPage(query), Condition.getQueryWrapper(superviseBoxclean));
        return R.data(SuperviseBoxcleanWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备清零日志表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入superviseBoxclean")
    public R<IPage<SuperviseBoxcleanVO>> page(SuperviseBoxcleanVO superviseBoxclean, Query query) {
        IPage<SuperviseBoxcleanVO> pages = superviseBoxcleanService.selectSuperviseBoxcleanPage(Condition.getPage(query), superviseBoxclean);
        return R.data(pages);
    }

    /**
     * 新增 设备清零日志表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入superviseBoxclean")
    public R save(@Valid @RequestBody SuperviseBoxclean superviseBoxclean) {
        return R.status(superviseBoxcleanService.save(superviseBoxclean));
    }

    /**
     * 修改 设备清零日志表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入superviseBoxclean")
    public R update(@Valid @RequestBody SuperviseBoxclean superviseBoxclean) {
        return R.status(superviseBoxcleanService.updateById(superviseBoxclean));
    }

    /**
     * 新增或修改 设备清零日志表
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入superviseBoxclean")
    public R submit(@Valid @RequestBody SuperviseBoxclean superviseBoxclean) {
        return R.status(superviseBoxcleanService.saveOrUpdate(superviseBoxclean));
    }


    /**
     * 删除 设备清零日志表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(superviseBoxcleanService.removeByIds(Func.toIntList(ids)));
    }


}
