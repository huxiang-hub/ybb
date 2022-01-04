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
import com.yb.process.entity.ProcessParamlink;
import com.yb.process.service.IProcessParamlinkService;
import com.yb.process.vo.ProcessParamlinkVO;
import com.yb.process.wrapper.ProcessParamlinkWrapper;
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
 * 工序参数关联表_yb_proc_paramlink 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/processparamlink")
@Api(value = "工序参数关联表_yb_proc_paramlink", tags = "工序参数关联表_yb_proc_paramlink接口")
public class ProcessParamlinkController extends BladeController {

    private IProcessParamlinkService processParamlinkService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processParamlink")
    public R<ProcessParamlinkVO> detail(ProcessParamlink processParamlink) {
        ProcessParamlink detail = processParamlinkService.getOne(Condition.getQueryWrapper(processParamlink));
        return R.data(ProcessParamlinkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序参数关联表_yb_proc_paramlink
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processParamlink")
    public R<IPage<ProcessParamlinkVO>> list(ProcessParamlink processParamlink, Query query) {
        IPage<ProcessParamlink> pages = processParamlinkService.page(Condition.getPage(query), Condition.getQueryWrapper(processParamlink));
        return R.data(ProcessParamlinkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 工序参数关联表_yb_proc_paramlink
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入processParamlink")
    public R<IPage<ProcessParamlinkVO>> page(ProcessParamlinkVO processParamlink, Query query) {
        IPage<ProcessParamlinkVO> pages = processParamlinkService.selectProcessParamlinkPage(Condition.getPage(query), processParamlink);
        return R.data(pages);
    }

    /**
     * 新增 工序参数关联表_yb_proc_paramlink
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processParamlink")
    public R save(@Valid @RequestBody ProcessParamlink processParamlink) {
        return R.status(processParamlinkService.save(processParamlink));
    }

    /**
     * 修改 工序参数关联表_yb_proc_paramlink
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processParamlink")
    public R update(@Valid @RequestBody ProcessParamlink processParamlink) {
        return R.status(processParamlinkService.updateById(processParamlink));
    }

    /**
     * 新增或修改 工序参数关联表_yb_proc_paramlink
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processParamlink")
    public R submit(@Valid @RequestBody ProcessParamlink processParamlink) {
        return R.status(processParamlinkService.saveOrUpdate(processParamlink));
    }


    /**
     * 删除 工序参数关联表_yb_proc_paramlink
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(processParamlinkService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 通过工序Id查询相关参数
     */
    @GetMapping("/detailByPyId")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processParamlink")
    public R<List<ProcessParamlinkVO>> detailByPyId(Integer pyId) {
        return R.data(processParamlinkService.selectProcessParamlinkByPrId(pyId));
    }

}
