package com.anaysis;

import org.springblade.core.cloud.http.HttpLoggingInterceptor;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author by SUMMER
 * @date 2020/3/9.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages ={AppConstant.BASE_PACKAGES,"com.anaysis"} )
@SpringCloudApplication
@EnableFeignClients(basePackages = {AppConstant.BASE_PACKAGES,"com.anaysis.feign"})
public class XyApplication {

    public static void main(String[] args) {
        BladeApplication.run("yb-xy", XyApplication.class, args);
    }
    @Bean
    public HttpLoggingInterceptor httpLoggingInterceptor() {
        return new HttpLoggingInterceptor();
    }
}
