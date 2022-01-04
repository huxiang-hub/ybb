package com.yb.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.redis.redisson")
@Data
public class RedissonProperties {

    private String config;

}
