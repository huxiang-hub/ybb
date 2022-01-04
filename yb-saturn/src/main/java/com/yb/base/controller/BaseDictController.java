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
package com.yb.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseDict;
import com.yb.base.service.IBaseDictService;
import com.yb.base.vo.BaseDictVO;
import com.yb.base.wrapper.BaseDictWrapper;
import io.protostuff.Request;
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
import java.util.List;

/**
 * 系统数据字典_yb_base_dict 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basedict")
@Api(value = "系统数据字典_yb_base_dict", tags = "系统数据字典_yb_base_dict接口")
public class BaseDictController extends BladeController {

    private IBaseDictService baseDictService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入baseDict")
    public R<BaseDictVO> detail(BaseDict baseDict) {
        BaseDict detail = baseDictService.getOne(Condition.getQueryWrapper(baseDict));
        return R.data(BaseDictWrapper.build().entityVO(detail));
    }

    /**
     * 分页 系统数据字典_yb_base_dict
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseDict")
    public R<IPage<BaseDictVO>> list(BaseDict baseDict, Query query) {
        IPage<BaseDict> pages = baseDictService.page(Condition.getPage(query), Condition.getQueryWrapper(baseDict));
        return R.data(BaseDictWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 系统数据字典_yb_base_dict
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入baseDict")
    public R<IPage<BaseDictVO>> page(BaseDictVO baseDict, Query query) {
        IPage<BaseDictVO> pages = baseDictService.selectBaseDictPage(Condition.getPage(query), baseDict);
        return R.data(pages);
    }

    /**
     * 新增 系统数据字典_yb_base_dict
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入baseDict")
    public R save(@Valid @RequestBody BaseDict baseDict) {
        return R.status(baseDictService.save(baseDict));
    }

    /**
     * 修改 系统数据字典_yb_base_dict
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入baseDict")
    public R update(@Valid @RequestBody BaseDict baseDict) {
        return R.status(baseDictService.updateById(baseDict));
    }

    /**
     * 新增或修改 系统数据字典_yb_base_dict
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseDict")
    public R submit(@Valid @RequestBody BaseDict baseDict) {
        return R.status(baseDictService.saveOrUpdate(baseDict));
    }


    /**
     * 删除 系统数据字典_yb_base_dict
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(baseDictService.removeByIds(Func.toIntList(ids)));
    }
    /**
     * 系统数据字典_yb_base_dict
     */
    @RequestMapping("/baseDictList")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "查询数据字典列表", notes = "传入dtType")
    public R baseDictList(String dtType) {
        List<BaseDict> baseDictList = baseDictService.list(new QueryWrapper<BaseDict>().eq("dt_type", dtType).eq("isdel", 0).ne("p_id", 0));
        return R.data(baseDictList);
    }


}
