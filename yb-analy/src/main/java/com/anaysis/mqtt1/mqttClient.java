package com.anaysis.mqtt1;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.common.SpringUtil;
import com.anaysis.dynamicData.datasource.DBIdentifier;
import com.anaysis.socket1.MTResolvePacket;
import com.anaysis.yLong.YiLongCollectData;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;

@Component
@Slf4j
public class mqttClient {
    MTResolvePacket mtResolvePacket = (MTResolvePacket) SpringUtil.getBean(MTResolvePacket.class);


    private String baseDataTopic = "dev/v4/up/baseData";
    String[] topic = {"dev/v4/up/baseData", "dev/v4/up/history"};
    private String mqttServerUrl = "tcp://mqtt.hopewellgroup.top:1883";
    private String mqttClientName = "admin";
    private String mqttClientPasswd = "Yabcd123456";
    private String mqttClinetID = null;
    private MemoryPersistence persistence;
    private MqttClient client;
    private MqttConnectOptions connOpts;

    int icount = 0;

    public mqttClient() {
        persistence = new MemoryPersistence();
        // MQTT 连接选项
        connOpts = new MqttConnectOptions();
        connOpts.setUserName(mqttClientName);
        connOpts.setPassword(mqttClientPasswd.toCharArray());
        // 保留会话
        connOpts.setCleanSession(true);
        // 设置心跳
        connOpts.setKeepAliveInterval(60);
    }

    public MqttClient getMqttClient() {
        return this.client;
    }

    public void connectServer() {
        try {
            mqttClinetID = UUID.randomUUID().toString();
            client = new MqttClient(mqttServerUrl, mqttClinetID, persistence);
            // 设置回调
            client.setCallback(new OnMessageCallback());
            // 建立连接
            client.connect(connOpts);
            // 订阅
            client.subscribe(topic, new int[]{2, 2});
        } catch (MqttException me) {
            log.warn("reason " + me.getReasonCode());
            log.warn("reason " + me.getReasonCode());
            log.warn("msg " + me.getMessage());
            log.warn("loc " + me.getLocalizedMessage());
            log.warn("cause " + me.getCause());
            log.warn("excep " + me);
            me.printStackTrace();
        }
    }

    public class OnMessageCallback implements MqttCallback {
        //断开链接会走到这，但会自动重连
        @Override
        public void connectionLost(Throwable cause) {
            System.out.print("client disconnect\n");
            // 连接丢失后，一般在这里面进行重连
            System.out.println("连接断开，可以做重连");
            connectServer();
        }

        // subscribe后，服务器发布的消息会执行到这里面
        @Override
        public void messageArrived(String topic, MqttMessage message) throws ParseException {
            // subscribe后得到的消息会执行到这里面
            System.out.println("接收记录条数 : " + icount++);
            System.out.println("接收消息主题 : " + topic);
            System.out.println("接收消息Qos : " + message.getQos());
            System.out.println("接收消息内容 : " + new String(message.getPayload()));
            String json = new String(message.getPayload());
            YiLongCollectData yiLongCollectData = JSONObject.parseObject(json, YiLongCollectData.class);
            String devGroup = yiLongCollectData.getDevGroup();
            if ("yilong".equals(devGroup) || "yintong".equals(devGroup) || "yutong".equals(devGroup)) {
                if ("yutong".equals(devGroup)) {
                    devGroup = "demo";
                }
                DBIdentifier.setProjectCode(devGroup);

                YiLongCollectData.data data = yiLongCollectData.getData();
                if (data.getType().equals("baseData")) {
                    mtResolvePacket.yiLongResolve_m(yiLongCollectData);
                } else {
                    //历史数据统计,增加regular信息
                    mtResolvePacket.yiLongHistoryData(yiLongCollectData);
                }
            }
        }

        //发布消息成功会走到这里
        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            System.out.println("deliveryComplete---------" + token.isComplete());
        }
    }
}
