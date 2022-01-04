package com.anaysis.command;

import com.anaysis.common.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

import static com.anaysis.command.Command.DI1;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/20 20:30
 */
public class CommandUtils {

    public static void removeClose(Map map, ChannelHandlerContext ctx) {
        StringBuilder builder = new StringBuilder();
        if (!map.isEmpty()) {
            map.forEach((k, v) -> {
                if (v.equals(ctx)) {
                    builder.append(k);
                }
            });
        }
        if (StringUtils.isNotBlank(builder.toString())) {
            map.remove(builder.toString());
        }
    }

    /***
     *  // 向客户端发送消息
     * @param ctx
     */
    public static  void sendClient(ChannelHandlerContext ctx, String msg) {
        // 在当前场景下，发送的数据必须转换成ByteBuf数组
        ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());
        encoded.writeBytes(HexUtil.hexStringToBytes(msg));
        ctx.writeAndFlush(encoded);
    }
}
