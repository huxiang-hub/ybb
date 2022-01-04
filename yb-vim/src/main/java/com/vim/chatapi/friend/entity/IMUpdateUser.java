package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IMUpdateUser implements Serializable {
    @JsonProperty("From_Account")
    private String From_Account;//修改资料的印聊号
    @JsonProperty("ProfileItem")
    private List<ProfileItem> ProfileItem;//修改哪些资料
}
