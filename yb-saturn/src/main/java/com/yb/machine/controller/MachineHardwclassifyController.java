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
import com.yb.machine.entity.MachineHardwclassify;
import com.yb.machine.service.IMachineHardwclassifyService;
import com.yb.machine.vo.MachineHardwclassifyVO;
import com.yb.machine.wrapper.MachineHardwclassifyWrapper;
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
 * 硬件设备型号_yb_machine_hardwclassify 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinehardwclassify")
@Api(value = "硬件设备型号_yb_machine_hardwclassify", tags = "硬件设备型号_yb_machine_hardwclassify接口")
public class MachineHardwclassifyController extends BladeController {

    private IMachineHardwclassifyService machineHardwclassifyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineHardwclassify")
    public R<MachineHardwclassifyVO> detail(MachineHardwclassify machineHardwclassify) {
        MachineHardwclassify detail = machineHardwclassifyService.getOne(Condition.getQueryWrapper(machineHardwclassify));
        return R.data(MachineHardwclassifyWrapper.build().entityVO(detail));
    }

    /**
     * 分页 硬件设备型号_yb_machine_hardwclassify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineHardwclassify")
    public R<IPage<MachineHardwclassifyVO>> list(MachineHardwclassify machineHardwclassify, Query query) {
        IPage<MachineHardwclassify> pages = machineHardwclassifyService.page(Condition.getPage(query), Condition.getQueryWrapper(machineHardwclassify));
        return R.data(MachineHardwclassifyWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 硬件设备型号_yb_machine_hardwclassify
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineHardwclassify")
    public R<IPage<MachineHardwclassifyVO>> page(MachineHardwclassifyVO machineHardwclassify, Query query) {
        IPage<MachineHardwclassifyVO> pages = machineHardwclassifyService.selectMachineHardwclassifyPage(Condition.getPage(query), machineHardwclassify);
        return R.data(pages);
    }

    /**
     * 新增 硬件设备型号_yb_machine_hardwclassify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineHardwclassify")
    public R save(@Valid @RequestBody MachineHardwclassify machineHardwclassify) {
        return R.status(machineHardwclassifyService.save(machineHardwclassify));
    }

    /**
     * 修改 硬件设备型号_yb_machine_hardwclassify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineHardwclassify")
    public R update(@Valid @RequestBody MachineHardwclassify machineHardwclassify) {
        return R.status(machineHardwclassifyService.updateById(machineHardwclassify));
    }

    /**
     * 新增或修改 硬件设备型号_yb_machine_hardwclassify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineHardwclassify")
    public R submit(@Valid @RequestBody MachineHardwclassify machineHardwclassify) {
        return R.status(machineHardwclassifyService.saveOrUpdate(machineHardwclassify));
    }


    /**
     * 删除 硬件设备型号_yb_machine_hardwclassify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(machineHardwclassifyService.removeByIds(Func.toIntList(ids)));
    }


}
