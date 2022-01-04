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
package com.yb.system.role.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.system.role.entity.Role;
import com.yb.system.role.entity.RoleMenu;
import com.yb.system.role.mapper.SaRoleMapper;
import com.yb.system.role.service.SaIRoleMenuService;
import com.yb.system.role.service.SaIRoleService;
import com.yb.system.role.vo.RoleVO;
import lombok.AllArgsConstructor;
import org.springblade.core.secure.utils.SecureUtil;
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
        String userRole = SecureUtil.getUserRole();
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
