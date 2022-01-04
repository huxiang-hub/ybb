package com.sso;

import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author by my
 * @date 2020/5/26
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringCloudApplication
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES)
@ServletComponentScan(basePackages = "com.sso")
public class SsoApplication {
    public static void main(String[] args) {
        BladeApplication.run("yb-sso", SsoApplication.class, args);
    }
}
