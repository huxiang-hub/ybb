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
package com.vim.chatapi.user.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.vim.chatapi.user.entity.BaseStaffinfo;
import com.vim.chatapi.user.vo.BaseStaffextVO;
import com.vim.chatapi.user.vo.UserDto;
import org.springblade.core.mp.base.BaseService;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.entity.UserInfo;

import java.util.List;

/**
 * 服务类
 *
 * @author qinbo
 */
public interface IChatUserService extends BaseService<User> {

    UserDto getUserInfo(Integer userId, String tenantId);

    /**
     * 新增或修改用户
     *
     * @param user
     * @return
     */
    boolean submit(User user);

    /**
     * 自定义分页
     *
     * @param page
     * @param user
     * @return
     */
    IPage<User> selectUserPage(IPage<User> page, User user);

    /**
     * 用户信息
     *
     * @param userId
     * @return
     */
    UserInfo userInfo(Long userId);

    /**
     * 用户信息
     *
     * @param tenantId
     * @param account
     * @param password
     * @return
     */
    UserInfo userInfo(String tenantId, String account, String password);

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

    /**
     * 注册用户
     * author  : qinbo
     * @param user
     * @return
     */
    boolean saveBladeUser(User user);

    /**
     * 获取最大的chatNo编号
     * @return
     */
    Integer getMaxChatNo();


    /**
     * date:2020/3/24
     * author: qinbo
     * @param newPhone
     * @return
     * 用户修改手机
     */
    boolean updataUserPhone(String newPhone,Integer id);

    /**
     * ate:2020/3/24
     * author qinbo11
     * @param id
     * @return
     * 获取用户信息
     */
    User getUser(Integer id);
    /**
     * date 2020/3/24
     * author qinbo
     * @param phoneNum
     * @return
     * 通过手机知道用户
     */
    User getUserByPhone(String phoneNum);
    /**
     * date 2020/3/25
     * author qinbo
     * @param newPassword
     * @return
     * 修改密码通过验证码
     */
    boolean updatePasswordById(String newPassword,Integer id);

    /**
     * 登录
     * @param user
     * @return
     */
    User selectUser(User user);

    /**
     * 获取用户的其他信息
     * @return
     */
    BaseStaffextVO getBaseStaffextVO(Integer userId);


    User selectUserInfo(Integer userId);

    /**
     * 修改住址
     * @param
     * @return
     */
    int updateStaffinfo(BaseStaffext baseStaffext);

    BaseStaffinfo getbaseStaffInfoByJobnNum(String jobNum);

    int  updateBaseSatffinfo(String jobNum,Integer userId);

}
