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
}
