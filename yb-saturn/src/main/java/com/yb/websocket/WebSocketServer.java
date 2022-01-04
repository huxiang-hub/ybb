package com.yb.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author by SUMMER
 * @date 2020/3/14.
 */

@Component
@ServerEndpoint(value = "/webSocket/{userId}", configurator = WebSocketEndpointConfigure.class)
public class WebSocketServer {
    //ConcurrentHashMap用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<String, Session>();
    private Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        webSocketSet.put(userId, session);
        //相关业务处理，根据拿到的用户ID判断其为那种角色，根据角色ID去查询是否有需要推送给该角色的消息，有则推送
        List<String> totalPushMsgs = new ArrayList<String>();
        totalPushMsgs.add("欢迎您:" + userId);
        if (totalPushMsgs != null && !totalPushMsgs.isEmpty()) {
            totalPushMsgs.forEach(e -> sendMessage(userId, e));
        }
    }

    public void sendMessage(String userId, String message) {
        try {
            Session currentSession = webSocketSet.get(userId);
            if (currentSession != null) {
                currentSession.getBasicRemote().sendText(message);
            }
            log.info("推送消息成功，消息为：" + message);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 自定义消息
     */
    public static void sendInfo(String userId, String message) throws IOException {
        Session currentSession = webSocketSet.get(userId);
        if (currentSession != null) {
            currentSession.getBasicRemote().sendText(message);
        }
    }

    /**
     * 用户退出时，连接关闭调用的方法
     */
    public static void onCloseConection(String userId) {
        webSocketSet.remove(userId); // 从set中删除
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("一个客户端关闭连接");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("websocket出现错误");
    }
}
