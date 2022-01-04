package com.vim.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vim.message.entity.ImMessage;
import com.vim.message.mapper.ImMessageMapper;
import com.vim.message.service.IImMessageService;
import org.springblade.common.tool.ChatType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 服务实现类
 *
 * @author 乐天
 * @since 2018-10-08
 */
@Service
@Qualifier(value = "iImMessageService")
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements IImMessageService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(ImMessage imMessage) {
        new ImMessageServiceImpl.SaveChatMessageThread(imMessage).run();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ImMessage> getUnReadMessage(String toId) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", toId);
        queryWrapper.eq("read_status", "1");
        List<ImMessage> messageList = baseMapper.selectList(queryWrapper);
        for (ImMessage message : messageList) {
            message.setReadStatus(ChatType.READED.getType());
            this.updateById(message);
        }
        return messageList;
    }

    @Override
    public List<ImMessage> getHiMsgFriend(String userId, String friendId, String content) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("from_id", userId);
        queryWrapper.eq("to_id", friendId);
        queryWrapper.like("content", content);
        queryWrapper.or();
        queryWrapper.eq("from_id", friendId);
        queryWrapper.eq("to_id", userId);
        queryWrapper.like("content", content);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ImMessage> getHiMsgGroup(String chatGroupId, String content) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", chatGroupId);
        queryWrapper.like("content", content);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ImMessage> getUnReadFriend(String userId, String friendId) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", userId);
        queryWrapper.eq("from_id", friendId);
        queryWrapper.eq("read_status", ChatType.UNREAD.getType());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ImMessage> getUnReadGroup(String chatGroupId) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", chatGroupId);
        queryWrapper.eq("read_status", ChatType.UNREAD.getType());
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ImMessage> getUnReadApply(String userId) {
        QueryWrapper<ImMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_id", userId);
        queryWrapper.eq("type", ChatType.FRIEND_APPLY.getType());
        queryWrapper.eq("read_status", ChatType.UNREAD.getType());
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 内部类
     */
    class SaveChatMessageThread implements Runnable {

        private ImMessage imMessage;

        public SaveChatMessageThread(ImMessage imMessage) {
            this.imMessage = imMessage;
        }

        @Override
        public void run() {
            save(imMessage);
        }
    }
}
