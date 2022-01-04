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
package com.yb.machine.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.machine.entity.MachineScreen;
import com.yb.machine.service.IMachineScreenService;
import com.yb.machine.vo.MachineScreenVO;
import com.yb.machine.wrapper.MachineScreenWrapper;
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
 * 设备关联屏幕（主系统同步数据） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinescreen")
@Api(value = "设备关联屏幕（主系统同步数据）", tags = "设备关联屏幕（主系统同步数据）接口")
public class MachineScreenController extends BladeController {

    private IMachineScreenService machineScreenService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineScreen")
    public R<MachineScreenVO> detail(MachineScreen machineScreen) {
        MachineScreen detail = machineScreenService.getOne(Condition.getQueryWrapper(machineScreen));
        return R.data(MachineScreenWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备关联屏幕（主系统同步数据）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineScreen")
    public R<IPage<MachineScreenVO>> list(MachineScreen machineScreen, Query query) {
        IPage<MachineScreen> pages = machineScreenService.page(Condition.getPage(query), Condition.getQueryWrapper(machineScreen));
        return R.data(MachineScreenWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备关联屏幕（主系统同步数据）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineScreen")
    public R<IPage<MachineScreenVO>> page(MachineScreenVO machineScreen, Query query) {
        IPage<MachineScreenVO> pages = machineScreenService.selectMachineScreenPage(Condition.getPage(query), machineScreen);
        return R.data(pages);
    }

    /**
     * 新增 设备关联屏幕（主系统同步数据）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineScreen")
    public R save(@Valid @RequestBody MachineScreen machineScreen) {
        return R.status(machineScreenService.save(machineScreen));
    }

    /**
     * 修改 设备关联屏幕（主系统同步数据）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineScreen")
    public R update(@Valid @RequestBody MachineScreen machineScreen) {
        return R.status(machineScreenService.updateById(machineScreen));
    }

    /**
     * 新增或修改 设备关联屏幕（主系统同步数据）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineScreen")
    public R submit(@Valid @RequestBody MachineScreen machineScreen) {
        return R.status(machineScreenService.saveOrUpdate(machineScreen));
    }


    /**
     * 删除 设备关联屏幕（主系统同步数据）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(machineScreenService.removeByIds(Func.toIntList(ids)));
    }


}
