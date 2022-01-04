package com.sso.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sso.system.entity.Tenant;
import com.sso.mapper.SaTenantMapper;
import com.sso.system.role.entity.Role;
import com.sso.mapper.SaRoleMapper;
import com.sso.system.service.SaITenantService;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.tenant.TenantId;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class SaTenantServiceImpl extends BaseServiceImpl<SaTenantMapper, Tenant> implements SaITenantService {

    private final TenantId tenantId;
    private final SaRoleMapper roleMapper;
//    private final SaDeptMapper deptMapper;
    private final SaTenantMapper tenantMapper;

    @Override
    public IPage<Tenant> selectTenantPage(IPage<Tenant> page, Tenant tenant) {
        return page.setRecords(baseMapper.selectTenantPage(page, tenant));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTenant(Tenant tenant) {
        if (Func.isEmpty(tenant.getId())) {
            List<Tenant> tenants = baseMapper.selectList(Wrappers.<Tenant>query().lambda().eq(Tenant::getIsDeleted, BladeConstant.DB_NOT_DELETED));
            List<String> codes = tenants.stream().map(Tenant::getTenantId).collect(Collectors.toList());
            String tenantId = getTenantId(codes);
            tenant.setTenantId(tenantId);
            // 新建租户对应的默认角色
            Role role = new Role();
            role.setTenantId(tenantId);
            role.setParentId(0);
            role.setRoleName("管理员");
            role.setRoleAlias("admin");
            role.setSort(2);
            role.setIsDeleted(0);
            roleMapper.insert(role);
            // 新建租户对应的默认部门
//            Dept dept = new Dept();
//            dept.setTenantId(tenantId);
//            dept.setParentId(0);
//            dept.setDeptName(tenant.getTenantName());
//            dept.setFullName(tenant.getTenantName());
//            dept.setSort(2);
//            dept.setIsDeleted(0);
//            deptMapper.insert(dept);
        }
        return super.saveOrUpdate(tenant);
    }

    @Override
    public List<Tenant> getTenantList(String factoryName) {
        return tenantMapper.getTenantList(factoryName);
    }

    @Override
    public Tenant getTenant(String factoryName) {
        return tenantMapper.getTenant(factoryName);
    }

    private String getTenantId(List<String> codes) {
        String code = tenantId.generate();
        if (codes.contains(code)) {
            return getTenantId(codes);
        }
        return code;
    }

    @Override
    public List<Tenant> getList(){
       return tenantMapper.getList();
    }

}
