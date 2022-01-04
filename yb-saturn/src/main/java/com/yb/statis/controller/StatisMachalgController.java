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
package com.yb.statis.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.statis.entity.StatisMachalg;
import com.yb.statis.service.IStatisMachalgService;
import com.yb.statis.service.IStatisMachoeeService;
import com.yb.statis.vo.StatisMachalgVO;
import com.yb.statis.wrapper.StatisMachalgWrapper;
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
 * OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisMachalg")
@Api(value = "OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）", tags = "OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）接口")
public class StatisMachalgController extends BladeController {

    private IStatisMachalgService statisMachalgService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisMachalg")
    public R<StatisMachalgVO> detail(StatisMachalg statisMachalg) {
        StatisMachalg detail = statisMachalgService.getOne(Condition.getQueryWrapper(statisMachalg));
        return R.data(StatisMachalgWrapper.build().entityVO(detail));
    }

    /**
     * 分页 OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statismachalg")
    public R<IPage<StatisMachalgVO>> list(StatisMachalg statisMachalg, Query query) {
        IPage<StatisMachalg> pages = statisMachalgService.page(Condition.getPage(query), Condition.getQueryWrapper(statisMachalg));
        return R.data(StatisMachalgWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 OEE数据信息表_yb_statis_machalge
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisMachalg")
    public R<IPage<StatisMachalgVO>> page(StatisMachalgVO statisMachalg, Query query) {
        IPage<StatisMachalgVO> pages = statisMachalgService.selectStatisMachalgPage(Condition.getPage(query), statisMachalg);
        return R.data(pages);
    }

    /**
     * 新增 OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisMachalg")
    public R save(@Valid @RequestBody StatisMachalg statisMachalg) {
        return R.status(statisMachalgService.save(statisMachalg));
    }

    /**
     * 修改 OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisMachalg")
    public R update(@Valid @RequestBody StatisMachalg statisMachalg) {
        return R.status(statisMachalgService.updateById(statisMachalg));
    }

    /**
     * 新增或修改 OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisMachalg")
    public R submit(@Valid @RequestBody StatisMachalg statisMachalg) {
        return R.status(statisMachalgService.saveOrUpdate(statisMachalg));
    }


    /**
     * 删除 OEE数据信息表_yb_statis_machalg（OEE的全量统计-状态变更及中间定时5分钟已上报记录）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(statisMachalgService.removeByIds(Func.toIntList(ids)));
    }

}
