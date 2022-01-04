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
package com.yb.satapi.power;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.entity.Tenant;
import com.yb.system.user.service.SaITenantService;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 控制器
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@ApiIgnore
@Api(value = "租户管理", tags = "接口")
@RequestMapping("/tenant")
public class SaTenantController extends BladeController {

    private SaITenantService tenantService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperation(value = "详情", notes = "传入tenant")
    public R<Tenant> detail(Tenant tenant) {
        Tenant detail = tenantService.getOne(Condition.getQueryWrapper(tenant));
        return R.data(detail);
    }

    /**
     * 分页
     */
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tenantId", value = "参数名称", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "tenantName", value = "角色别名", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "contactNumber", value = "联系电话", paramType = "query", dataType = "string")
    })
    @ApiOperation(value = "分页", notes = "传入tenant")
    public R<IPage<Tenant>> list(@ApiIgnore @RequestParam Map<String, Object> tenant, Query query, SaUser bladeUser) {
        bladeUser = SaSecureUtil.getUser();
        QueryWrapper<Tenant> queryWrapper = Condition.getQueryWrapper(tenant, Tenant.class);
        IPage<Tenant> pages = tenantService.page(Condition.getPage(query), (!bladeUser.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda().eq(Tenant::getTenantId, bladeUser.getTenantId()) : queryWrapper);
        return R.data(pages);
    }

    /**
     * 下拉数据源
     */
    @GetMapping("/select")
    @ApiOperation(value = "下拉数据源", notes = "传入tenant")
    public R<List<Tenant>> select(Tenant tenant, SaUser bladeUser) {
        bladeUser = SaSecureUtil.getUser();
        QueryWrapper<Tenant> queryWrapper = Condition.getQueryWrapper(tenant);
        List<Tenant> list = tenantService.getList();//(!bladeUser.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID)) ? queryWrapper.lambda().eq(Tenant::getTenantId, bladeUser.getTenantId()) : queryWrapper  去掉筛选条件
        return R.data(list);
    }

    /**
     * 自定义分页
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页", notes = "传入tenant")
    public R<IPage<Tenant>> page(Tenant tenant, Query query) {
        IPage<Tenant> pages = tenantService.selectTenantPage(Condition.getPage(query), tenant);
        return R.data(pages);
    }

    /**
     * 新增或修改
     */
    @PostMapping("/submit")
    @ApiOperation(value = "新增或修改", notes = "传入tenant")
    public R submit(@Valid @RequestBody Tenant tenant) {
        return R.status(tenantService.saveTenant(tenant));
    }


    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperation(value = "逻辑删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(tenantService.deleteLogic(Func.toIntList(ids)));
    }


}
