package com.mq.provider;

import com.alibaba.fastjson.JSONObject;
import org.springblade.common.activemq.ActiveMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Queue;
import java.util.UUID;

/**
 * @Description: 消息提供者demo
 * @Author my
 */
@Component
@EnableJms
public class QueueProvider {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private Queue queue;

    public void pushMsg() {
        jmsMessagingTemplate.convertAndSend(queue, UUID.randomUUID().toString().substring(0, 6));
    }

//    @Scheduled(fixedDelay = 3000)
//    public void pushTopicMsg() {
//        jmsMessagingTemplate.convertAndSend(topic, "主题消息" + UUID.randomUUID().toString().substring(0, 6));
//    }


//   @Scheduled(fixedDelay = 5000)
//    public void productMsg() throws JMSException {
//        Chid chid = new Chid();
//        chid.setName("啊哈哈哈哈哈");
//        ActiveMsgUtil.sendQueMsg(null, JSONObject.toJSONString(chid));
//    }
}
