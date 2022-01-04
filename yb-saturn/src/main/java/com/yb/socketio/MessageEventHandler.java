package com.yb.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.sun.nio.sctp.MessageInfo;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static javax.servlet.http.HttpServletResponse.SC_OK;

/**
 * @Description: 消息事件处理类 ******
 * @Author my
 * @Date Created in 2020/8/31 9:36
 */
@Component
@Slf4j
public class MessageEventHandler implements Const {

    private static SocketIOServer server;

    /**
     * 存放已连接的客户端
     */
    public static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        MessageEventHandler.server = server;
    }

    /**
     * connect事件处理，当客户端发起连接时将调用
     *
     * @param client
     */
    @OnConnect
    public void onConnect(SocketIOClient client) throws Exception {
        Map<String, List<String>> urlParams = client.getHandshakeData().getUrlParams();
        List<String> list = urlParams.get("uuid");
        if (!list.isEmpty()) {
            clientMap.put(list.get(0), client);
            client.sendEvent(Socket.EVENT_CONNECT, "hello！！！！");
        }
        System.out.println("连接上了");
    }


    /**
     * 关闭连接时调用
     *
     * @param client
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String uuid = getParamsByClient(client);
        if (StringUtils.isNotBlank(uuid)) {
            clientMap.remove(uuid);
        }
        System.out.println("关闭");
    }

    /**
     * 消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
     *
     * @param client
     * @param message 客户端发送的消息
     * @throws Exception
     */
    @OnEvent(value = Socket.EVENT_MESSAGE)
    public void onEvent(SocketIOClient client, AckRequest request, String message) throws Exception {
        System.out.println(message);
        //todo 逻辑处理
        // 给当前客户端推送消息内容
        client.sendEvent(Socket.EVENT_MESSAGE, "ohyeah");
        //消息处理成功，给客户端回执
//        if (request.isAckRequested()) {
//            request.sendAckData(new JSONObject().element(STATUS, SC_OK).element(MSG, "发送成功")
//                    .element(DATA, EMPTY));
//        }
    }

    @OnEvent("alarm")
    public void alarm(SocketIOClient client, String message) throws Exception {
        client.sendEvent("alarm", "警告警告");
    }

    /**
     * 获取唯一标识
     *
     * @param client
     * @return
     */
    private String getParamsByClient(SocketIOClient client) {
        // 从请求的连接中拿出参数（这里的loginUserNum必须是唯一标识）
        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
        List<String> list = params.get("uuid");
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}