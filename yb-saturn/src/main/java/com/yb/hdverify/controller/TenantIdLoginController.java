package com.yb.hdverify.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yb.system.user.entity.Tenant;
import com.yb.system.user.service.SaITenantService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/tenant")
public class TenantIdLoginController {

    @Autowired
    private SaITenantService saITenantService;

    @RequestMapping("/TenantLogin")
    public R TenantIdLogin(String tenant, String userName){
        Tenant ten = saITenantService.TenantIdLogin(tenant);
//        Tenant ten = saITenantService.getBaseMapper().selectOne(new QueryWrapper<Tenant>()
//                .eq("tenant_id", tenant));
        if(ten == null){
            return R.fail("租户信息不存在");
        }
        return R.data(tenant);
    }
}
