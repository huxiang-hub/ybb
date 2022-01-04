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
package com.yb.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.request.UserListRequest;
import com.yb.system.user.response.SysUserPageVO;
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
    List<SaUser> getUserList(IPage<SaUser> page, @Param("saUser") SaUser saUser);

    /**
     * 查询单个用户
     */
    SaUser getOneUserCondition(@Param("saUser") SaUser saUser);

    /**
     * 查询单个用户
     */
    SaUser getOneUserById(@Param("Id") Integer Id);

    /**
     * 修改角色
     */
    Integer updateOoleId(@Param("saUser") SaUser saUser);

    List<SysUserPageVO> page(IPage<SysUserPageVO> page, @Param("request") UserListRequest request);

    List<SaUser> findByAccount(@Param("account") String account, @Param("id") Integer id);

    void updateStatus(@Param("ids") List<Integer> ids);

    String getNameByIds(@Param("ids") List<String> ids);

    /**
     * 根据手机号修改用户头像
     * @param mobile
     * @param userAvatar
     */
    void updateAvatarByPhone(@Param("mobile") String mobile, @Param("userAvatar") String userAvatar);

    /**
     *
     * @param mobile
     * @return
     */
    String getAvatar(@Param("mobile") String mobile);
}
