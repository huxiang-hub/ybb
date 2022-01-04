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
import com.yb.statis.entity.StatisOrdinfo;
import com.yb.statis.service.IStatisOrdinfoService;
import com.yb.statis.vo.StatisOrdinfoVO;
import com.yb.statis.wrapper.StatisOrdinfoWrapper;
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
 * OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisordinfo")
@Api(value = "OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表", tags = "OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表接口")
public class StatisOrdinfoController extends BladeController {

    private IStatisOrdinfoService statisOrdinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisOrdinfo")
    public R<StatisOrdinfoVO> detail(StatisOrdinfo statisOrdinfo) {
        StatisOrdinfo detail = statisOrdinfoService.getOne(Condition.getQueryWrapper(statisOrdinfo));
        return R.data(StatisOrdinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statisOrdinfo")
    public R<IPage<StatisOrdinfoVO>> list(StatisOrdinfo statisOrdinfo, Query query) {
        IPage<StatisOrdinfo> pages = statisOrdinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(statisOrdinfo));
        return R.data(StatisOrdinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisOrdinfo")
    public R<IPage<StatisOrdinfoVO>> page(StatisOrdinfoVO statisOrdinfo, Query query) {
        IPage<StatisOrdinfoVO> pages = statisOrdinfoService.selectStatisOrdinfoPage(Condition.getPage(query), statisOrdinfo);
        return R.data(pages);
    }

    /**
     * 新增 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisOrdinfo")
    public R save(@Valid @RequestBody StatisOrdinfo statisOrdinfo) {
        return R.status(statisOrdinfoService.save(statisOrdinfo));
    }

    /**
     * 修改 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisOrdinfo")
    public R update(@Valid @RequestBody StatisOrdinfo statisOrdinfo) {
        return R.status(statisOrdinfoService.updateById(statisOrdinfo));
    }

    /**
     * 新增或修改 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisOrdinfo")
    public R submit(@Valid @RequestBody StatisOrdinfo statisOrdinfo) {
        return R.status(statisOrdinfoService.saveOrUpdate(statisOrdinfo));
    }

    /**
     * 删除 OEE数据信息表_yb_statis_ordinfo（订单统计基础表信息：订单、批次、设备、人员、班次）日期时间在基础表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(statisOrdinfoService.removeByIds(Func.toIntList(ids)));
    }
}
