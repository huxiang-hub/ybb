package com.anaysis.socket3;

import com.anaysis.socket1.MTNTServerHandler;
import com.anaysis.systask.OnlineInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IotTCPS implements OnlineInfo {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    //  iot版端口
    public static final Integer PORT = 8883;
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
            System.out.println("IOTTCPS监听服务打开端口号：OPEN:" + PORT);

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

    /***
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
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
