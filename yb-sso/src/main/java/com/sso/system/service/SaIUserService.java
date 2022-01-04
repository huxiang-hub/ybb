package com.sso.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.system.entity.SaUser;
import com.sso.system.entity.SaUserInfo;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.secure.provider.ClientDetails;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 服务类
 *
 * @author Jenny wang
 */
public interface SaIUserService extends BaseService<SaUser> {

    /**
     * 新增或修改用户
     *
     * @param saUser
     * @return
     */
    boolean submit(SaUser saUser);

    /**
     * 自定义分页
     *
     * @param page
     * @param saUser
     * @return
     */
    IPage<SaUser> selectUserPage(IPage<SaUser> page, SaUser saUser);

    /**
     * 用户信息
     *
     * @param userId
     * @return
     */
    SaUserInfo userInfo(Long userId);

    /**
     * 用户信息
     *
     * @param tenantId
     * @param account
     * @param password
     * @return
     */
    SaUserInfo userInfo(String tenantId, String account, String password);

    /**
     * 给用户设置角色
     *
     * @param userIds
     * @param roleIds
     * @return
     */
    boolean grant(String userIds, String roleIds);

    /**
     * 初始化密码
     *
     * @param userIds
     * @return
     */
    boolean resetPassword(String userIds);

    /**
     * 修改密码
     *
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param newPassword1
     * @return
     */
    boolean updatePassword(Integer userId, String oldPassword, String newPassword, String newPassword1);

    /**
     * 获取角色名
     *
     * @param roleIds
     * @return
     */
    List<String> getRoleName(String roleIds);

    /**
     * 获取部门名
     *
     * @param deptIds
     * @return
     */
    List<String> getDeptName(String deptIds);


    R<SaUserInfo> saUserInfo(Long userId);

    /**
     * 获取用户信息
     *
     * @param tenantId 租户ID
     * @param account    账号
     * @param password   密码
     * @return
     */
    R<SaUserInfo> saUserInfo(String tenantId, String account, String password);

    ClientDetails loadClientByClientId(String clientId);

    /**
     * 获取用户列表
     * @param page
     * @param SaUser
     * @return
     */
    IPage<SaUser> getUserList(IPage<SaUser> page, SaUser SaUser);

    /**
     * 查询用户
     */
    SaUser getOneUserCondition(SaUser saUser);

    /**
     * id查询用户
     * @param Id
     * @return
     */
    SaUser getOneUserById(Integer Id);
    /**
     * 修改角色
     */
    Boolean updateOoleId(SaUser saUser);
}
