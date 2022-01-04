package com.vim.api.entity;

import lombok.Data;

/**
 * websocket 通讯的json
 *
 * @author 乐天
 * @since 2018-10-07
 */
@Data
public class Message {

    /**
     * 消息来源用户名
     */
    private String username;

    /**
     * 发送者头像
     */
    private String avatar;

    /**
     * 消息的来源ID（如果是私聊，则是用户id，如果是群聊，则是群组id）
     */
    private String id;

    /**
     * 消息类型 0单聊1群聊 推送消息
     */
    private String type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息id
     */
    private String cid;

    /**
     * 是否本人发送
     */
    private boolean mine;

    /**
     * 消息的发送者id
     */
    private String fromid;

    /**
     * 服务端时间戳毫秒数
     */
    private long timestamp;


}
