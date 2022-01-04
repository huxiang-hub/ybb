package org.springblade.common.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @Description:
 * @Author my
 */
@Component
public class Config {

    @Value("${myqueue}")
    private String myQue;

    @Value("${topicqueue}")
    private String topicQueue;

    @Bean
    private Queue queue() {
        return new ActiveMQQueue(myQue);
    }
    @Bean
    private Topic topic() {
        return new ActiveMQTopic(topicQueue);
    }
}
