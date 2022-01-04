package com.vim.chatapi.friend.entity;

/**
 * Copyright 2020 bejson.com
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Auto-generated: 2020-07-11 17:47:42
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
public class AddFriendItem {
    /**
     * 好友的 UserID
     */
    @JsonProperty("To_Account")
    private String To_Account;
    /**
     * From_Account 对 To_Account 的好友备注，详情可参见 标配好友字段
     */
    @JsonProperty("Remark")
    private String Remark;
    /**
     * From_Account 对 To_Account 的分组信息，添加好友时只允许设置一个分组，因此使用 String 类型即可，详情可参见 标配好友字段
     */
    @JsonProperty("GroupName")
    private String GroupName;
    /**
     * 加好友来源字段，详情可参见 标配好友字段
     */
    @JsonProperty("AddSource")
    private String AddSource;
    /**
     * From_Account 和 To_Account 形成好友关系时的附言信息，详情可参见 标配好友字段
     */
    @JsonProperty("AddWording")
    private String AddWording;


}
