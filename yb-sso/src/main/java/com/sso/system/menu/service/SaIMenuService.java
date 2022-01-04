package com.sso.system.menu.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.system.entity.SaUser;
import com.sso.system.menu.entity.Menu;
import com.sso.system.menu.vo.MenuVO;
import org.springblade.core.tool.support.Kv;

import java.util.List;

/**
 * 服务类
 *
 * @author Chill
 */
public interface SaIMenuService extends IService<Menu> {

    /**
     * 自定义分页
     *
     * @param page
     * @param menu
     * @return
     */
    IPage<MenuVO> selectMenuPage(IPage<MenuVO> page, MenuVO menu);

    /**
     * 菜单树形结构
     *
     * @param roleId
     * @return
     */
    List<MenuVO> routes(String roleId);


    /**
     * app菜单
     *
     * @param roleId
     * @return
     */
    List<MenuVO> getAppRoutes(String roleId);

    /**
     * 按钮树形结构
     *
     * @param roleId
     * @return
     */
    List<MenuVO> buttons(String roleId);

    /**
     * 树形结构
     *
     * @return
     */
    List<MenuVO> tree();

    /**
     * 授权树形结构
     *
     * @param user
     * @return
     */
    List<MenuVO> grantTree(SaUser user);

    /**
     * 默认选中节点
     *
     * @param roleIds
     * @return
     */
    List<String> roleTreeKeys(String roleIds);

    /**
     * 获取配置的角色权限
     *
     * @param user
     * @return
     */
    List<Kv> authRoutes(SaUser user);

}
