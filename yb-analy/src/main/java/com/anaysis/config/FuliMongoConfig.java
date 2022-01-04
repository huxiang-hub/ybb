package com.anaysis.config;

import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb.fuli")
public class FuliMongoConfig extends AbstractMongoConfig {


    public MongoDbFactory mongoDbFactory() {
        return new SimpleMongoDbFactory(new MongoClient(host, port), database);
    }

    @Override
    @Bean(name = "fuliMongoTemplate")
    public MongoTemplate getMongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}