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
package com.yb.maintain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.maintain.entity.MaintainRepairinfo;
import com.yb.maintain.service.IMaintainRepairinfoService;
import com.yb.maintain.vo.MaintainRepairinfoVO;
import com.yb.maintain.wrapper.MaintainRepairinfoWrapper;
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
 * 维修信息_yb_maintain_repairinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/maintainrepairinfo")
@Api(value = "维修信息_yb_maintain_repairinfo", tags = "维修信息_yb_maintain_repairinfo接口")
public class MaintainRepairinfoController extends BladeController {

    private IMaintainRepairinfoService maintainRepairinfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入maintainRepairinfo")
    public R<MaintainRepairinfoVO> detail(MaintainRepairinfo maintainRepairinfo) {
        MaintainRepairinfo detail = maintainRepairinfoService.getOne(Condition.getQueryWrapper(maintainRepairinfo));
        return R.data(MaintainRepairinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 维修信息_yb_maintain_repairinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入maintainRepairinfo")
    public R<IPage<MaintainRepairinfoVO>> list(MaintainRepairinfo maintainRepairinfo, Query query) {
        IPage<MaintainRepairinfo> pages = maintainRepairinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(maintainRepairinfo));
        return R.data(MaintainRepairinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 维修信息_yb_maintain_repairinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入maintainRepairinfo")
    public R<IPage<MaintainRepairinfoVO>> page(MaintainRepairinfoVO maintainRepairinfo, Query query) {
        IPage<MaintainRepairinfoVO> pages = maintainRepairinfoService.selectMaintainRepairinfoPage(Condition.getPage(query), maintainRepairinfo);
        return R.data(pages);
    }

    /**
     * 新增 维修信息_yb_maintain_repairinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入maintainRepairinfo")
    public R save(@Valid @RequestBody MaintainRepairinfo maintainRepairinfo) {
        return R.status(maintainRepairinfoService.save(maintainRepairinfo));
    }

    /**
     * 修改 维修信息_yb_maintain_repairinfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入maintainRepairinfo")
    public R update(@Valid @RequestBody MaintainRepairinfo maintainRepairinfo) {
        return R.status(maintainRepairinfoService.updateById(maintainRepairinfo));
    }

    /**
     * 新增或修改 维修信息_yb_maintain_repairinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入maintainRepairinfo")
    public R submit(@Valid @RequestBody MaintainRepairinfo maintainRepairinfo) {
        return R.status(maintainRepairinfoService.saveOrUpdate(maintainRepairinfo));
    }


    /**
     * 删除 维修信息_yb_maintain_repairinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(maintainRepairinfoService.removeByIds(Func.toIntList(ids)));
    }


}
