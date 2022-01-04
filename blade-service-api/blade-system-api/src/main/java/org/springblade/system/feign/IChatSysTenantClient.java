package org.springblade.system.feign;

import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.tool.api.R;
import org.springblade.system.entity.Tenant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/25.
 */
@FeignClient(
        value = AppConstant.APPLICATION_SYSTEM_NAME,
        fallback = ChatSysTenantClientFallback.class
)
public interface IChatSysTenantClient {

    String API_PREFIX = "/chat" ;

    @GetMapping(API_PREFIX + "/getTenant")
    R<Tenant> getTenant(@RequestParam("factoryName") String factoryName);

    @GetMapping(API_PREFIX + "/getTenantList")
    R<List<Tenant>> getTenantList(@RequestParam("factoryName") String factoryName);
}
