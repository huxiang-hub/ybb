package com.sso.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.system.entity.Tenant;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 服务类
 *
 * @author Chill
 */
public interface SaITenantService extends BaseService<Tenant> {

    /**
     * 自定义分页
     *
     * @param page
     * @param tenant
     * @return
     */
    IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant);

    /**
     * 新增
     *
     * @param tenant
     * @return
     */
    boolean saveTenant(Tenant tenant);

    List<Tenant> getTenantList(String factoryName);

    Tenant getTenant(String factoryName);

    List<Tenant> getList();
}
