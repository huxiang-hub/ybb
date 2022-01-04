package com.vim.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vim.chatapi.user.entity.SaUser;
import com.vim.user.entity.ImGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.entity.ImUserFriend;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-12-31
 */
@Mapper
public interface ImUserFriendMapper extends BaseMapper<ImUserFriend> {

    /**
     * 根据用户的ID 获取 用户好友(双向用户关系)
     * @param userId 用户ID
     * @return 好友分组的列表
     */
    List<ImGroup> getUserFriends(String userId);

    boolean delFriend(String userId, String friendId);

    List<ImUser> getFriendsList(String userId);

    SaUser searchFriend(String search,String tenantId);

    ImUserFriend getFriend(@RequestParam("userId") String userId,
                           @RequestParam("friendId") String friendId);

    boolean updateByPrimary(ImUserFriend userFriend);

    /**
     * 查询好友列表
     * @param userId
     * @return
     */
    List<SaUser> friendList(String userId);
}
