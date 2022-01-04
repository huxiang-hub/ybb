package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.system.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Chill
 */
@Mapper
public interface SaTenantMapper extends BaseMapper<Tenant> {

    /**
     * 自定义分页
     *
     * @param page
     * @param tenant
     * @return
     */
    List<Tenant> selectTenantPage(IPage page, Tenant tenant);

    Tenant getTenant(String factoryName);

    List<Tenant> getTenantList(String factoryName);
    List<Tenant> getList();


    List<String> findTenant();


}
