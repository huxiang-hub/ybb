package org.springblade.system.feign;

import org.springblade.core.tool.api.R;
import org.springblade.system.entity.Tenant;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/25.
 */
@Component
public class ChatSysTenantClientFallback implements IChatSysTenantClient {
    @Override
    public R<Tenant> getTenant(String factoryName) {
        return R.fail("服务暂时不可用");
    }

    @Override
    public R<List<Tenant>> getTenantList(String factoryName) {
        return R.fail("服务暂时不可用");
    }
}
