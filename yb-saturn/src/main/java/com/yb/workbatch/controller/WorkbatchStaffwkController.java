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
package com.yb.workbatch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.workbatch.entity.WorkbatchStaffwk;
import com.yb.workbatch.service.IWorkbatchStaffwkService;
import com.yb.workbatch.vo.WorkbatchStaffwkVO;
import com.yb.workbatch.wrapper.WorkbatchStaffwkWrapper;
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
 * 人员排班表_yb_workbatch_staffwk 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/workbatchstaffwk")
@Api(value = "人员排班表_yb_workbatch_staffwk", tags = "人员排班表_yb_workbatch_staffwk接口")
public class WorkbatchStaffwkController extends BladeController {

    private IWorkbatchStaffwkService workbatchStaffwkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入workbatchStaffwk")
    public R<WorkbatchStaffwkVO> detail(WorkbatchStaffwk workbatchStaffwk) {
        WorkbatchStaffwk detail = workbatchStaffwkService.getOne(Condition.getQueryWrapper(workbatchStaffwk));
        return R.data(WorkbatchStaffwkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 人员排班表_yb_workbatch_staffwk
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入workbatchStaffwk")
    public R<IPage<WorkbatchStaffwkVO>> list(WorkbatchStaffwk workbatchStaffwk, Query query) {
        IPage<WorkbatchStaffwk> pages = workbatchStaffwkService.page(Condition.getPage(query), Condition.getQueryWrapper(workbatchStaffwk));
        return R.data(WorkbatchStaffwkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 人员排班表_yb_workbatch_staffwk
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入workbatchStaffwk")
    public R<IPage<WorkbatchStaffwkVO>> page(WorkbatchStaffwkVO workbatchStaffwk, Query query) {
        IPage<WorkbatchStaffwkVO> pages = workbatchStaffwkService.selectWorkbatchStaffwkPage(Condition.getPage(query), workbatchStaffwk);
        return R.data(pages);
    }

    /**
     * 新增 人员排班表_yb_workbatch_staffwk
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入workbatchStaffwk")
    public R save(@Valid @RequestBody WorkbatchStaffwk workbatchStaffwk) {
        return R.status(workbatchStaffwkService.save(workbatchStaffwk));
    }

    /**
     * 修改 人员排班表_yb_workbatch_staffwk
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入workbatchStaffwk")
    public R update(@Valid @RequestBody WorkbatchStaffwk workbatchStaffwk) {
        return R.status(workbatchStaffwkService.updateById(workbatchStaffwk));
    }

    /**
     * 新增或修改 人员排班表_yb_workbatch_staffwk
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入workbatchStaffwk")
    public R submit(@Valid @RequestBody WorkbatchStaffwk workbatchStaffwk) {
        return R.status(workbatchStaffwkService.saveOrUpdate(workbatchStaffwk));
    }


    /**
     * 删除 人员排班表_yb_workbatch_staffwk
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(workbatchStaffwkService.removeByIds(Func.toIntList(ids)));
    }


}
