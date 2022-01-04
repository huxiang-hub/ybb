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
package com.yb.exeset.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.exeset.entity.ExesetNetwork;
import com.yb.exeset.service.IExesetNetworkService;
import com.yb.exeset.vo.ExesetNetworkVO;
import com.yb.exeset.wrapper.ExesetNetworkWrapper;
import com.yb.process.service.IProcessClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 网络设置管理_yb_exeset_network 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/exesetnetwork")
@Api(value = "网络设置管理_yb_exeset_network", tags = "网络设置管理_yb_exeset_network接口")
public class ExesetNetworkController extends BladeController {

    private IExesetNetworkService exesetNetworkService;

    private IBaseDeptinfoService iBaseDeptinfoService;

    private IProcessClassifyService iProcessClassifyService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入exesetNetwork")
    public R<ExesetNetworkVO> detail(ExesetNetwork exesetNetwork) {
        ExesetNetwork detail = exesetNetworkService.getOne(Condition.getQueryWrapper(exesetNetwork));
        return R.data(ExesetNetworkWrapper.build().entityVO(detail));
    }

    /**
     * 分页 网络设置管理_yb_exeset_network
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入exesetNetwork")
    public R<IPage<ExesetNetworkVO>> list(ExesetNetwork exesetNetwork, Query query) {
        IPage<ExesetNetwork> pages = exesetNetworkService.page(Condition.getPage(query), Condition.getQueryWrapper(exesetNetwork));
        return R.data(ExesetNetworkWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 网络设置管理_yb_exeset_network
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入exesetNetwork")
    public R<IPage<ExesetNetworkVO>> page(ExesetNetworkVO exesetNetwork, Query query) {
        IPage<ExesetNetworkVO> pages = exesetNetworkService.selectExesetNetworkPage(Condition.getPage(query), exesetNetwork);
        return R.data(pages);
    }

    /**
     * 新增 网络设置管理_yb_exeset_network
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入exesetNetwork")
    public R save(@Valid @RequestBody ExesetNetwork exesetNetwork) {
        return R.status(exesetNetworkService.save(exesetNetwork));
    }

    /**
     * 修改 网络设置管理_yb_exeset_network
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入exesetNetwork")
    public R update(@Valid @RequestBody ExesetNetwork exesetNetwork) {
        return R.status(exesetNetworkService.updateById(exesetNetwork));
    }

    /**
     * 新增或修改 网络设置管理_yb_exeset_network
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入exesetNetwork")
    public R submit(@Valid @RequestBody ExesetNetwork exesetNetwork) {
        return R.status(exesetNetworkService.saveOrUpdate(exesetNetwork));
    }


    /**
     * 删除 网络设置管理_yb_exeset_network
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(exesetNetworkService.removeByIds(Func.toIntList(ids)));
    }


    /**
     * todo redis注解
     */
   /* @PostMapping("/getDeptList")
    @ApiOperation(value = "获取部门信息", notes = "传入ids")
    public R getDeptList() {
        HashMap result = new HashMap();
        List<BaseDeptinfoVO> baseDeptinfo = iBaseDeptinfoService.baseDeptinfos();
            result.put("deptInfo",baseDeptinfo);
        return R.data(result);
    }*/
    /**
     * todo redis注解
     */
    @RequestMapping("/getDeptList")
    @ApiOperation(value = "获取部门信息", notes = "传入ids")
    public R getHierarchyDeptList() {
        HashMap result = new HashMap();
        List<BaseDeptinfoVO> baseDeptinfo = iBaseDeptinfoService.getHierarchyDeptList();
        result.put("deptInfo",baseDeptinfo);
        return R.data(result);
    }

}
