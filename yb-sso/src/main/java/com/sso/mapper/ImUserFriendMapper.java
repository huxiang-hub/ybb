package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.chatapi.entity.ImGroup;
import com.sso.chatapi.entity.ImUser;
import com.sso.chatapi.entity.ImUserFriend;
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

    ImUser searchFriend(String search,String tenantId);

    ImUserFriend getFriend(@RequestParam("userId") String userId,
                           @RequestParam("friendId") String friendId);

    boolean updateByPrimary(ImUserFriend userFriend);

}
