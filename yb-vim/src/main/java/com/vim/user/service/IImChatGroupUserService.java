package com.vim.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImChatGroupUser;
import com.vim.user.entity.ImUser;

import java.util.List;

/**
 * <p>
 * 群 服务类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-28
 */
public interface IImChatGroupUserService extends IService<ImChatGroupUser> {

    List<ImChatGroupUser> getByGroupId(String chatGroupId);

    ImChatGroupUser getGroup(String chatGroupId, String userId);

    Boolean dropOutGroup(String chatGroupId, String userId);

    List<ImUser> getGroupUserByGroupId(String chatGroupId);

    List<ImChatGroup> getChatGroupByUserId(String userId);

    int getCount(String chatGroupId);

    Boolean disBandGroup(String chatGroupId);
}
