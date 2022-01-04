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
package com.yb.prod.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdProcelinkVO;
import com.yb.prod.wrapper.ProdProcelinkWrapper;
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
 * 产品对应工序关联表 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/prodprocelink")
@Api(value = "产品对应工序关联表", tags = "产品对应工序关联表接口")
public class ProdProcelinkController extends BladeController {

    private IProdProcelinkService prodProcelinkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入prodProcelink")
    public R<ProdProcelinkVO> detail(ProdProcelink prodProcelink) {
        ProdProcelink detail = prodProcelinkService.getOne(Condition.getQueryWrapper(prodProcelink));
        return R.data(ProdProcelinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 产品对应工序关联表
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入prodProcelink")
    public R<IPage<ProdProcelinkVO>> list(ProdProcelink prodProcelink, Query query) {
        IPage<ProdProcelink> pages = prodProcelinkService.page(Condition.getPage(query), Condition.getQueryWrapper(prodProcelink));
        return R.data(ProdProcelinkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 产品对应工序关联表
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入prodProcelink")
    public R<IPage<ProdProcelinkVO>> page(ProdProcelinkVO prodProcelink, Query query) {
        IPage<ProdProcelinkVO> pages = prodProcelinkService.selectProdProcelinkPage(Condition.getPage(query), prodProcelink);
        return R.data(pages);
    }

    /**
     * 新增 产品对应工序关联表
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入prodProcelink")
    public R save(@Valid @RequestBody ProdProcelink prodProcelink) {
        return R.status(prodProcelinkService.save(prodProcelink));
    }

    /**
     * 修改 产品对应工序关联表
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入prodProcelink")
    public R update(@Valid @RequestBody ProdProcelink prodProcelink) {
        return R.status(prodProcelinkService.updateById(prodProcelink));
    }

    /**
     * 新增或修改 产品对应工序关联表
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入prodProcelink")
    public R submit(@Valid @RequestBody ProdProcelink prodProcelink) {
        return R.status(prodProcelinkService.saveOrUpdate(prodProcelink));
    }


    /**
     * 删除 产品对应工序关联表
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(prodProcelinkService.removeByIds(Func.toIntList(ids)));
    }

    @GetMapping("/rowSelectPr")
    @ApiOperation(value = "分页", notes = "id")
    public List<ProdPartsinfoVo> rowSelectPr(Integer id, Integer pdType) {
        List<ProdPartsinfoVo> list = prodProcelinkService.rowSelectPr(id, pdType);
        return list;
    }
    @GetMapping("/rowSelectPd")
    @ApiOperation(value = "分页", notes = "id")
    public List<ProdProcelinkVO> rowSelectPd(Integer id, Integer pdType) {
        List<ProdProcelinkVO> list = prodProcelinkService.rowSelectPd(id, pdType);
        return list;
    }

}
