package com.sso.system.role.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.system.role.entity.Role;
import com.sso.system.role.entity.RoleMenu;
import com.sso.mapper.SaRoleMapper;
import com.sso.system.role.service.SaIRoleMenuService;
import com.sso.system.role.service.SaIRoleService;
import com.sso.system.role.vo.RoleVO;
import com.sso.utils.SaSecureUtil;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.constant.RoleConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.CollectionUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@Validated
@AllArgsConstructor
public class SaRoleServiceImpl extends ServiceImpl<SaRoleMapper, Role> implements SaIRoleService {

    SaIRoleMenuService roleMenuService;

    @Override
    public IPage<RoleVO> selectRolePage(IPage<RoleVO> page, RoleVO role) {
        return page.setRecords(baseMapper.selectRolePage(page, role));
    }

    @Override
    public List<RoleVO> tree(String tenantId) {
        String userRole = SaSecureUtil.getUserRole();
        String excludeRole = null;
        if (!CollectionUtil.contains(Func.toStrArray(userRole), RoleConstant.ADMIN)) {
            excludeRole = RoleConstant.ADMIN;
        }
        return ForestNodeMerger.merge(baseMapper.tree(tenantId, excludeRole));
    }

    @Override
    public boolean grant(@NotEmpty List<Integer> roleIds, @NotEmpty List<Integer> menuIds) {
        // 删除角色配置的菜单集合
        roleMenuService.remove(Wrappers.<RoleMenu>update().lambda().in(RoleMenu::getRoleId, roleIds));
        // 组装配置
        List<RoleMenu> roleMenus = new ArrayList<>();
        roleIds.forEach(roleId -> menuIds.forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenus.add(roleMenu);
        }));
        // 新增配置
        return roleMenuService.saveBatch(roleMenus);
    }

}
