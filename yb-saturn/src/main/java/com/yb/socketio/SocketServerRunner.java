package com.yb.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Description: SocketIOServer 启动类
 * @Author my
 * @Date Created in 2020/8/31 9:55
 */
@Component
public class SocketServerRunner implements CommandLineRunner {
    private final SocketIOServer server;

    @Autowired
    public SocketServerRunner(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("socketio启动啦！！！！！！！");
        server.start();
    }
}