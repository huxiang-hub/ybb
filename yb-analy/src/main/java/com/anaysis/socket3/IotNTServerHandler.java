package com.anaysis.socket3;

import com.anaysis.common.CommonUtils;
import com.anaysis.common.HexUtil;
import com.anaysis.common.JSONUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * iot版盒子上行数据
 */
@Slf4j
@ChannelHandler.Sharable
public class IotNTServerHandler extends ChannelInboundHandlerAdapter {

    // 向客户端发送消息，默认心跳数据
    String response = "hello hopewell";
    //设定接收信息的实际长度字节数
    int receiveLen = 61 * 2;
    //接收半截数据信息，一般最小值信息
    int receiveMinlen = 12;


    //解析类进行初始化处理
    IotResolvePacket iotResolvePacket = new IotResolvePacket();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        //读取消息包中的数据信息
        String resultStr = HexUtil.bytes2HexString(readbody(result));

        //截取有效的数字信息
        String rs = (resultStr != null) ? resultStr.substring(0, result.readableBytes() * 2) : null;
        rs = HexUtil.hexStringToString(rs);
        if (rs.length() < receiveMinlen) {
            return;
        }
        System.out.println(rs);
        int params = rs.lastIndexOf("params");
        if (params <= 0) {
            log.info("解析数据格式异常:[uuid:{}]");
            return;
        }
        //开始拼接json
        StringBuilder builder = new StringBuilder();
        String substring = rs.substring(params - 1, rs.length());
        builder.append("{").append(substring);
        System.out.println(builder.toString());
        rs = builder.toString();
        Date currTim = new Date();
        if (rs != null && rs.length() > 0) {
            try {
                //判定接收到定时数据信息
                IotCollectData coldata = JSONUtils.jsonToBean(rs, IotCollectData.class, "params");
                System.out.println(coldata);
                if (coldata != null && coldata.getMid() != null) {
                    //System.out.println("======collect============collect-id：" + coldata);
                    iotResolvePacket.resolve_m(coldata, CommonUtils.getIPString(ctx));
                }
                sendClient(ctx, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ctx.flush();
        }
        // 释放资源，这行很关键
        result.release();
    }

    /****
     * 返回接收信息的包内信息
     * @param result
     * @return
     */
    private byte[] readbody(ByteBuf result) {
        if (result == null) {
            return null;
        }
        byte[] body = new byte[result.readableBytes()];
        result.getBytes(0, body);
        return body;
    }


    /***
     *  // 向客户端发送消息
     * @param ctx
     */
    private void sendClient(ChannelHandlerContext ctx, String msg) {
        String sendstr = (msg != null && msg.length() > 0) ? msg : response;
        // 在当前场景下，发送的数据必须转换成ByteBuf数组
        ByteBuf encoded = ctx.alloc().buffer(4 * sendstr.length());
        encoded.writeBytes(sendstr.getBytes());
        ctx.write(encoded);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
