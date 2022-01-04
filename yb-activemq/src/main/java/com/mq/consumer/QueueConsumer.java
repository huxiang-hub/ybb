package com.mq.consumer;

import com.mq.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.jms.*;

/**
 * @Description: 队列监听
 * @Author my
 */
@Component
@Slf4j
public class QueueConsumer {

    @Resource
    @Qualifier("redisTemp")
    private RedisTemplate<String, Object> redisTemplate;
    RedisUtil redisUtil;

    //连接账号
    @Value("${spring.activemq.user}")
    private String userName;
    //连接密码
    @Value("${spring.activemq.password}")
    private String password;
    //连接地址
    @Value("${spring.activemq.broker-url}")
    private String brokerURL;
    //connection的工厂
    private ConnectionFactory factory;
    //连接对象
    private Connection connection;
    //一个操作会话
    private Session session;
    //目的地，其实就是连接到哪个队列，如果是点对点，那么它的实现是Queue，如果是订阅模式，那它的实现是Topic
    private Destination destination;
    //消费者，就是接收数据的对象
    private MessageConsumer consumer;

    @Value("${myqueue}")
    private String myQue;

    @PostConstruct
    public void init() {
        redisUtil = new RedisUtil(redisTemplate);
        new Thread(() -> {
            start();
        }).start();
    }

    public void start() {
        try {
            //根据用户名，密码，url创建一个连接工厂
            factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
            //从工厂中获取一个连接
            connection = factory.createConnection();
            //连接启动
            connection.start();
            //创建一个session
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            //创建一个到达的目的地，activemq不可能同时只能跑一个队列吧，这里就是连接了一个名为"Alarm"的队列，这个会话将会到这个队列，当然，如果这个队列不存在，将会被创建
            destination = session.createQueue(myQue);
            //根据session，创建一个接收者对象
            consumer = session.createConsumer(destination);

            //实现一个消息的监听器
            //实现这个监听器后，以后只要有消息，就会通过这个监听器接收到
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        //获取到接收的数据
                        String text = ((TextMessage) message).getText();
                        System.out.println(text);
                        session.rollback();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            //关闭接收端，也不会终止程序哦
//            consumer.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }



    // @JmsListener(destination = "${myqueue}", containerFactory = "jmsListenerContainerQueue")
    public void receive(TextMessage message) throws JMSException {
        if (redisUtil == null) {
            System.out.println("11111111111");
        }
        try {
            if (redisUtil.get(message.getJMSMessageID()) == null) {
                System.out.println("收到主题消息" + message.getText());
                //存入redis防止重复消费
                redisUtil.set(message.getJMSMessageID(), message.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("消息消费出现异常[messageId:{}, text:{}]", message.getJMSMessageID(), message.getText());
        }
    }
}
