//package com.mq.consumer;
//
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.stereotype.Component;
//
//import javax.jms.JMSException;
//import javax.jms.TextMessage;
//
///**
// * @Description:
// * @Author my
// */
//@Component
//public class TopicConsumer {
//
//    @JmsListener(destination = "${topicqueue}", containerFactory = "jmsListenerContainerTopic")
//    public void receive(TextMessage textMessage) throws JMSException {
//        System.out.println("收到主题消息" + textMessage.getText());
//    }
//
//    @JmsListener(destination = "${topicqueue}", containerFactory = "jmsListenerContainerTopic")
//    public void senTo(TextMessage textMessage) throws JMSException {
//        System.out.println("收到主题消息" + textMessage.getText());
//    }
//}
