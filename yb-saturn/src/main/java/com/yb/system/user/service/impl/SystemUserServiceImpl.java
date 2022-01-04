package com.yb.system.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Joiner;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.mapper.BaseStaffinfoMapper;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.system.role.entity.Role;
import com.yb.system.role.mapper.SaRoleMapper;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.mapper.SaUserMapper;
import com.yb.system.user.request.RoleAuthorRequest;
import com.yb.system.user.request.SaveAndUpdateRequest;
import com.yb.system.user.request.UserListRequest;
import com.yb.system.user.response.SysUserPageVO;
import com.yb.system.user.service.SystemUserService;
import org.springblade.common.exception.CommonException;
import org.springblade.common.modelMapper.ModelMapperUtil;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @Description: 系统用户service实现类
 * @Author my
 * @Date Created in 2020/10/21
 */
@Service
public class SystemUserServiceImpl extends BaseServiceImpl<SaUserMapper, SaUser> implements SystemUserService {

    @Autowired
    private SaUserMapper saUserMapper;

    @Autowired
    private SaRoleMapper saRoleMapper;

    @Autowired
    private BaseStaffinfoMapper baseStaffinfoMapper;


    @Override
    public IPage<SysUserPageVO> page(IPage<SysUserPageVO> page, UserListRequest request) {
        List<SysUserPageVO> vo = saUserMapper.page(page, request);
        if (vo.isEmpty()) {
            return page.setRecords(new ArrayList<>());
        }
        List<Role> role = saRoleMapper.selectList(null);
        vo.forEach(o -> {
            String roleIds = o.getRoleIds();
            if (Func.isNotBlank(roleIds)) {
                List<String> ids = Arrays.asList(o.getRoleIds().split(","));
                List<Role> roles = role.stream().filter(b -> {
                    if (ids.contains(String.valueOf(b.getId()))) {
                        return true;
                    } else {
                    }
                    return false;
                }).collect(Collectors.toList());
                o.setRole(roles);
            }
        });
        return page.setRecords(vo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAndUpdateRequest(SaveAndUpdateRequest request) {
        //新增,处理重复账号
        List<SaUser> list = saUserMapper.findByAccount(request.getAccount(), request.getId());
        int count = 0;
        while (!list.isEmpty()) {
            count++;
            request.setAccount(request.getAccount() + count);
            list = saUserMapper.findByAccount(request.getAccount(), request.getId());
        }
        if (request.getId() == null) {
            SaUser user = ModelMapperUtil.getStrictModelMapper().map(request, SaUser.class);
            user.setCreateTime(new Date());
            user.setRoleId(request.getRoleIds());
            user.setTenantId(DBIdentifier.getProjectCode());
            if (Func.isNotBlank(request.getPassword())) {
                user.setPassword(DigestUtil.encrypt(request.getPassword()));
            }
            user.setName(user.getRealName());
            saUserMapper.insert(user);
            //同步增加员工信息
            BaseStaffinfo baseStaffinfo = new BaseStaffinfo();
            baseStaffinfo.setUserId(user.getUserId());
            baseStaffinfo.setCreateAt(new Date());
            baseStaffinfo.setDpId(Integer.valueOf(user.getDeptId()));
            baseStaffinfo.setJobnum(user.getAccount());
            baseStaffinfo.setIsUsed(1);
            baseStaffinfo.setPhone(user.getPhone());
            baseStaffinfo.setName(user.getRealName());
            baseStaffinfo.setUserId(user.getId());
            baseStaffinfoMapper.insert(baseStaffinfo);
            return;
        }
        //修改
        SaUser saUser = saUserMapper.selectById(request.getId());
        if (Func.isNotBlank(request.getPassword())) {
            request.setPassword(DigestUtil.encrypt(request.getPassword()));
        } else {
            request.setPassword(saUser.getPassword());
        }
        ModelMapperUtil.getStrictModelMapper().map(request, saUser);
        saUser.setUpdateTime(new Date());
        saUser.setRoleId(request.getRoleIds());
        saUser.setName(saUser.getRealName());
        saUserMapper.updateById(saUser);
        //同步修改员工信息
        BaseStaffinfo staffinfo = baseStaffinfoMapper.selectOne(new QueryWrapper<BaseStaffinfo>().eq("user_id", request.getId()));
        if (staffinfo != null) {
            staffinfo.setDpId(Integer.valueOf(saUser.getDeptId()));
            staffinfo.setJobnum(saUser.getAccount());
            staffinfo.setIsUsed(saUser.getIsDeleted());
            staffinfo.setPhone(saUser.getPhone());
            staffinfo.setName(saUser.getRealName());
            baseStaffinfoMapper.updateById(staffinfo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void roleAuthor(RoleAuthorRequest request) {
        List<SaUser> saUsers = saUserMapper.selectBatchIds(request.getId());
        if (saUsers.isEmpty()) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到用户信息,授权失败");
        }
        if (!request.getRoleIds().isEmpty()) {
            saUsers.forEach(o -> {
                o.setRoleId(Joiner.on(",").join(request.getRoleIds()));
                saUserMapper.updateById(o);
            });
        } else {
            saUsers.forEach(o -> {
                o.setRoleId(null);
                saUserMapper.updateById(o);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(Integer id, String password) {
        SaUser saUser = saUserMapper.selectById(id);
        if (saUser == null) {
            throw new CommonException(HttpStatus.NOT_FOUND.value(), "未找到用户信息,重置失败");
        }
        //密码加密
        saUser.setPassword(DigestUtil.encrypt(password));
        saUserMapper.updateById(saUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        List<SaUser> saUsers = saUserMapper.selectBatchIds(ids);
        if (!saUsers.isEmpty()) {
            saUserMapper.updateStatus(ids);
        }
    }
}
