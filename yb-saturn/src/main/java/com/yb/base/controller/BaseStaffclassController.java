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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.entity.BaseStaffclass;
import com.yb.base.service.IBaseStaffclassService;
import com.yb.base.vo.BaseStaffclassVO;
import com.yb.base.wrapper.BaseStaffclassWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 人员班组临时调班_yb_base_staffclass 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basestaffclass")
@Api(value = "人员班组临时调班_yb_base_staffclass", tags = "人员班组临时调班_yb_base_staffclass接口")
public class BaseStaffclassController extends BladeController {

    private IBaseStaffclassService baseStaffclassService;
    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入sf_id")
    public R<BaseStaffclassVO> detail(Integer id) {
        BaseStaffclass detail = baseStaffclassService.getById(id);
        return R.data(BaseStaffclassWrapper.build().entityVO(detail));
    }

    /**
     * 分页 人员班组临时调班_yb_base_staffclass
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseStaffclass")
    public R<IPage<BaseStaffclassVO>> list(BaseStaffclass baseStaffclass, Query query) {
        IPage<BaseStaffclass> pages = baseStaffclassService.page(Condition.getPage(query), Condition.getQueryWrapper(baseStaffclass));
        return R.data(BaseStaffclassWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 人员班组临时调班_yb_base_staffclass
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入baseStaffclass")
    public R<IPage<BaseStaffclassVO>> page(BaseStaffclassVO baseStaffclass, Query query) {
        IPage<BaseStaffclassVO> pages = baseStaffclassService.selectBaseStaffclassPage(Condition.getPage(query), baseStaffclass);
        return R.data(pages);
    }

    /**
     * 新增 人员班组临时调班_yb_base_staffclass
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "baseStaffclassVo")
    public R save(@Valid @RequestBody BaseStaffclassVO baseStaffclassVo) {
        return R.status(baseStaffclassService.saveOrUpdate(baseStaffclassVo));
    }

    /**
     * 修改 人员班组临时调班_yb_base_staffclass
     */
    @PostMapping("/updateStaffclass")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入baseStaffclass")
    public R updateStaffclass(@Valid @RequestBody BaseStaffclassVO baseStaffclassVO) {
        return R.status(baseStaffclassService.updateById(baseStaffclassVO));
    }

    /**
     * 新增或修改 人员班组临时调班_yb_base_staffclass
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseStaffclass")
    @Transactional
    public R submit(@Valid @RequestBody BaseStaffclassVO baseStaffclassVO) {
        return R.status(baseStaffclassService.saveOrUpdate(baseStaffclassVO));
    }


    /**
     * 删除 人员班组临时调班_yb_base_staffclass
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(baseStaffclassService.removeByIds(Func.toIntList(ids)));
    }

}
