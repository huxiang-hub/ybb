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
import com.yb.statis.entity.StatisMachreach;
import com.yb.statis.request.StatisMachreachListRequest;
import com.yb.statis.request.HourPlanRateRequest;
import com.yb.statis.service.IStatisMachreachService;
import com.yb.statis.vo.DayStatisMachreachListVO;
import com.yb.statis.vo.MonthStatisMachreachListVO;
import com.yb.statis.vo.StatisMachreachListVO;
import com.yb.statis.vo.StatisMachreachVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 控制器
 *
 * @author Blade
 * @since 2020-04-17
 */
@RestController
@AllArgsConstructor
@RequestMapping("/statismachreach")
@Api(value = "", tags = "接口")
public class StatisMachreachController extends BladeController {

    @Autowired
    private IStatisMachreachService statisMachreachService;

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "statisMachreach")
    public R<IPage<StatisMachreach>> list(StatisMachreachVO statisMachreach, Query query) {
        IPage<StatisMachreach> pages = statisMachreachService.selectStatisMachreach(Condition.getPage(query), statisMachreach);
        return R.data(pages);
    }

    /**
     *
     */
    @GetMapping("/details")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "详情", notes = "id")
    public R<IPage<StatisMachreachVO>> selectOne(Integer id) {
        System.out.println(id);
        IPage<StatisMachreachVO> pages = statisMachreachService.selectOne(id);
        return R.data(pages);
    }

    //这里是用于测试的
    @GetMapping("/createObject")
    public R createObject(Integer id) {
        statisMachreachService.createStatisMachreachObj(id);
        return null;
    }

    /**
     * 计划达成率
     */
    @GetMapping("/planRate")
    @ApiOperation(value = "计划达成率")
    public R<IPage<StatisMachreachListVO>> planRate(Query query, StatisMachreachListRequest request) {
        IPage<StatisMachreachListVO> voiPage = statisMachreachService.planRate(Condition.getPage(query), request);
        return R.data(voiPage);
    }

    /**
     * 获取月计划达成率
     */
    @GetMapping("/monthPlanRate")
    @ApiOperation(value = "获取月计划达成率")
    public R<List<MonthStatisMachreachListVO>> monthPlanRate(String maType,Integer wsId) {
        List<MonthStatisMachreachListVO> monthStatisMachreachListVOS = statisMachreachService.monthPlanRate(maType,wsId);
        return R.data(monthStatisMachreachListVOS);
    }

    /**
     * 获取小时计划达成率
     */
    @PostMapping("/hourPlanRate")
    @ApiOperation(value = "获取小时计划达成率")
    public R<List<DayStatisMachreachListVO>> hourPlanRate(@RequestBody @Validated HourPlanRateRequest request) {
        List<DayStatisMachreachListVO> vos = statisMachreachService.hourPlanRate(request);
        return R.data(vos);
    }
}
