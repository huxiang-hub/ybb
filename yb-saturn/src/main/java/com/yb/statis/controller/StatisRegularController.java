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
import com.yb.statis.entity.StatisRegular;
import com.yb.statis.service.IStatisRegularService;
import com.yb.statis.vo.StatisRegularVO;
import com.yb.statis.wrapper.StatisRegularWrapper;
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
 * OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statisregular")
@Api(value = "OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）", tags = "OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）接口")
public class StatisRegularController extends BladeController {

    private IStatisRegularService statisRegularService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入statisRegular")
    public R<StatisRegularVO> detail(StatisRegular statisRegular) {
        StatisRegular detail = statisRegularService.getOne(Condition.getQueryWrapper(statisRegular));
        return R.data(StatisRegularWrapper.build().entityVO(detail));
    }

    /**
     * 分页 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入statisRegular")
    public R<IPage<StatisRegularVO>> list(StatisRegular statisRegular, Query query) {
        IPage<StatisRegular> pages = statisRegularService.page(Condition.getPage(query), Condition.getQueryWrapper(statisRegular));
        return R.data(StatisRegularWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入statisRegular")
    public R<IPage<StatisRegularVO>> page(StatisRegularVO statisRegular, Query query) {
        IPage<StatisRegularVO> pages = statisRegularService.selectStatisRegularPage(Condition.getPage(query), statisRegular);
        return R.data(pages);
    }

    /**
     * 新增 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入statisRegular")
    public R save(@Valid @RequestBody StatisRegular statisRegular) {
        return R.status(statisRegularService.save(statisRegular));
    }

    /**
     * 修改 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入statisRegular")
    public R update(@Valid @RequestBody StatisRegular statisRegular) {
        return R.status(statisRegularService.updateById(statisRegular));
    }

    /**
     * 新增或修改 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入statisRegular")
    public R submit(@Valid @RequestBody StatisRegular statisRegular) {
        return R.status(statisRegularService.saveOrUpdate(statisRegular));
    }


    /**
     * 删除 OEE数据信息表_yb_statis_regular（定时进行oee的统计；每半个小时进行OEE汇总统计）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(statisRegularService.removeByIds(Func.toIntList(ids)));
    }


}
