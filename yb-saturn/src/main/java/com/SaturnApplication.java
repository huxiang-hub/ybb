package com;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author by SUMMER
 * @date 2020/3/9.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.yb", AppConstant.BASE_PACKAGES})
@EnableCaching//开启注解驱动的缓存管理
@SpringCloudApplication
@EnableFeignClients(basePackages = {AppConstant.BASE_PACKAGES, "com.yb.nxhr.client"})
@EnableScheduling
@ServletComponentScan(basePackages = "com.yb")
@EnableAsync
@EnableSwagger2
public class SaturnApplication {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SaturnApplication.class);

    public static void main(String[] args) {
        log.info("start-----------------------------------");
        BladeApplication.run("yb-saturn", SaturnApplication.class, args);
        log.info("end-----------------------------------");
    }

}
