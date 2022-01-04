package com.vim.chatapi.friend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AskAddFriend implements Serializable {

    @JsonProperty("SyncOtherMachine")
    private Integer SyncOtherMachine;// 消息是否同步至发送方,1同步,2不同步,若不填写默认情况下会将消息存 From_Account 漫游
    @JsonProperty("To_Account")
    private String To_Account;//消息接收方 UserID
    @JsonProperty("MsgLifeTime")
    private Integer MsgLifeTime;//消息保存时间(s),默认保存七天
    @JsonProperty("MsgRandom")
    private Integer MsgRandom;//消息随机数，由随机函数产生，用于后台定位问题
    @JsonProperty("MsgTimeStamp")
    private Integer MsgTimeStamp;//消息时间戳，UNIX 时间戳（单位：秒）,选填
    @JsonProperty("MsgBody")
    private List<MsgBody> MsgBody;//消息内容，具体格式请参考 消息格式描述（注意，一条消息可包括多种消息元素，MsgBody 为 Array 类型）

}
