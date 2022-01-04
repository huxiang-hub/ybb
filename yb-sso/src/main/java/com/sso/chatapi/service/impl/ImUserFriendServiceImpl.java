package com.sso.chatapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.chatapi.entity.ImGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.ImUserFriend;
import com.sso.chatapi.service.IImUserFriendService;
import com.sso.mapper.ImUserFriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  用户关系表
 * </p>
 *
 * @author jobob
 * @since 2018-12-31
 */
@Service
@Qualifier(value = "imUserFriendService")
public class ImUserFriendServiceImpl extends ServiceImpl<ImUserFriendMapper, ImUserFriend> implements IImUserFriendService {

    @Autowired
    ImUserFriendMapper imUserFriendMapper;

    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     *
     * @param userId 用户ID
     * @return 好友分组的列表
     */
   public List<ImGroup> getUserFriends(String userId){
       return this.imUserFriendMapper.getUserFriends(userId);
    }

    @Override
    public boolean delFriend(String userId, String friendId) {
        return imUserFriendMapper.delFriend(userId,friendId);
    }

    @Override
    public List<ImUser> getFriendsList(String userId) {
        return imUserFriendMapper.getFriendsList(userId);
    }

    @Override
    public ImUser searchFriend(String search,String tenantId) {
        return imUserFriendMapper.searchFriend(search,tenantId);
    }

    @Override
    public ImUserFriend getFriend(String userId, String friendId) {
        return imUserFriendMapper.getFriend(userId,friendId);
    }

    @Override
    public boolean updateByPrimary(ImUserFriend userFriend) {
        return imUserFriendMapper.updateByPrimary(userFriend);
    }
}
