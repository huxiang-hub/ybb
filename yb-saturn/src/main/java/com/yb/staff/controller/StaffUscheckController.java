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
package com.yb.staff.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.staff.entity.StaffUscheck;
import com.yb.staff.service.IStaffUscheckService;
import com.yb.staff.vo.StaffUscheckVO;
import com.yb.staff.wrapper.StaffUscheckWrapper;
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
 * 考勤表_yb_staff_uscheck 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/staffuscheck")
@Api(value = "考勤表_yb_staff_uscheck", tags = "考勤表_yb_staff_uscheck接口")
public class StaffUscheckController extends BladeController {

    private IStaffUscheckService staffUscheckService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入staffUscheck")
    public R<StaffUscheckVO> detail(StaffUscheck staffUscheck) {
        StaffUscheck detail = staffUscheckService.getOne(Condition.getQueryWrapper(staffUscheck));
        return R.data(StaffUscheckWrapper.build().entityVO(detail));
    }

    /**
     * 分页 考勤表_yb_staff_uscheck
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入staffUscheck")
    public R<IPage<StaffUscheckVO>> list(StaffUscheck staffUscheck, Query query) {
        IPage<StaffUscheck> pages = staffUscheckService.page(Condition.getPage(query), Condition.getQueryWrapper(staffUscheck));
        return R.data(StaffUscheckWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 考勤表_yb_staff_uscheck
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入staffUscheck")
    public R<IPage<StaffUscheckVO>> page(StaffUscheckVO staffUscheck, Query query) {
        IPage<StaffUscheckVO> pages = staffUscheckService.selectStaffUscheckPage(Condition.getPage(query), staffUscheck);
        return R.data(pages);
    }

    /**
     * 新增 考勤表_yb_staff_uscheck
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入staffUscheck")
    public R save(@Valid @RequestBody StaffUscheck staffUscheck) {
        return R.status(staffUscheckService.save(staffUscheck));
    }

    /**
     * 修改 考勤表_yb_staff_uscheck
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入staffUscheck")
    public R update(@Valid @RequestBody StaffUscheck staffUscheck) {
        return R.status(staffUscheckService.updateById(staffUscheck));
    }

    /**
     * 新增或修改 考勤表_yb_staff_uscheck
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入staffUscheck")
    public R submit(@Valid @RequestBody StaffUscheck staffUscheck) {
        return R.status(staffUscheckService.saveOrUpdate(staffUscheck));
    }


    /**
     * 删除 考勤表_yb_staff_uscheck
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(staffUscheckService.removeByIds(Func.toIntList(ids)));
    }


}
