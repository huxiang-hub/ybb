package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.chatapi.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 乐天
 * @since 2018-10-07
 */
@Component
@Qualifier("imUserMapper")
@Mapper
public interface ImUserMapper extends BaseMapper<ImUser> {

    User getUserInfo(String userId);

    /**
     * 根据用户id 获取好友的分组
     *
     * @param userId id
     * @return List<ImGroup>
     */
    List<ImGroup> getGroupUsers(String userId);

    /**
     * 根据用户id 获取群组
     *
     * @param userId id
     * @return List<ImGroup>
     */
    List<ImChatGroup> getUserGroups(String userId);


    /**
     * 获取群组的用户
     *
     * @param chatId 群组id
     * @return 用户List
     */
    List<ImUser> getChatUserList(String chatId);

    StatisEntity getStatist(
            @PathParam("userId") String userId,
            @PathParam("startDate") String startDate,
            @PathParam("endDate") String endDate);

    ImUser getByFromId(String fromId);


}
