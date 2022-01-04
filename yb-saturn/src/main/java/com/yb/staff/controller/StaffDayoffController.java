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
import com.yb.staff.entity.StaffDayoff;
import com.yb.staff.service.IStaffDayoffService;
import com.yb.staff.vo.StaffDayoffVO;
import com.yb.staff.wrapper.StaffDayoffWrapper;
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
 * 人事请假管理_yb_staff_dayoff 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/staffdayoff")
@Api(value = "人事请假管理_yb_staff_dayoff", tags = "人事请假管理_yb_staff_dayoff接口")
public class StaffDayoffController extends BladeController {

    private IStaffDayoffService staffDayoffService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入staffDayoff")
    public R<StaffDayoffVO> detail(StaffDayoff staffDayoff) {
        StaffDayoff detail = staffDayoffService.getOne(Condition.getQueryWrapper(staffDayoff));
        return R.data(StaffDayoffWrapper.build().entityVO(detail));
    }

    /**
     * 分页 人事请假管理_yb_staff_dayoff
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入staffDayoff")
    public R<IPage<StaffDayoffVO>> list(StaffDayoff staffDayoff, Query query) {
        IPage<StaffDayoff> pages = staffDayoffService.page(Condition.getPage(query), Condition.getQueryWrapper(staffDayoff));
        return R.data(StaffDayoffWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 人事请假管理_yb_staff_dayoff
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入staffDayoff")
    public R<IPage<StaffDayoffVO>> page(StaffDayoffVO staffDayoff, Query query) {
        IPage<StaffDayoffVO> pages = staffDayoffService.selectStaffDayoffPage(Condition.getPage(query), staffDayoff);
        return R.data(pages);
    }

    /**
     * 新增 人事请假管理_yb_staff_dayoff
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入staffDayoff")
    public R save(@Valid @RequestBody StaffDayoff staffDayoff) {
        return R.status(staffDayoffService.save(staffDayoff));
    }

    /**
     * 修改 人事请假管理_yb_staff_dayoff
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入staffDayoff")
    public R update(@Valid @RequestBody StaffDayoff staffDayoff) {
        return R.status(staffDayoffService.updateById(staffDayoff));
    }

    /**
     * 新增或修改 人事请假管理_yb_staff_dayoff
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入staffDayoff")
    public R submit(@Valid @RequestBody StaffDayoff staffDayoff) {
        return R.status(staffDayoffService.saveOrUpdate(staffDayoff));
    }


    /**
     * 删除 人事请假管理_yb_staff_dayoff
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(staffDayoffService.removeByIds(Func.toIntList(ids)));
    }


}
