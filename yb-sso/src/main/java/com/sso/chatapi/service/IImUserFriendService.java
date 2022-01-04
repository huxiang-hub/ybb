package com.sso.chatapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.chatapi.entity.ImGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.ImUserFriend;

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

    ImUser searchFriend(String search,String tenantId);

    ImUserFriend getFriend(String userId, String friendId);

    boolean updateByPrimary(ImUserFriend userFriend);

}
