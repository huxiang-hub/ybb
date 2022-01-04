package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MsgBody implements Serializable {

    @JsonProperty("MsgType")
    private String MsgType;//TIM 消息对象类型，目前支持的消息对象包括：TIMTextElem(文本消息)，TIMFaceElem(表情消息)，TIMLocationElem(位置消息)，TIMCustomElem(自定义消息)
    @JsonProperty("MsgContent")
    private MsgContent MsgContent;//对于每种 MsgType 用不同的 MsgContent 格式，具体可参考 消息格式描述
}
