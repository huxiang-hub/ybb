package com.sso.system.role.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.system.role.entity.RoleMenu;
import com.sso.mapper.SaRoleMenuMapper;
import com.sso.system.role.service.SaIRoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class SaRoleMenuServiceImpl extends ServiceImpl<SaRoleMenuMapper, RoleMenu> implements SaIRoleMenuService {

}
