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
import com.yb.execute.dto.ExecutePreparationDTO;
import com.yb.execute.entity.ExecutePreparation;
import com.yb.execute.service.IExecutePreparationService;
import com.yb.execute.vo.ExecutePreparationVO;
import com.yb.execute.wrapper.ExecutePreparationWrapper;
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
 * 生产准备记录_yb_execute_preparation 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executepreparation")
@Api(value = "生产准备记录_yb_execute_preparation", tags = "生产准备记录_yb_execute_preparation接口")
public class ExecutePreparationController extends BladeController {

    private IExecutePreparationService executePreparationService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入executePreparation")
    public R<ExecutePreparationVO> detail(ExecutePreparation executePreparation) {
        ExecutePreparation detail = executePreparationService.getOne(Condition.getQueryWrapper(executePreparation));
        return R.data(ExecutePreparationWrapper.build().entityVO(detail));
    }

    /**
     * 分页 生产准备记录_yb_execute_preparation
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executePreparation")
    public R<IPage<ExecutePreparationVO>> list(ExecutePreparation executePreparation, Query query) {
        IPage<ExecutePreparation> pages = executePreparationService.page(Condition.getPage(query), Condition.getQueryWrapper(executePreparation));
        return R.data(ExecutePreparationWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 生产准备记录_yb_execute_preparation
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入executePreparation")
    public R<IPage<ExecutePreparationDTO>> page(ExecutePreparationDTO executePreparation, Query query) {
        IPage<ExecutePreparationDTO> pages = executePreparationService.selectExecutePreparationPage(Condition.getPage(query), executePreparation);
        return R.data(pages);
    }

    /**
     * 新增 生产准备记录_yb_execute_preparation
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入executePreparation")
    public R save(@Valid @RequestBody ExecutePreparation executePreparation) {
        return R.status(executePreparationService.save(executePreparation));
    }

    /**
     * 修改 生产准备记录_yb_execute_preparation
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入executePreparation")
    public R update(@Valid @RequestBody ExecutePreparation executePreparation) {
        return R.status(executePreparationService.updateById(executePreparation));
    }

    /**
     * 新增或修改 生产准备记录_yb_execute_preparation
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入executePreparation")
    public R submit(@Valid @RequestBody ExecutePreparation executePreparation) {
        return R.status(executePreparationService.saveOrUpdate(executePreparation));
    }


    /**
     * 删除 生产准备记录_yb_execute_preparation
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(executePreparationService.removeByIds(Func.toIntList(ids)));
    }


}
