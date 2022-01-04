package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "飞书消息参数对象")
public class FeiShuSendVO implements Serializable {
    @ApiModelProperty(value = "用户的飞书open_id")
    @JsonProperty("open_id")
    private String openId;
    //    @JsonProperty("root_id")
//    private String rootId;
    @ApiModelProperty(value = "飞书群id")
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("userId")
    @ApiModelProperty(value = "飞书用户id")
    private String userId;
    @JsonProperty("email")
    @ApiModelProperty(value = "飞书用户邮箱")
    private String email;
    @ApiModelProperty(value = "消息类型,text为文本")
    @JsonProperty("msg_type")
    private String msgType = "text";
    @JsonProperty("content")
    @ApiModelProperty(value = "消息内容对象")
    private Content content;
}
