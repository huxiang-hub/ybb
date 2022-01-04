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
package com.vim.maintain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.vim.maintain.entity.MaintainLoginfo;
import com.vim.maintain.service.IMaintainLoginfoService;
import com.vim.maintain.vo.MaintainLoginfoVO;
import com.vim.maintain.wrapper.MaintainLoginfoWrapper;
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
 * 保养日志_yb_maintain_loginfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/maintainloginfo")
@Api(value = "保养日志_yb_maintain_loginfo", tags = "保养日志_yb_maintain_loginfo接口")
public class MaintainLoginfoController extends BladeController {

    private IMaintainLoginfoService maintainLoginfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入maintainLoginfo")
    public R<MaintainLoginfoVO> detail(MaintainLoginfo maintainLoginfo) {
        MaintainLoginfo detail = maintainLoginfoService.getOne(Condition.getQueryWrapper(maintainLoginfo));
        return R.data(MaintainLoginfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 保养日志_yb_maintain_loginfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入maintainLoginfo")
    public R<IPage<MaintainLoginfoVO>> list(MaintainLoginfo maintainLoginfo, Query query) {
        IPage<MaintainLoginfo> pages = maintainLoginfoService.page(Condition.getPage(query), Condition.getQueryWrapper(maintainLoginfo));
        return R.data(MaintainLoginfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 保养日志_yb_maintain_loginfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入maintainLoginfo")
    public R<IPage<MaintainLoginfoVO>> page(MaintainLoginfoVO maintainLoginfo, Query query) {
        IPage<MaintainLoginfoVO> pages = maintainLoginfoService.selectMaintainLoginfoPage(Condition.getPage(query), maintainLoginfo);
        return R.data(pages);
    }

    /**
     * 新增 保养日志_yb_maintain_loginfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入maintainLoginfo")
    public R save(@Valid @RequestBody MaintainLoginfo maintainLoginfo) {
        return R.status(maintainLoginfoService.save(maintainLoginfo));
    }

    /**
     * 修改 保养日志_yb_maintain_loginfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入maintainLoginfo")
    public R update(@Valid @RequestBody MaintainLoginfo maintainLoginfo) {
        return R.status(maintainLoginfoService.updateById(maintainLoginfo));
    }

    /**
     * 新增或修改 保养日志_yb_maintain_loginfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入maintainLoginfo")
    public R submit(@Valid @RequestBody MaintainLoginfo maintainLoginfo) {
        return R.status(maintainLoginfoService.saveOrUpdate(maintainLoginfo));
    }


    /**
     * 删除 保养日志_yb_maintain_loginfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(maintainLoginfoService.removeByIds(Func.toIntList(ids)));
    }


}
