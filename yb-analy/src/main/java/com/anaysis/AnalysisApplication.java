package com.anaysis;

import com.anaysis.mqtt1.mqttClient;
import com.anaysis.socket1.MTTCPS;
import com.anaysis.socket2.TCPS;
import com.anaysis.systask.RunTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Moshow
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = AppConstant.BASE_PACKAGES)
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ServletComponentScan(basePackages = "com.anaysis")
@EnableAsync
public class AnalysisApplication {
    private static Logger logger = LoggerFactory.getLogger(AnalysisApplication.class);

    public static void main(String[] args) {
        //SpringApplication.run(AnalysisApplication.class,args);
        BladeApplication.run("yb-analy", AnalysisApplication.class, args);

        logger.info("启动成功TCPS-8899-白色第一版盒子!");
        //起socket服务 默认8899端口
        TCPS tcps = new TCPS();
        new Thread(tcps).start();

        logger.info("启动成功MTTCPS-8890-黑色自研盒子V1.0!");
        MTTCPS mttcps = new MTTCPS();
        new Thread(mttcps).start();

        mqttClient mqttClient =new mqttClient();
        mqttClient.connectServer();

        logger.info("启动成功RunTimeout!--设备离线检测进程启动");
        RunTimeout runtime = new RunTimeout(); //离线进程，需要加载前面两个事物的对象信息
        RunTimeout.exeRunport.put(tcps.PORT, tcps);
        runtime.exeRunport.put(mttcps.PORT, mttcps);
        Thread myTimeout = new Thread(runtime);
        myTimeout.start();
    }
}
