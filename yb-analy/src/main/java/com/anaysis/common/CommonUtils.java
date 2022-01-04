package com.anaysis.common;

import io.netty.channel.ChannelHandlerContext;

public class CommonUtils {
    /**
     * 获取client对象：ip+port
     *
     * @param ctx
     * @return
     */
    public static String getRemoteAddress(ChannelHandlerContext ctx) {
        String socketString = "";
        socketString = ctx.channel().remoteAddress().toString();
        return socketString;
    }
    /**
     * 获取client的ip
     *
     * @param ctx
     * @return
     */
    public static String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }
}
