package com.vim.chatapi.user.constant;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 常量配置类
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "constants")
public class Constants {

    @NotEmpty
    @Value("weCatAppId")
    private String weChatAppId;

    @NotEmpty
    @Value("weCatAppSecret")
    private String weChatAppSecret;

    @NotEmpty
    @Value("weChatTokenUrl")
    private String weChatTokenUrl;

    @NotEmpty
    @Value("weCatRedirectUrl")
    private String weChatRedirectUrl;

    @NotEmpty
    @Value("scope")
    private String scope;

    @NotEmpty
    @Value("state")
    private String state;


}