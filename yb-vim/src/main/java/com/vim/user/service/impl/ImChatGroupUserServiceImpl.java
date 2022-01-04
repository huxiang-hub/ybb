package com.vim.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImChatGroupUser;
import com.vim.user.entity.ImUser;
import com.vim.user.mapper.ImChatGroupUserMapper;
import com.vim.user.service.IImChatGroupUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 群 服务实现类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-28
 */
@Service
@Qualifier("imChatGroupUserService")
public class ImChatGroupUserServiceImpl extends ServiceImpl<ImChatGroupUserMapper, ImChatGroupUser> implements IImChatGroupUserService {

    @Autowired
    private ImChatGroupUserMapper groupUserMapper;

    @Override
    public List<ImChatGroupUser> getByGroupId(String chatGroupId) {
        return groupUserMapper.getByGroupId(chatGroupId);
    }

    @Override
    public ImChatGroupUser getGroup(String chatGroupId, String userId) {
        return groupUserMapper.getGroup(chatGroupId,userId);
    }

    @Override
    public Boolean dropOutGroup(String chatGroupId, String userId) {
        return groupUserMapper.dropOutGroup(chatGroupId,userId);
    }

    @Override
    public List<ImUser> getGroupUserByGroupId(String chatGroupId) {
        return groupUserMapper.getGroupUserByGroupId(chatGroupId);
    }

    @Override
    public List<ImChatGroup> getChatGroupByUserId(String userId) {
        return groupUserMapper.getChatGroupByUserId(userId);
    }

    @Override
    public int getCount(String chatGroupId) {
        return groupUserMapper.getCount(chatGroupId);
    }

    @Override
    public Boolean disBandGroup(String chatGroupId) {
        return groupUserMapper.disBandGroup(chatGroupId);
    }
}
