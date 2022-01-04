package com.vim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vim.chatapi.user.entity.SaUser;
import com.vim.user.entity.ImGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.entity.ImUserFriend;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2018-12-31
 */
public interface IImUserFriendService extends IService<ImUserFriend> {

    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     *
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    List<ImGroup> getUserFriends(String userId);

    boolean delFriend(String userId, String friendId);

    List<ImUser> getFriendsList(String userId);

    SaUser searchFriend(String search,String tenantId);

    ImUserFriend getFriend(String userId, String friendId);

    boolean updateByPrimary(ImUserFriend userFriend);

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    List<SaUser> friendList(String userId);
}
