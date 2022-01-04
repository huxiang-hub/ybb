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
public class DeleteFirend {

    /**
     * 需要为该 UserID 删除好友
     */

    @JsonProperty("From_Account")
    private String From_Account;
    /**
     * 待删除的好友的 UserID 列表
     */

    @JsonProperty("To_Account")
    private List<String> To_Account;
    /**
     * 删除模式(单向删除好友:Delete_Type_Single  双向删除好友:Delete_Type_Both)
     */

    @JsonProperty("DeleteType")
    private String DeleteType;



}
