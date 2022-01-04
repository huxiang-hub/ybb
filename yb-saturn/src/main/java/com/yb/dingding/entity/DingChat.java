package com.yb.dingding.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "创建群返回对象")
public class DingChat implements Serializable {
    @ApiModelProperty(value = "返回码")
    private int errcode;
    private String errmsg;
    @ApiModelProperty(value = "群会话的ID")
    private String chatid;
//    @ApiModelProperty(value = "群会话的ID")
    private String openConversationId;
    @ApiModelProperty(value = "会话类型：2：企业群。")
    private int conversationTag;
}
