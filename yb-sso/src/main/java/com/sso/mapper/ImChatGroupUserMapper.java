package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.chatapi.entity.ImChatGroup;
import com.sso.chatapi.entity.ImChatGroupUser;
import com.sso.chatapi.entity.ImUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 群 Mapper 接口
 * </p>
 *
 * @author 乐天
 * @since 2018-10-28
 */
@Mapper
public interface ImChatGroupUserMapper extends BaseMapper<ImChatGroupUser> {

    List<ImChatGroupUser> getByGroupId(String chatGroupId);

    ImChatGroupUser getGroup(String chatGroupId, String userId);

    Boolean dropOutGroup(String chatGroupId, String userId);

    List<ImUser> getGroupUserByGroupId(String chatGroupId);

    List<ImChatGroup> getChatGroupByUserId(String userId);

    int getCount(String chatGroupId);

    Boolean disBandGroup(String chatGroupId);

}
