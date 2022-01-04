package com.yb.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/31 10:25
 */
@Slf4j
public class Client {
    public static void main(String[] args) {
        // 服务端socket.io连接通信地址
        String url = "http://127.0.0.1:8888";
        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            options.reconnectionAttempts = 2;
            // 失败重连的时间间隔
            options.reconnectionDelay = 1000;
            // 连接超时时间(ms)
            options.timeout = 500;
            // userId: 唯一标识 传给服务端存储
            final Socket socket = IO.socket(url + "?maId=1", options);

            socket.on(Socket.EVENT_CONNECT, args1 -> socket.send("hello..."));

            // 自定义事件`connected` -> 接收服务端成功连接消息
           // socket.on(Socket.EVENT_CONNECT, objects -> System.out.println("服务端:" + objects[0].toString()));

//            // 自定义事件`push_data_event` -> 接收服务端消息
            socket.on(Socket.EVENT_MESSAGE, objects -> System.out.println("服务端:" + objects[0].toString()));
//
//            // 自定义事件`myBroadcast` -> 接收服务端广播消息
//            socket.on("myBroadcast", objects -> log.debug("服务端：" + objects[0].toString()));

            socket.connect();

            while (true) {
                Thread.sleep(3000);
                // 自定义事件`push_data_event` -> 向服务端发送消息
                socket.emit(Socket.EVENT_MESSAGE, "客户端发送的数据" );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

