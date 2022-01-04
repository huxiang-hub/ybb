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
package com.vim.chatapi.user.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.vim.chatapi.user.config.MD5Utils;
import com.vim.chatapi.user.entity.BaseStaffinfo;
import com.vim.chatapi.user.mapper.ChatUserMapper;
import com.vim.chatapi.user.service.IChatUserService;
import com.vim.chatapi.user.vo.BaseStaffextVO;
import com.vim.chatapi.user.vo.UserDto;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.tool.MD5;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.saturn.entity.BaseStaffext;
import org.springblade.system.user.entity.User;
import org.springblade.system.user.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author qinbo
 */
@Service
public class IChatUserServiceImpl extends BaseServiceImpl<ChatUserMapper, User> implements IChatUserService {


    @Override
    public UserDto getUserInfo(Integer userId, String tenantId) {

        return baseMapper.getUserInfo(userId,tenantId);
    }

    @Override
    public boolean submit(User user) {
        //密码加密
        if (Func.isNotEmpty(user.getPassword())) {
            user.setPassword(MD5.getMD5(user.getPassword()));
            //user.setPassword(DigestUtil.encrypt(user.getPassword()));
        }
        //判断用户是否存在（登录账号）
        Integer cnt = baseMapper.selectCount(Wrappers.<User>query().lambda().eq(User::getTenantId, user.getTenantId()).eq(User::getAccount, user.getAccount()));
        if (cnt > 0) {
            throw new ApiException("当前用户已存在!");
        }
        return saveOrUpdate(user);
    }

    @Override
    public IPage<User> selectUserPage(IPage<User> page, User user) {
        return page.setRecords(baseMapper.selectUserPage(page, user));
    }

    @Override
    public UserInfo userInfo(Long userId) {
        UserInfo userInfo = new UserInfo();
        User user = baseMapper.selectById(userId);
        userInfo.setUser(user);
        if (Func.isNotEmpty(user)) {
            List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(user.getRoleId()));
            userInfo.setRoles(roleAlias);
        }
        return userInfo;
    }

    @Override
    public UserInfo userInfo(String tenantId, String account, String password) {
        UserInfo userInfo = new UserInfo();
        password = MD5Utils.encodeByMD5(password);
        User user = baseMapper.getUser(tenantId, account, password);
        userInfo.setUser(user);
        if (Func.isNotEmpty(user)) {
            List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(user.getRoleId()));
            userInfo.setRoles(roleAlias);
        }
        return userInfo;
    }

    @Override
    public boolean grant(String userIds, String roleIds) {
        User user = new User();
        user.setRoleId(roleIds);
        return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toIntList(userIds)));
    }

    @Override
    public boolean resetPassword(String userIds) {
        User user = new User();
        user.setPassword(DigestUtil.encrypt(CommonConstant.DEFAULT_PASSWORD));
        user.setUpdateTime(DateUtil.now());
        return this.update(user, Wrappers.<User>update().lambda().in(User::getId, Func.toIntList(userIds)));
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String newPassword1) {
        User user = getById(userId);
        if (!newPassword.equals(newPassword1)) {
            throw new ServiceException("请输入正确的确认密码!");
        }
        if (!user.getPassword().equals(DigestUtil.encrypt(oldPassword))) {
            throw new ServiceException("原密码不正确!");
        }
        return this.update(Wrappers.<User>update().lambda().set(User::getPassword, DigestUtil.encrypt(newPassword)).eq(User::getId, userId));
    }

    @Override
    public List<String> getRoleName(String roleIds) {

        return baseMapper.getRoleName(Func.toStrArray(roleIds));
    }

    @Override
    public List<String> getDeptName(String deptIds) {

        return baseMapper.getDeptName(Func.toStrArray(deptIds));
    }

    @Override
    public boolean saveBladeUser(User user) {
        //密码加密处理
        user.setPassword(MD5Utils.encodeByMD5(user.getPassword()));
        return baseMapper.saveBladeUser(user);
    }

    @Override
    public Integer getMaxChatNo() {

        return baseMapper.getMaxChatNo();
    }

    @Override
    public boolean updataUserPhone(String newPhone,Integer id) {

        return baseMapper.updataUserPhone(newPhone,id);
    }

    @Override
    public User getUser(Integer id)
    {

        return baseMapper.getUser(id);
    }

    @Override
    public User getUserByPhone(String phoneNum) {

        return baseMapper.getUserByPhone(phoneNum);
    }

    @Override
    public boolean updatePasswordById(String newPassword,Integer id) {

        return baseMapper.updatePasswordById(newPassword,id);
    }

    @Override
    public User selectUser(User user) {
        //加密在登录
        user.setPassword(MD5Utils.encodeByMD5(user.getPassword()));
        return baseMapper.selectUser(user);
    }

    @Override
    public BaseStaffextVO getBaseStaffextVO(Integer userId) {

        return baseMapper.getBaseStaffextVO(userId);
    }


    public User selectUserInfo(Integer userId) {

        return baseMapper.selectUserInfo(userId);
    }



    @Override
    public int updateStaffinfo(BaseStaffext baseStaffext) {

        return baseMapper.updateStaffinfo(baseStaffext);
    }

    @Override
    public BaseStaffinfo getbaseStaffInfoByJobnNum(String jobNum) {

        return baseMapper.getbaseStaffInfoByJobnNum(jobNum);
    }

    @Override
    public int updateBaseSatffinfo(String jobNum, Integer userId) {

        return baseMapper.updateBaseSatffinfo(jobNum,userId);
    }


}
