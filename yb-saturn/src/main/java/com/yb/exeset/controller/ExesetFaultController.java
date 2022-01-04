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
import com.yb.exeset.entity.ExesetFault;
import com.yb.exeset.service.IExesetFaultService;
import com.yb.exeset.vo.ExesetFaultVO;
import com.yb.exeset.wrapper.ExesetFaultWrapper;
import com.yb.machine.service.IMachineMixboxService;
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
 * 故障停机设置_yb_exeset_fault 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exesetfault")
@Api(value = "故障停机设置_yb_exeset_fault", tags = "故障停机设置_yb_exeset_fault接口")
public class ExesetFaultController extends BladeController {

    private IExesetFaultService exesetFaultService;

    private IMachineMixboxService mixboxService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入exesetFault")
    public R<ExesetFaultVO> detail(ExesetFault exesetFault) {
        ExesetFault detail = exesetFaultService.getOne(Condition.getQueryWrapper(exesetFault));
        return R.data(ExesetFaultWrapper.build().entityVO(detail));
    }

    /**
     * 分页 故障停机设置_yb_exeset_fault
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入exesetFault")
    public R<IPage<ExesetFaultVO>> list(ExesetFault exesetFault, Query query) {
        IPage<ExesetFault> pages = exesetFaultService.page(Condition.getPage(query), Condition.getQueryWrapper(exesetFault));
        return R.data(ExesetFaultWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 故障停机设置_yb_exeset_fault
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入exesetFault")
    public R<IPage<ExesetFaultVO>> page(ExesetFaultVO exesetFault, Query query) {
        IPage<ExesetFaultVO> pages = exesetFaultService.selectExesetFaultPage(Condition.getPage(query), exesetFault);
        return R.data(pages);
    }

    /**
     * 新增 故障停机设置_yb_exeset_fault
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入exesetFault")
    public R save(@Valid @RequestBody ExesetFault exesetFault) {
        return R.status(exesetFaultService.save(exesetFault));
    }

    /**
     * 修改 故障停机设置_yb_exeset_fault
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入exesetFault")
    public R update(@Valid @RequestBody ExesetFault exesetFault) {
        return R.status(exesetFaultService.updateById(exesetFault));
    }

    /**
     * 新增或修改 故障停机设置_yb_exeset_fault
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入exesetFault")
    public R submit(@Valid @RequestBody ExesetFault exesetFault) {
        return R.status(exesetFaultService.saveOrUpdate(exesetFault));
    }


    /**
     * 删除 故障停机设置_yb_exeset_fault
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(exesetFaultService.removeByIds(Func.toIntList(ids)));
    }

}
