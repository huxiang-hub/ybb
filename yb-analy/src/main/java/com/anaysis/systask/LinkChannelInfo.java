package com.anaysis.systask;

import lombok.Data;

/****
 * 客户机台管理的停机监控功能
 */
@Data
public class LinkChannelInfo {

    //private ChannelHandlerContext channelHandlerContext;//客户端交互连接对象
    private String ctGuid;//客户端唯一标识
    private Long lastTime;//客户端交互的最后通讯时间
//    private String lastStatus;//最后的状态
//    private Long diffTime;//差异时间
}
