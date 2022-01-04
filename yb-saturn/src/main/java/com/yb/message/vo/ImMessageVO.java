package com.yb.message.vo;

import com.yb.message.entity.ImMessage;
import lombok.Data;

@Data
public class ImMessageVO extends ImMessage {

    /**
     *
     */
    private String name; // 名字
    private Integer flag; //机台消息还是用户员工消息
    /**
     * 发送者名字
     */
    private String fromName;
    /**
     * 接受者名字
     */
    private String toName;
}
