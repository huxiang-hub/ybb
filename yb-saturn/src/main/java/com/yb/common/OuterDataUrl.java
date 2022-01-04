package com.yb.common;

import com.yb.execute.entity.ExecuteState;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Component
@PropertySource({"classpath:application.properties"})
public class OuterDataUrl {

    @Value("${spring.exeurl}")
    //private String exeurl = "http://192.168.0.105:2022/";//获取盒子的解析服务的url请求地址
    private static String exeurl;//获取盒子的解析服务的url请求地址

    private static RestTemplate restTemplate = new RestTemplate();

    /****
     * 通过接口保存到方正数据库
     * @param
     */
    public static void sendExestart(ExecuteState exestate) {
        System.out.println("开始执行outerUrl数据信息：：：：：：：：：：：：：：");
        exestate = new ExecuteState();
        exestate.setMaId(1);
        exestate.setCreateAt(new Date());
        exestate.setStatus("C");
        //接收控制台数据：正式开始
        String url = exeurl + "exprod/start";

        System.out.println("时间：" + DateUtil.formatDateTime(new Date()));
        String psstring = exestate.getMaId() + "," + exestate.getStatus() + "," + DateUtil.formatDateTime(new Date());
        try {
            //restTemplate.postForEntity(url, psstring, String.class);
            ResponseEntity<ExecuteState> responseEntity = restTemplate.postForEntity(url, exestate, ExecuteState.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("结束执行outerUrl数据信息：：：：：：：：：：：：：：");
    }
}
