package com.anaysis;

import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author by SUMMER
 * @date 2020/3/9.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages ={AppConstant.BASE_PACKAGES,"com.anaysis"} )
@SpringCloudApplication
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES)
public class FzApplication {

    public static void main(String[] args) {
        BladeApplication.run("yb-fz", FzApplication.class, args);
    }
}
