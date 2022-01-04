package com.sso.chatapi.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.chatapi.message.entity.ImMessage;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 乐天
 * @since 2018-10-08
 */
public interface IImMessageService extends IService<ImMessage> {

    /**
     * 保存消息
     *
     * @param imMessage 消息
     */
    void saveMessage(ImMessage imMessage);

    /**
     * 获取未读消息根据接收人的ID
     *
     * @param toId 接收人的Id
     */
    List<ImMessage> getUnReadMessage(String toId);

    /**
     * 查找好友聊天记录
     * @param userId
     * @param friend
     * @param content
     * @return
     */
    List<ImMessage> getHiMsgFriend(String userId,String friend, String content);

    /**
     * 查找群聊天记录
     * @param chatGroupId
     * @param content
     * @return
     */
    List<ImMessage> getHiMsgGroup(String chatGroupId, String content);

    List<ImMessage> getUnReadFriend(String userId, String friendId);

    List<ImMessage> getUnReadGroup(String friendId);

    List<ImMessage> getUnReadApply(String userId);

}
