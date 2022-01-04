package com.yb.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by SUMMER
 * @date 2020/3/14.
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public WebSocketEndpointConfigure newConfigure() {
        return new WebSocketEndpointConfigure();
    }
}