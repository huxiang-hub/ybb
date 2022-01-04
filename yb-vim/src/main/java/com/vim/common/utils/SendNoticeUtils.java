package com.vim.common.utils;

import com.vim.feign.PushNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author by SUMMER
 * @date 2020/4/8.
 */
@Component
public class SendNoticeUtils {

    private static SendNoticeUtils noticeUtils;

    @Autowired
    private PushNotice pushNotice;

    @PostConstruct
    public void init() {
        noticeUtils = this;
        noticeUtils.pushNotice = pushNotice;
    }

    public static void sendNotice(String userId,String content,String type){
        try {
            noticeUtils.pushNotice.sendMsgToUser(userId, content, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
