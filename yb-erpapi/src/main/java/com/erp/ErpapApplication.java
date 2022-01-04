package com.erp;

import lombok.extern.slf4j.Slf4j;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.UnknownHostException;

/**
 * @Author lzb
 * @Date 2020/11/25 09:14
 **/
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages ={AppConstant.BASE_PACKAGES,"com.erp"} )
@SpringCloudApplication
@EnableSwagger2
@EnableFeignClients
public class ErpapApplication {
    public static void main(String[] args) throws UnknownHostException {
        log.info("start-----------------------------------");
        BladeApplication.run("yb-erpap", ErpapApplication.class, args);
        log.info("end-----------------------------------");
    }
}
