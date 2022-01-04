package com.yb.panelapi.common;

import org.springblade.message.feign.ImMessageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author by SUMMER
 * @date 2020/4/10.
 */
@Component
public class SendMsgUtils {

    @Autowired
    private ImMessageClient messageClient;

    //重点二：建一个静态的本类
    private static SendMsgUtils sendMsgUtils;

    //重点三：初始化
    @PostConstruct
    public void init() {
        sendMsgUtils= this;
        sendMsgUtils.messageClient= this.messageClient;
    }
    public static void sendToUser(String usIds, String content, String type) {

        if (usIds != null) {
            String[] split = usIds.split("\\|");
            if (split == null || split.length < 2) {
                return;
            }
            //手机推送消息给所有用户
            for (int i = 1, length = split.length; i < length; i++) {
                try {
                    sendMsgUtils.messageClient.sendMsgToUser(split[i], content, type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
