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
package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.service.IExecuteWasteService;
import com.yb.execute.vo.ExecuteWastReportVO;
import com.yb.execute.vo.ExecuteWasteNumberVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.execute.wrapper.ExecuteWasteWrapper;
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
 * 质量检查废品表_yb_execute_waste 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executewaste")
@Api(value = "质量检查废品表_yb_execute_waste", tags = "质量检查废品表_yb_execute_waste接口")
public class ExecuteWasteController extends BladeController {

    private IExecuteWasteService executeWasteService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executeWaste")
    public R<ExecuteWasteVO> detail(ExecuteWaste executeWaste) {
        ExecuteWaste detail = executeWasteService.getOne(Condition.getQueryWrapper(executeWaste));
        return R.data(ExecuteWasteWrapper.build().entityVO(detail));
    }

    /**
     * 分页 质量检查废品表_yb_execute_waste
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeWaste")
    public R<IPage<ExecuteWasteVO>> list(ExecuteWaste executeWaste, Query query) {
        IPage<ExecuteWaste> pages = executeWasteService.page(Condition.getPage(query), Condition.getQueryWrapper(executeWaste));
        return R.data(ExecuteWasteWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 质量检查废品表_yb_execute_waste
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executeWaste")
    public R<IPage<ExecuteWasteVO>> page(ExecuteWasteVO executeWaste, Query query) {
        IPage<ExecuteWasteVO> pages = executeWasteService.selectExecuteWastePage(Condition.getPage(query), executeWaste);
        return R.data(pages);
    }

    /**
     * 新增 质量检查废品表_yb_execute_waste
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executeWaste")
    public R save(@Valid @RequestBody ExecuteWaste executeWaste) {
        return R.status(executeWasteService.save(executeWaste));
    }

    /**
     * 修改 质量检查废品表_yb_execute_waste
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executeWaste")
    public R update(@Valid @RequestBody ExecuteWaste executeWaste) {
        return R.status(executeWasteService.updateById(executeWaste));
    }

    /**
     * 新增或修改 质量检查废品表_yb_execute_waste
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executeWaste")
    public R submit(@Valid @RequestBody ExecuteWaste executeWaste) {
        return R.status(executeWasteService.saveOrUpdate(executeWaste));
    }


    /**
     * 删除 质量检查废品表_yb_execute_waste
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executeWasteService.removeByIds(Func.toIntList(ids)));
    }

    @PostMapping("submitWaste")
    @ApiOperation("机台废品-废品上报")
    public R submitWaste(@RequestBody ExecuteWastReportVO reportVO) {
        executeWasteService.submitWaste(reportVO);
        return R.success("上报成功");
    }


    @GetMapping("lastTwoWaste")
    @ApiOperation("机台废品-获取前两道工序废品")
    public R<List<ExecuteWasteNumberVO>> lastTwoWaste(Integer wfId) {
        List<ExecuteWasteNumberVO> result = executeWasteService.lastTwoWaste(wfId);
        return R.data(result);
    }


}
