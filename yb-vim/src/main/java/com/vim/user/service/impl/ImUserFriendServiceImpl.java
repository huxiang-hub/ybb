package com.vim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.chatapi.user.entity.SaUser;
import com.vim.user.entity.ImGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.entity.ImUserFriend;
import com.vim.user.mapper.ImUserFriendMapper;
import com.vim.user.service.IImUserFriendService;
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
    public SaUser searchFriend(String search,String tenantId) {
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

    @Override
    public List<SaUser> friendList(String userId) {
        List<SaUser> saUserList = imUserFriendMapper.friendList(userId);
        return saUserList;
    }
}
