package com.anaysis.socket1;

import com.anaysis.systask.OnlineInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MTTCPS implements OnlineInfo {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    public static final Integer PORT = 8882;//  8890(宝锋8802)黑色自研盒子
    /**
     * 存储client的channel
     * key:ip，value:Channel
     */
    public static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
    private static Map<String, byte[]> messageMap = new ConcurrentHashMap<String, byte[]>();

    @Override
    public void run() {
        try {

            ServerBootstrap a = new ServerBootstrap();
            a.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MTNTServerHandler());
//            //绑定端口
            ChannelFuture ch = a.bind(PORT).sync();
            System.out.println("MTTCPS监听服务打开端口号：OPEN:" + PORT);

            ChannelFuture f = ch.channel().closeFuture().sync();
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    /**
     * @return the messageMap
     */
    public static Map<String, byte[]> getMessageMap() {
        return messageMap;
    }

    /**
     * @param messageMap the messageMap to set
     */
//    public static void setMessageMap(Map<String, byte[]> messageMap) {
//        MTTCPS.messageMap = messageMap;
//    }
}
