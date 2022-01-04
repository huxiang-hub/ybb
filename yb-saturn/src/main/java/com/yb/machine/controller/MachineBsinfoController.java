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
import com.yb.machine.entity.MachineBsinfo;
import com.yb.machine.service.IMachineBsinfoService;
import com.yb.machine.vo.MachineBsinfoVO;
import com.yb.machine.wrapper.MachineBsinfoWrapper;
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
 * 设备扩展信息_yb_machine_bsinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinebsinfo")
@Api(value = "设备扩展信息_yb_machine_bsinfo", tags = "设备扩展信息_yb_machine_bsinfo接口")
public class MachineBsinfoController extends BladeController {

    private IMachineBsinfoService machineBsinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineBsinfo")
    public R<MachineBsinfoVO> detail(MachineBsinfo machineBsinfo) {
        MachineBsinfo detail = machineBsinfoService.getOne(Condition.getQueryWrapper(machineBsinfo));
        return R.data(MachineBsinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 设备扩展信息_yb_machine_bsinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineBsinfo")
    public R<IPage<MachineBsinfoVO>> list(MachineBsinfo machineBsinfo, Query query) {
        IPage<MachineBsinfo> pages = machineBsinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(machineBsinfo));
        return R.data(MachineBsinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备扩展信息_yb_machine_bsinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineBsinfo")
    public R<IPage<MachineBsinfoVO>> page(MachineBsinfoVO machineBsinfo, Query query) {
        IPage<MachineBsinfoVO> pages = machineBsinfoService.selectMachineBsinfoPage(Condition.getPage(query), machineBsinfo);
        return R.data(pages);
    }

    /**
     * 新增 设备扩展信息_yb_machine_bsinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineBsinfo")
    public R save(@Valid @RequestBody MachineBsinfo machineBsinfo) {
        return R.status(machineBsinfoService.save(machineBsinfo));
    }

    /**
     * 修改 设备扩展信息_yb_machine_bsinfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineBsinfo")
    public R update(@Valid @RequestBody MachineBsinfo machineBsinfo) {
        return R.status(machineBsinfoService.updateById(machineBsinfo));
    }

    /**
     * 新增或修改 设备扩展信息_yb_machine_bsinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineBsinfo")
    public R submit(@Valid @RequestBody MachineBsinfo machineBsinfo) {
        return R.status(machineBsinfoService.saveOrUpdate(machineBsinfo));
    }


    /**
     * 删除 设备扩展信息_yb_machine_bsinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(machineBsinfoService.removeByIds(Func.toIntList(ids)));
    }


}
