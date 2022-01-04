package org.springblade.common.activemq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.*;
import java.util.UUID;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/12/4
 */
@Component
@EnableJms
public class ActiveMsgUtil {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    private static ActiveMsgUtil activeMsgUtil;

    @Resource
    private Queue queue;

    @Resource
    private Topic topic;

    @PostConstruct
    public void init() {
        activeMsgUtil = this;
        activeMsgUtil.jmsMessagingTemplate = this.jmsMessagingTemplate;
        activeMsgUtil.queue = this.queue;
        activeMsgUtil.topic = this.topic;
    }


    /**
     * 推送主题消息
     *
     * @param json 消息内容json
     */
    public static void pushTopicMsg(String json) {
        activeMsgUtil.jmsMessagingTemplate.convertAndSend(activeMsgUtil.topic, UUID.randomUUID().toString().substring(0, 6));
    }

    /**
     * 发送队列消息
     *
     * @param queueName 发送的队列名称，为空采取默认
     * @param data      发送的消息 json
     */
    public  static void sendQueMsg(String queueName, String data) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        // 获取连接工厂
        ConnectionFactory connectionFactory = activeMsgUtil.jmsMessagingTemplate.getConnectionFactory();

        // 获取连接
        connection = connectionFactory.createConnection();
        connection.start();
        // 开启事务设置签收
        session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        // 创建一个消息队列
        if (StringUtils.isNotBlank(queueName)) {
            Queue queue = session.createQueue(queueName);
            producer = session.createProducer(queue);
        } else {
            producer = session.createProducer(activeMsgUtil.queue);
        }
        TextMessage message = session.createTextMessage(data);
        message.setJMSMessageID(UUID.randomUUID().toString());
        // 发送消息
        producer.send(message);
        try {
            //事务提交
            session.commit();
        } catch (Exception e) {
            //回滚
            session.rollback();
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
