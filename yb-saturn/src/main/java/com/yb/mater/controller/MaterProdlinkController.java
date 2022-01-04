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
package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.mater.wrapper.MaterProdlinkWrapper;
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
 * 产品物料关系（materiel） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/materprodlink")
@Api(value = "产品物料关系（materiel）", tags = "产品物料关系（materiel）接口")
public class MaterProdlinkController extends BladeController {

    private IMaterProdlinkService materProdlinkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入materProdlink")
    public R<MaterProdlinkVO> detail(MaterProdlink materProdlink) {
        MaterProdlink detail = materProdlinkService.getOne(Condition.getQueryWrapper(materProdlink));
        return R.data(MaterProdlinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 产品物料关系（materiel）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入materProdlink")
    public R<IPage<MaterProdlinkVO>> list(MaterProdlink materProdlink, Query query) {
        IPage<MaterProdlink> pages = materProdlinkService.page(Condition.getPage(query), Condition.getQueryWrapper(materProdlink));
        return R.data(MaterProdlinkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 产品物料关系（materiel）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入materProdlink")
    public R<IPage<MaterProdlinkVO>> page(MaterProdlinkVO materProdlink, Query query) {
        IPage<MaterProdlinkVO> pages = materProdlinkService.selectMaterProdlinkPage(Condition.getPage(query), materProdlink);
        return R.data(pages);
    }

    /**
     * 新增 产品物料关系（materiel）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入materProdlink")
    public R save(@Valid @RequestBody MaterProdlink materProdlink) {
        return R.status(materProdlinkService.save(materProdlink));
    }

    /**
     * 修改 产品物料关系（materiel）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入materProdlink")
    public R update(@Valid @RequestBody MaterProdlink materProdlink) {
        return R.status(materProdlinkService.updateById(materProdlink));
    }

    /**
     * 新增或修改 产品物料关系（materiel）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入materProdlink")
    public R submit(@Valid @RequestBody MaterProdlink materProdlink) {
        return R.status(materProdlinkService.saveOrUpdate(materProdlink));
    }


    /**
     * 删除 产品物料关系（materiel）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(materProdlinkService.removeByIds(Func.toIntList(ids)));
    }


}
