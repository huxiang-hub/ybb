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
package com.yb.process.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.process.entity.ProcessClasslink;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.vo.ProcessClasslinkVO;
import com.yb.process.wrapper.ProcessClasslinkWrapper;
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
 * 工序分类关联表yb_process_classlink 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/processclasslink")
@Api(value = "工序分类关联表yb_process_classlink", tags = "工序分类关联表yb_process_classlink接口")
public class ProcessClasslinkController extends BladeController {

    private IProcessClasslinkService processClasslinkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processClasslink")
    public R<ProcessClasslinkVO> detail(ProcessClasslink processClasslink) {
        ProcessClasslink detail = processClasslinkService.getOne(Condition.getQueryWrapper(processClasslink));
        return R.data(ProcessClasslinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序分类关联表yb_process_classlink
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClasslink")
    public R<IPage<ProcessClasslinkVO>> list(ProcessClasslink processClasslink, Query query) {
        IPage<ProcessClasslink> pages = processClasslinkService.page(Condition.getPage(query), Condition.getQueryWrapper(processClasslink));
        return R.data(ProcessClasslinkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 工序分类关联表yb_process_classlink
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processClasslink")
    public R<IPage<ProcessClasslinkVO>> page(ProcessClasslinkVO processClasslink, Query query) {
        IPage<ProcessClasslinkVO> pages = processClasslinkService.selectProcessClasslinkPage(Condition.getPage(query), processClasslink);
        return R.data(pages);
    }

    /**
     * 新增 工序分类关联表yb_process_classlink
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processClasslink")
    public R save(@Valid @RequestBody ProcessClasslink processClasslink) {
        return R.status(processClasslinkService.save(processClasslink));
    }

    /**
     * 修改 工序分类关联表yb_process_classlink
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processClasslink")
    public R update(@Valid @RequestBody ProcessClasslink processClasslink) {
        return R.status(processClasslinkService.updateById(processClasslink));
    }

    /**
     * 新增或修改 工序分类关联表yb_process_classlink
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processClasslink")
    public R submit(@Valid @RequestBody ProcessClasslink processClasslink) {
        return R.status(processClasslinkService.saveOrUpdate(processClasslink));
    }


    /**
     * 删除 工序分类关联表yb_process_classlink
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(processClasslinkService.removeByIds(Func.toIntList(ids)));
    }


}
