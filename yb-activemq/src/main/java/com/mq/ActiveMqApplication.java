
package com.mq;

import org.springblade.core.launch.BladeApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author by SUMMER
 * @date 2020/3/9.
 */
@SpringBootApplication
@SpringCloudApplication
@EnableScheduling
public class ActiveMqApplication {

    public static void main(String[] args) {
        BladeApplication.run("yb-mq", ActiveMqApplication.class, args);
    }
}
