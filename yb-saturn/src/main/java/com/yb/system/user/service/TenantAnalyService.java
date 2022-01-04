package com.yb.system.user.service;

import com.yb.system.user.entity.Tenant;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.tool.api.R;



/**
 * 服务类
 *
 * @author 解析对外接口
 */
public interface TenantAnalyService extends BaseService<Tenant> {

    /**
     * 根据加密后的字符串获取租户id
     *
     * @param tenantStr
     * @return
     */
    R getAnalyTenantBoxInfo(String tenantStr);

}
