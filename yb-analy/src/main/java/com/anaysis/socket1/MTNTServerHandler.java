package com.anaysis.socket1;

import com.alibaba.fastjson.JSONObject;
import com.anaysis.command.Command;
import com.anaysis.command.CommandUtils;
import com.anaysis.common.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
@ChannelHandler.Sharable
public class MTNTServerHandler extends ChannelInboundHandlerAdapter {

    //Map<String, String> cacheread = new HashMap<String, String>();
    // 向客户端发送消息，默认心跳数据 hello hopewell
    String response = "";//默认不发送信息hello
    //设定接收信息的实际长度字节数
    int receiveLen = 61 * 2;
    //接收半截数据信息，一般最小值信息
    int receiveMinlen = 12;
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    //解析类进行初始化处理
    MTResolvePacket mtResolvePacket = (MTResolvePacket) SpringUtil.getBean(MTResolvePacket.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf result = (ByteBuf) msg;
        //读取消息包中的数据信息
        String resultStr = HexUtil.bytes2HexString(readbody(result));
//        if (StringUtils.isNotBlank(resultStr)) {
//            if (resultStr.startsWith("0110")) {
//                //是否正确回复 待处理
//                System.out.println("盒子回复正确数据:" + resultStr);
//                log.debug("收到盒子modbus回复[resultStr:{}]", resultStr);
//                result.release();
//                return;
//            }
//        }
        //截取有效的数字信息
        String rs = (resultStr != null) ? resultStr.substring(0, result.readableBytes() * 2) : null;
        rs = HexUtil.hexStringToString(rs);
        System.out.println(rs);
        // 接收并打印客户端的信息
        //log.info("接口请求信息::" + getRemoteAddress(ctx) + "::::msg:" + rs);
        Date currTim = new Date();
        //返回数据信息，如果不全就处理为空，就会跳过处理分析过程
        //String ip = getIPString(ctx);//获取ip地址
        //String restr = receive(ip, rs);//数据信息进行分析截取，碰到不健全数据进行缓存操作处理。
        //log.info("======获取exedata-1：" + CommonUtils.getRemoteAddress(ctx)  + "=============================re-str:" + rs);
        //判断返回正常数据，否则就为null
        if (rs != null && rs.length() > 0) {
            if (rs.indexOf("CollectData") >= 0) {
                String lastStr = (StringUtil.includeBystr(rs, "CollectData") > 1) ? "{\"CollectData\":" + rs.substring(rs.lastIndexOf("{"), rs.length()) : rs;
                //判定接收到定时数据信息
                CollectData coldata = JSONUtils.jsonToBean(lastStr, CollectData.class, "CollectData");
                if (coldata != null) {
                    if (coldata != null && coldata.getMid() != null) {
                        System.out.println(coldata);
                        Command.map.put(coldata.getMid(), ctx);
                        mtResolvePacket.resolve_m(coldata, CommonUtils.getIPString(ctx));
                    }
                }
            }  else if (rs.indexOf("BladeData") >= 0) {
                //换版数据信息
                BladeData bladedb = JSONUtils.jsonToBean(rs, BladeData.class, "BladeData");
                if (bladedb != null && bladedb.getMid() != null) {
                    bladedb.setNtime(currTim);//设定当前时间
                    //mtResolvePacket.saveBladeData(bladedb);//设定换版数据信息记录
                }
            } else if (rs.indexOf("ConmachRs") >= 0) {
                ConmachRs conmachrs = JSONUtils.jsonToBean(rs, ConmachRs.class, "ConmachRs");
                if (conmachrs != null && conmachrs.getUuid() != null) {
                    conmachrs.setCreateAt(currTim);//设定当前时间
                    //mtResolvePacket.saveConmachRs(conmachrs);//设定换版数据信息记录
                }
            }

            //----------------------------------------------------------------------------//
            // 在当前场景下，发送的数据必须转换成ByteBuf数组；发送数据控制盒子信息内容。
            if (response != null && response.length() > 0) {
                sendClient(ctx, response);
            }
            //isOffonline(ctx, restr);

            //ctx.flush();
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
        ctx.writeAndFlush(encoded);
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

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();  //其实相当于一个connection
        Command.map.put("1", ctx);
        System.out.println("建立连接on");
    }

    /**
     * 断开连接后用该方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        CommandUtils.removeClose(Command.map, ctx);

    }

    //连接处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + " 下线了");
    }

    /**==========================================================================================================***/

    /**
     * 获取client对象：ip+port
     *
     * @param ctx
     * @return
     */
//    public String getRemoteAddress(ChannelHandlerContext ctx) {
//        String socketString = "";
//        socketString = ctx.channel().remoteAddress().toString();
//        return socketString;
//    }

}