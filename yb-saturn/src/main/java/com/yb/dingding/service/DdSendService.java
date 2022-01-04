package com.yb.dingding.service;

public interface DdSendService {
    /**
     * 钉钉消息推送
     * @param msg
     * @param apUnique
     */
    void asyncsend_v2(String msg, String apUnique, String userId);

    /**
     * 钉钉群消息发送
     * @param msg
     * @param apUnique
     * @param chatid
     */
    void send(String msg, String apUnique, String chatid);
}
