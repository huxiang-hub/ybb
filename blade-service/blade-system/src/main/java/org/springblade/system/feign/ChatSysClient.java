package org.springblade.system.feign;

import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.system.entity.Tenant;
import org.springblade.system.service.ITenantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/25.
 */
@RestController
@AllArgsConstructor
public class ChatSysClient implements IChatSysTenantClient {

    ITenantService tenantService;

    /**
     * 根据名称查找租户
     */
    @Override
    @GetMapping(API_PREFIX + "/getTenant")
    public R<Tenant> getTenant(@RequestParam("factoryName")String factoryName){
        Tenant tenant = tenantService.getTenant(factoryName);
        return R.data(tenantService.getTenant(factoryName));

    }
    /**
     * 根据名称模糊查询所有厂区
     */
    @Override
    @GetMapping(API_PREFIX + "/getTenantList")
    public R<List<Tenant>> getTenantList(@RequestParam("factoryName")String factoryName){
        List<Tenant> tenantList = tenantService.getTenantList(factoryName);
        return R.data(tenantService.getTenantList(factoryName));
    }
}
