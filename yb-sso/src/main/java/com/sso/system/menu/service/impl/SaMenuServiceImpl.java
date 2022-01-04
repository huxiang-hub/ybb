package com.sso.system.menu.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.system.entity.SaUser;
import com.sso.system.menu.dto.MenuDTO;
import com.sso.system.menu.entity.Menu;
import com.sso.mapper.SaMenuMapper;
import com.sso.system.menu.service.SaIMenuService;
import com.sso.system.menu.vo.MenuVO;
import com.sso.system.menu.wrapper.MenuWrapper;
import com.sso.system.role.entity.RoleMenu;
import com.sso.system.role.service.SaIRoleMenuService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.support.Kv;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
@AllArgsConstructor
public class SaMenuServiceImpl extends ServiceImpl<SaMenuMapper, Menu> implements SaIMenuService {

    SaIRoleMenuService roleMenuService;

    @Override
    public IPage<MenuVO> selectMenuPage(IPage<MenuVO> page, MenuVO menu) {
        return page.setRecords(baseMapper.selectMenuPage(page, menu));
    }

    @Override
    public List<MenuVO> routes(String roleId) {
        List<Menu> allMenus = baseMapper.allMenu();
        List<Menu> roleMenus = baseMapper.roleMenu(Func.toIntList(roleId));
        List<Menu> routes = new LinkedList<>(roleMenus);
        roleMenus.forEach(roleMenu -> recursion(allMenus, routes, roleMenu));
        routes.sort(Comparator.comparing(Menu::getSort));
        MenuWrapper menuWrapper = new MenuWrapper();
//        List<Menu> collect = routes.stream().filter(x -> Func.equals(x.getCategory(), 1)).collect(Collectors.toList());
        List<Menu> collect = routes.stream().filter(o -> {
            if (o.getCategory() == 1 || o.getCategory() == 4) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return menuWrapper.listNodeVO(collect);
    }

    @Override
    public List<MenuVO> getAppRoutes(String roleId) {
        List<Menu> allMenus = baseMapper.appAllMenu();
        List<Menu> roleMenus = baseMapper.appRoleMenu(Func.toIntList(roleId));
        List<Menu> routes = new LinkedList<>(roleMenus);
        roleMenus.forEach(roleMenu -> recursion(allMenus, routes, roleMenu));
        routes.sort(Comparator.comparing(Menu::getSort));
        MenuWrapper menuWrapper = new MenuWrapper();
        List<Menu> collect = routes.stream().filter(x -> Func.equals(x.getCategory(), 3)).collect(Collectors.toList());
        return menuWrapper.listNodeVO(collect);
    }

    public void recursion(List<Menu> allMenus, List<Menu> routes, Menu roleMenu) {
        Optional<Menu> menu = allMenus.stream().filter(x -> Func.equals(x.getId(), roleMenu.getParentId())).findFirst();
        if (menu.isPresent() && !routes.contains(menu.get())) {
            routes.add(menu.get());
            recursion(allMenus, routes, menu.get());
        }
    }

    @Override
    public List<MenuVO> buttons(String roleId) {
        List<Menu> buttons = baseMapper.buttons(Func.toIntList(roleId));
        MenuWrapper menuWrapper = new MenuWrapper();
        return menuWrapper.listNodeVO(buttons);
    }

    @Override
    public List<MenuVO> tree() {
        return ForestNodeMerger.merge(baseMapper.tree());
    }

    @Override
    public List<MenuVO> grantTree(SaUser user) {
        return ForestNodeMerger.merge(user.getTenantId().equals(BladeConstant.ADMIN_TENANT_ID) ? baseMapper.grantTree() : baseMapper.grantTreeByRole(Func.toIntList(user.getRoleId())));
    }

    @Override
    public List<String> roleTreeKeys(String roleIds) {
        List<RoleMenu> roleMenus = roleMenuService.list(Wrappers.<RoleMenu>query().lambda().in(RoleMenu::getRoleId, Func.toIntList(roleIds)));
        return roleMenus.stream().map(roleMenu -> Func.toStr(roleMenu.getMenuId())).collect(Collectors.toList());
    }

    @Override
    public List<Kv> authRoutes(SaUser user) {
        if (Func.isEmpty(user)) {
            return null;
        }
        List<MenuDTO> routes = baseMapper.authRoutes(Func.toIntList(user.getRoleId()));
        List<Kv> list = new ArrayList<>();
        routes.forEach(route -> list.add(Kv.init().set(route.getPath(), Kv.init().set("authority", Func.toStrArray(route.getAlias())))));
        return list;
    }

}
