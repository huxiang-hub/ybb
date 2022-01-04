package com.vim.chatapi.message.controller;

import com.vim.message.service.IImMessageService;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by SUMMER
 * @date 2020/4/7.
 */
@RestController
@RequestMapping("/api/msg")
public class MessageController {
    @Autowired
    private IImMessageService messageService;

    /**
     * 查看聊天消息记录
     *
     * @param userId
     * @param content
     * @return
     */
    @GetMapping("/searchMsgFriend")
    public R getHistoryMsg(String userId, String friendId, String content) {
        return R.data(messageService.getHiMsgFriend(userId, friendId, content));
    }

    /**
     * 查看群聊天消息记录
     *
     * @param chatGroupId
     * @param content
     * @return
     */
    @GetMapping("/searchMsgGroup")
    public R getHistoryMsg(String chatGroupId, String content) {
        return R.data(messageService.getHiMsgGroup(chatGroupId, content));
    }
}
