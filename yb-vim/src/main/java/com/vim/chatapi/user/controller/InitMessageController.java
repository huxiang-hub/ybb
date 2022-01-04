package com.vim.chatapi.user.controller;

import com.vim.message.entity.ImMessage;
import com.vim.message.service.IImMessageService;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImUserFriendService;
import com.vim.user.service.IImUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by SUMMER
 * @date 2020/4/9.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/init")
public class InitMessageController {

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;
    @Resource
    @Qualifier(value = "imUserFriendService")
    private IImUserFriendService imUserFriendService;
    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService messageService;

    @RequestMapping("/initInfo")
    public R initInfo(String userId) {
        Map<String, Object> result = new HashMap<>();
        /**
         * 初始化好友未读信息
         */
        List<ImUser> friends = imUserFriendService.getFriendsList(String.valueOf(userId));
        Map<String, List<ImMessage>> friendMessage = new HashMap<>();
        for (ImUser friend : friends) {
            List<ImMessage> messages = messageService.getUnReadFriend(userId, friend.getId());
            if (messages != null && !messages.isEmpty()) {
                friendMessage.put(friend.getId(), messages);
            }
        }
        /**
         * 初始化用户群组未读消息
         */
        List<ImChatGroup> chatGroups = imUserService.getChatGroups(String.valueOf(userId));
        Map<String, List<ImMessage>> groupMessage = new HashMap<>();
        for (ImChatGroup chatGroup : chatGroups) {
            List<ImMessage> messages = messageService.getUnReadGroup(chatGroup.getId());
            if (messages != null && !messages.isEmpty()) {
                groupMessage.put(chatGroup.getId(), messages);
            }
        }
        /**
         * 初始化未读的好友申请列表
         */
        List<ImMessage> applyNotice = messageService.getUnReadApply(userId);
        //初始化用户信息列表
        result.put("friendMessage", friendMessage);
        result.put("groupMessage", groupMessage);
        result.put("applyNotice", applyNotice);

        return null;
    }
}
