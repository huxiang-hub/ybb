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
import com.yb.machine.entity.MachineClassify;
import com.yb.machine.service.IMachineClassifyService;
import com.yb.machine.vo.MachineClassifyVO;
import com.yb.machine.wrapper.MachineClassifyWrapper;
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
 * 设备型号_yb_mach_classify 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machineclassify")
@Api(value = "设备型号_yb_mach_classify", tags = "设备型号_yb_mach_classify接口")
public class MachineClassifyController extends BladeController {

    private IMachineClassifyService machineClassifyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineClassify")
    public R<MachineClassifyVO> detail(MachineClassify machineClassify) {
        MachineClassify detail = machineClassifyService.getOne(Condition.getQueryWrapper(machineClassify));
        return R.data(MachineClassifyWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备型号_yb_mach_classify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineClassify")
    public R<IPage<MachineClassifyVO>> list(MachineClassify machineClassify, Query query) {
        IPage<MachineClassify> pages = machineClassifyService.page(Condition.getPage(query), Condition.getQueryWrapper(machineClassify));
        return R.data(MachineClassifyWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备型号_yb_mach_classify
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineClassify")
    public R<IPage<MachineClassifyVO>> page(MachineClassifyVO machineClassify, Query query) {
        IPage<MachineClassifyVO> pages = machineClassifyService.selectMachineClassifyPage(Condition.getPage(query), machineClassify);
        return R.data(pages);
    }

    /**
     * 新增 设备型号_yb_mach_classify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineClassify")
    public R save(@Valid @RequestBody MachineClassify machineClassify) {
        return R.status(machineClassifyService.save(machineClassify));
    }

    /**
     * 修改 设备型号_yb_mach_classify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineClassify")
    public R update(@Valid @RequestBody MachineClassify machineClassify) {
        return R.status(machineClassifyService.updateById(machineClassify));
    }

    /**
     * 新增或修改 设备型号_yb_mach_classify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineClassify")
    public R submit(@Valid @RequestBody MachineClassify machineClassify) {
        return R.status(machineClassifyService.saveOrUpdate(machineClassify));
    }


    /**
     * 删除 设备型号_yb_mach_classify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(machineClassifyService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 查找所有的品牌信息
     * @return
     */
    @GetMapping("/findBrand")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R findBrand() {
        return R.data(machineClassifyService.getAllBrand());
    }

    /**
     * 查找所有的品牌信息
     * @return
     */
    @GetMapping("/findBrandInfoById")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R findBrandInfoById(Integer id) {
        machineClassifyService.getById(id);
        return R.data(machineClassifyService.getById(id));
    }
}
