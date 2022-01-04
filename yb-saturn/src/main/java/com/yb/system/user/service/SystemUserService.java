package com.yb.system.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.system.user.request.RoleAuthorRequest;
import com.yb.system.user.request.SaveAndUpdateRequest;
import com.yb.system.user.request.UserListRequest;
import com.yb.system.user.response.SysUserPageVO;

import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/10/21 11:05
 */
public interface SystemUserService {
    /**
     * 分页
     *
     * @param request
     * @return
     */
    IPage<SysUserPageVO> page(IPage<SysUserPageVO> page, UserListRequest request);

    /**
     * 新增/修改
     *
     * @param request
     */
    void saveAndUpdateRequest(SaveAndUpdateRequest request);

    /**
     * 角色授权
     *
     * @param request
     */
    void roleAuthor(RoleAuthorRequest request);

    void resetPassword(Integer id, String password);

    void delete(List<Integer> ids);

}
