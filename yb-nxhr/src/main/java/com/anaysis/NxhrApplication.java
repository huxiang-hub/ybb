package com.anaysis;

import lombok.extern.slf4j.Slf4j;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author lzb
 * @Date 2020/11/25 09:14
 **/
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages ={AppConstant.BASE_PACKAGES,"com.anaysis"} )
@SpringCloudApplication
@EnableSwagger2
@EnableFeignClients
public class NxhrApplication {
    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext context = BladeApplication.run("yb-nxhr", NxhrApplication.class, args);
        Environment env = context.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
                "Application NxhrApplication is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                "Doc: \t\thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}
