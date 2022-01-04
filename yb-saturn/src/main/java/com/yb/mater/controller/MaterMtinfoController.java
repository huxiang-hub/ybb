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
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.service.IMaterMtinfoService;
import com.yb.mater.vo.MaterMtinfoVO;
import com.yb.mater.wrapper.MaterMtinfoWrapper;
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
 * 物料列表_yb_mater_matinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/matermtinfo")
@Api(value = "物料列表_yb_mater_matinfo", tags = "物料列表_yb_mater_matinfo接口")
public class MaterMtinfoController extends BladeController {

    private IMaterMtinfoService materMtinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入materMtinfo")
    public R<MaterMtinfoVO> detail(MaterMtinfo materMtinfo) {
        MaterMtinfo detail = materMtinfoService.getOne(Condition.getQueryWrapper(materMtinfo));
        return R.data(MaterMtinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 物料列表_yb_mater_matinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入materMtinfo")
    public R<IPage<MaterMtinfoVO>> list(MaterMtinfo materMtinfo, Query query) {
        materMtinfo.setSize(null);
        IPage<MaterMtinfo> pages = materMtinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(materMtinfo));
        return R.data(MaterMtinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 物料列表_yb_mater_matinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入materMtinfo")
    public R<IPage<MaterMtinfoVO>> page(MaterMtinfoVO materMtinfo, Query query) {
        IPage<MaterMtinfoVO> pages = materMtinfoService.selectMaterMtinfoPage(Condition.getPage(query), materMtinfo);
        return R.data(pages);
    }

    /**
     * 新增 物料列表_yb_mater_matinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入materMtinfo")
    public R save(@Valid @RequestBody MaterMtinfo materMtinfo) {
        return R.status(materMtinfoService.save(materMtinfo));
    }

    /**
     * 修改 物料列表_yb_mater_matinfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入materMtinfo")
    public R update(@Valid @RequestBody MaterMtinfo materMtinfo) {
        return R.status(materMtinfoService.updateById(materMtinfo));
    }

    /**
     * 新增或修改 物料列表_yb_mater_matinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入materMtinfo")
    public R submit(@Valid @RequestBody MaterMtinfo materMtinfo) {
        return R.status(materMtinfoService.saveOrUpdate(materMtinfo));
    }


    /**
     * 删除 物料列表_yb_mater_matinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        String[] strings = ids.split(",");
        Integer[] id = new Integer[strings.length];
        for (int a = 0; a < strings.length; a++){
            id[a] = Integer.parseInt(strings[a]);
        }
        return R.status(materMtinfoService.updatemtinfoIsdelById(id));
    }

    /**
     * 通过物料类型查询所属类型的物料
     */
    @GetMapping("/mtinfoByType")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入mcId")
    public List<MaterMtinfoVO> mtinfoByType(Integer mcId){
        return materMtinfoService.mtinfoByType(mcId);
    }
}
