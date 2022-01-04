package com.anaysis.socket2;

import com.anaysis.systask.OnlineInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TCP服务
 *
 * @author justcode
 */
public class TCPS implements OnlineInfo {

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    public static final int PORT = 8881;// 8899(宝锋8801)白色盒子
    /**
     * 存储client的channel
     * key:ip，value:Channel
     */
    public static Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();
    private static Map<String, byte[]> messageMap = new ConcurrentHashMap<String, byte[]>();

    @Override
    public void run() {
        try {
//            Bootstrap b = new Bootstrap(); // (1)
//            b.group(workerGroup); // (2)
//            b.channel(NioSocketChannel.class); // (3)
//            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TCPServerHandler());
//                }
//            });
            ServerBootstrap a = new ServerBootstrap();
            a.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NTServerHandler());
//            //绑定端口
            ChannelFuture ch = a.bind(PORT).sync();
            System.out.println("TCP监听服务打开端口号：OPEN:" + PORT);

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
    public static void setMessageMap(Map<String, byte[]> messageMap) {
        TCPS.messageMap = messageMap;
    }

    /***
     * 判断是否超时，并且设置数据库盒子离线超时
     * 设定在线的盒子信息
     * @param ctx
     * @param restr
     */
    /*public static void setOnline(ChannelHandlerContext ctx, String restr) {
        String ip = getIPString(ctx);

        //匹配缓存中是否有该盒子的数据信息
        LinkChannelInfo lk = ctmap.get(ip);
        String guid = getBoxInfo(restr);
        //传递的参数信息不能为空，否则返回对应信息；没有获取到uuid信息为无效数据
        if (guid == null)
            return;

        //查询缓存表中，是否有数据接收信息，
        if (lk == null) {
            lk = new LinkChannelInfo();
            lk.setCtGuid(guid);
            lk.setLastTime(System.currentTimeMillis());
            ctmap.put(guid, lk);
        } else {
            //如果没有超时就直接更新最后执行时间信息
            lk.setLastTime(System.currentTimeMillis());
            ctmap.put(guid, lk);

        }
    }*/
}
