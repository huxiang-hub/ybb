package com.anaysis.command;

import com.anaysis.common.HexUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/21 9:16
 */
public class Command {

    public static Map<String, ChannelHandlerContext> map = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> channelHashMap = new ConcurrentHashMap();

    //DI1做正反纸检测 发送数据
    public static String DI1 = "0110000100010200016641";
    //DI1返回数据
    public static String DI1_BACK = "01 10 00 01 00 01 50 09";

    //DI2做正反纸检测 发送数据
    public static String DI2 = "0110000100010200022640";

    //DI2返回数据
    public static String DI2_BACK = "01 10 00 01 00 01 50 09";

    //DI3做正反纸检测 发送数据
    public static String DI3 = "011000010001020003E780";

    //DI3返回数据
    public static String DI3_BACK = "01 10 00 01 00 01 50 09";

    //DI4做正反纸检测 发送数据
    public static String DI4 = "011000010001020004A642";

    //DI4返回数据
    public static String DI4_BACK = "01 10 00 01 00 01 50 09";

}
