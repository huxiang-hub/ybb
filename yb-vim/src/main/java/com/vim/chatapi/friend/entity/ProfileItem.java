package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProfileItem implements Serializable {
    @JsonProperty("Tag")
    private String Tag;//指定要设置的资料字段的名称
    @JsonProperty("Value")
    private String Value;//待设置的资料字段的值
}
