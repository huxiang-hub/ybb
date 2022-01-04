package com.sso.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.sso.system.entity.SaUser;
import com.sso.system.entity.SaUserInfo;
import com.sso.mapper.SaUserMapper;
import com.sso.system.service.SaIUserService;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.tool.MD5;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.provider.ClientDetails;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务实现类
 *
 * @author Jenny wang
 */
@Service
public class SaIUserServiceImpl extends BaseServiceImpl<SaUserMapper, SaUser> implements SaIUserService {

    @Override
    public boolean submit(SaUser saUser) {
        //密码加密
        if (Func.isNotEmpty(saUser.getPassword())) {
            saUser.setPassword(MD5.getMD5(saUser.getPassword()));
            //user.setPassword(DigestUtil.encrypt(user.getPassword()));
        }
        //判断用户是否存在（登录账号）
        Integer cnt = baseMapper.selectCount(Wrappers.<SaUser>query().lambda().eq(SaUser::getTenantId, saUser.getTenantId()).eq(SaUser::getAccount, saUser.getAccount()));
        if (cnt > 0) {
            throw new ApiException("当前用户已存在!");
        }
        return saveOrUpdate(saUser);
    }

    @Override
    public IPage<SaUser> selectUserPage(IPage<SaUser> page, SaUser saUser) {
        return page.setRecords(baseMapper.selectUserPage(page, saUser));
    }

    @Override
    public SaUserInfo userInfo(Long userId) {
        SaUserInfo saUserInfo = new SaUserInfo();
        SaUser saUser = baseMapper.selectById(userId);
        saUserInfo.setSaUser(saUser);
        if (Func.isNotEmpty(saUser)) {
            List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(saUser.getRoleId()));
            saUserInfo.setRoles(roleAlias);
        }
        return saUserInfo;
    }

    @Override
    public SaUserInfo userInfo(String tenantId, String account, String password) {
        SaUserInfo saUserInfo = new SaUserInfo();
        SaUser saUser = baseMapper.getUser(tenantId, account, password);
        saUserInfo.setSaUser(saUser);
        if (Func.isNotEmpty(saUser)) {
            List<String> roleAlias = baseMapper.getRoleAlias(Func.toStrArray(saUser.getRoleId()));
            saUserInfo.setRoles(roleAlias);
        }
        return saUserInfo;
    }

    @Override
    public boolean grant(String userIds, String roleIds) {
        SaUser saUser = new SaUser();
        saUser.setRoleId(roleIds);
        return this.update(saUser, Wrappers.<SaUser>update().lambda().in(SaUser::getId, Func.toIntList(userIds)));
    }

    @Override
    public boolean resetPassword(String userIds) {
        SaUser saUser = new SaUser();
        saUser.setPassword(DigestUtil.encrypt(DigestUtil.encrypt(CommonConstant.DEFAULT_PASSWORD)));
        saUser.setUpdateTime(DateUtil.now());
        return this.update(saUser, Wrappers.<SaUser>update().lambda().in(SaUser::getId, Func.toIntList(userIds)));
    }

    @Override
    public boolean updatePassword(Integer userId, String oldPassword, String newPassword, String newPassword1) {
        SaUser saUser = getById(userId);
        if (!newPassword.equals(newPassword1)) {
            throw new ServiceException("请输入正确的确认密码!");
        }
        if (!saUser.getPassword().equals(DigestUtil.encrypt(oldPassword))) {
            throw new ServiceException("原密码不正确!");
        }
        return this.update(Wrappers.<SaUser>update().lambda().set(SaUser::getPassword, DigestUtil.encrypt(newPassword)).eq(SaUser::getId, userId));
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
    public R<SaUserInfo> saUserInfo(Long userId){
        return  R.data(userInfo(userId));
    }

    /**
     * 获取用户信息
     *
     * @param tenantId 租户ID
     * @param account    账号
     * @param password   密码
     * @return
     */
    @Override
    public R<SaUserInfo> saUserInfo(String tenantId, String account, String password){
        return R.data(userInfo(tenantId, account, password));
    }

    @Override
    public ClientDetails loadClientByClientId(String clientId) {
        try {
            ClientDetails details =baseMapper.getClientDetails(clientId);
            return  details;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public IPage<SaUser> getUserList(IPage<SaUser> page, SaUser SaUser) {
        return page.setRecords(baseMapper.getUserList(page,SaUser));
    }
//查询单个用户信息通过名字，账号
    @Override
    public SaUser getOneUserCondition(SaUser saUser) {
        return baseMapper.getOneUserCondition(saUser);
    }

    @Override
    public SaUser getOneUserById(Integer Id) {
        return baseMapper.getOneUserById(Id);
    }
    /**
     * 修改角色
     */
    @Override
    public Boolean updateOoleId(SaUser saUser){
        if(baseMapper.updateOoleId(saUser) > 0){
            return true;
        }
        return false;
    }
}
