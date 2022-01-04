package com.vim.chatapi.friend.entity;

/**
 * Copyright 2020 bejson.com
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2020-07-11 17:47:42
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

@Data
public class AddFirend {

    /**
     * 需要为该 UserID 添加好友
     */

    @JsonProperty("From_Account")
    private String From_Account;
    /**
     * 好友结构体对象
     */
    @JsonProperty("AddFriendItem")
    private List<AddFriendItem> AddFriendItem;
    /**
     * 	加好友方式（默认双向加好友方式）：
     * Add_Type_Single 表示单向加好友
     * Add_Type_Both 表示双向加好友
     */
    @JsonProperty("AddType")
    private String AddType;
    /**
     * 管理员强制加好友标记：1表示强制加好友，0表示常规加好友方式
     */
    @JsonProperty("ForceAddFlags")
    private int ForceAddFlags;


}
