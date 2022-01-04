package com.anaysis.socket2;

import com.anaysis.command.Command;
import com.anaysis.command.CommandUtils;
import com.anaysis.common.CommonUtils;
import com.anaysis.common.HexUtil;
import com.anaysis.common.StringUtils;
import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ChannelHandler.Sharable
public class NTServerHandler extends ChannelInboundHandlerAdapter {

    Map<String, String> cacheread = new HashMap<String, String>();
    // 向客户端发送消息，默认心跳数据
    String response = "";//hello hopewell
    //设定接收信息的实际长度字节数；最大长度
    int receiveLen = 61 * 2;
    //接收半截数据信息，一般最小值信息
    int receiveMinlen = 12;

    //解析类进行初始化处理
    ResolvePacket resolvePacket = new ResolvePacket();
    //执行接口方法（统一方法）
    private static Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf result = (ByteBuf) msg;
        //读取消息包中的数据信息
        String resultStr = HexUtil.bytes2HexString(readbody(result));
        if (StringUtils.isNotBlank(resultStr)) {
            if (resultStr.startsWith("0110")) {
                //是否正确回复 待处理
                log.debug("收到盒子modbus回复[resultStr:{}]", resultStr);
            }
            return;
        }
        //截取有效的数字信息
        String rs = (resultStr != null) ? resultStr.substring(0, result.readableBytes() * 2) : null;

        // 接收并打印客户端的信息
        log.info("接口请求信息::exedata-2::" + CommonUtils.getRemoteAddress(ctx) + "::::msg:" + rs);

        //返回数据信息，如果不全就处理为空，就会跳过处理分析过程
        String ip = getIPString(ctx);//获取ip地址
        String restr = receive(ip, rs);//数据信息进行分析截取，碰到不健全数据进行缓存操作处理。
        //log.info("======获取exedata-2：" + ip + "=============================re-str:" + rs);
        //判断返回正常数据，否则就为null
        if (restr != null && restr.length() > 0) {
            try {
                map.put(getBoxInfo(restr), ctx);
                resolvePacket.resolve_m(restr, CommonUtils.getIPString(ctx));//获取解析内容
            } catch (Exception e) {
                sendClient(ctx, null);
                ctx.flush();
                return;
            }
            // 在当前场景下，发送的数据必须转换成ByteBuf数组；发送数据控制盒子信息内容。--发送控制信息信息
            sendClient(ctx, null);
            //setOnline(ctx, restr);  //判断问题，能够接收到数据不能为离线数据;设置在线数据信息
            ctx.flush();
        }
        // 释放资源，这行很关键
        result.release();
        //System.out.println("Client said:" + DateUtil.refFormatNowDate() + "::" + getRemoteAddress(ctx) + "::::" + restr);
        // System.out.println("cacheread:" + cacheread);
    }

    /****
     * 返回接收信息的包内信息
     * @param result
     * @return
     */
    private byte[] readbody(ByteBuf result) {
        if (result == null)
            return null;
        byte[] body = new byte[result.readableBytes()];
        result.getBytes(0, body);
        return body;
    }


    /****
     * 通过判断接收数据是否小于最小长度，进行数据拼接操作。
     * @param rs
     * @return
     */
    private String receive(String ip, String rs) {
        String tpcach = cacheread.get(ip);//从缓存中取出对应数据信息
        String restr = null;
        //碰到数据出现断层情况，进行数据拼接操作
        if (tpcach != null && tpcach.length() > 0) {//判断缓存信息中有信息，就进行拼接当条数据信息
            rs = tpcach + rs;//拼接接收数据信息
            if (rs.length() < receiveLen) {//判断拼接字符串是否达到接收数据的长度，如果还没有达到继续拼接
                cacheread.put(ip, rs);
            } else {

                restr = matchedBox(rs);//截取有效信息内容
                //长度达到61*2字节数了，清空缓存
                cacheread.put(ip, null);
            }
        } else {
            //没有缓存判断接收数据是否小于12的长度
            if (rs != null && rs.length() < receiveMinlen) { //判断接收的信息小于12的长度信息。标识有半截信息节点内容，需要进行缓存拼接
                cacheread.put(ip, rs);   //小于长度放入缓存字节数
            } else {
                //缓存中没有信息，就正常执行
                restr = matchedBox(rs);
            }
        }
        return restr;
    }

    /***
     * resolvePacket对象中保存盒子的全部信息内容，包括mac地址为key的map对象，进行字符串匹配截取有效信息。过滤掉错误信息
     * @param restr
     * @return
     */
    private String matchedBox(String restr) {
        //遍历传递过来的信息中是否包含已有的设备信息内容，如果包含，就通过字符串进行识别截取
        if (TCPS.ErrdataBoxinfo != null && TCPS.ErrdataBoxinfo.size() > 0) {
            for (Map.Entry<String, SuperviseBoxinfo> entry : TCPS.ErrdataBoxinfo.entrySet()) {
                int bitnum = restr.toUpperCase().indexOf(entry.getKey().toUpperCase());
                //包含key关键字符串，然后进行字符串截取
                if (bitnum >= 0) {
                    restr = (restr != null) ? restr.substring(bitnum, restr.length()) : null;
                    return restr;
                }
            }
        }
        return null;
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
        System.out.println("关闭连接了哦");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 获取client的ip
     *
     * @param ctx
     * @return
     */
    public String getIPString(ChannelHandlerContext ctx) {
        String ipString = "";
        String socketString = ctx.channel().remoteAddress().toString();
        int colonAt = socketString.indexOf(":");
        ipString = socketString.substring(1, colonAt);
        return ipString;
    }

    /****
     * 解析消息信息中的uuid的对应信息
     * @param message
     * @return
     */
    private String getBoxInfo(String message) {
        if (message == null)
            return "";

        char[] message_a = message.toCharArray();
        //设备GUID
        char[] uuid_a = Arrays.copyOfRange(message_a, 0, 24);
        //数据1 状态
//        char[] status_a = Arrays.copyOfRange(message_a, 39, 40);
//        String[] rs = new String[2];
//        rs[1] = resolvePacket.resolve_sta(String.valueOf(status_a));//返回数字状态 1run，2stop，3error
        return String.valueOf(uuid_a);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();  //其实相当于一个connection
        Command.map.put("1", ctx);
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

}