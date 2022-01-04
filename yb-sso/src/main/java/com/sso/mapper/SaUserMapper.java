package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sso.system.entity.SaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.secure.provider.ClientDetails;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author Jenny wang
 */
@Mapper
public interface SaUserMapper extends BaseMapper<SaUser> {

    /**
     * 自定义分页
     *
     * @param page
     * @param saUser
     * @return
     */
    List<SaUser> selectUserPage(IPage page, SaUser saUser);

    /**
     * 获取用户
     *
     * @param tenantId
     * @param account
     * @param password
     * @return
     */
    SaUser getUser(String tenantId, String account, String password);

    /**
     * 获取角色名
     *
     * @param ids
     * @return
     */
    List<String> getRoleName(String[] ids);

    /**
     * 获取角色别名
     *
     * @param ids
     * @return
     */
    List<String> getRoleAlias(String[] ids);

    /**
     * 获取部门名
     *
     * @param ids
     * @return
     */
    List<String> getDeptName(String[] ids);

    ClientDetails getClientDetails(String clientId);

    /**
     * 查询userList
     */
    List<SaUser> getUserList(IPage<SaUser> page, @Param("saUser")SaUser saUser);
    /**
     * 查询单个用户
     */
    SaUser getOneUserCondition(@Param("saUser")SaUser saUser);
    /**
     * 查询单个用户
     */
    SaUser getOneUserById(@Param("Id")Integer Id);
    /**
     * 修改角色
     */
    Integer updateOoleId(@Param("saUser")SaUser saUser);
}
