package com.yb.socketio;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/8/31 9:32
 */
@Data
public class SocketClient {


    /**
     *  客户端ID 同用户ID
     */
    private String clientId;

    /**
     * 客户端的SessionID
     */
    private UUID sessionId;

    /**
     * 客户端SessionID 大部分有意义的二进制数字
     */
    private long mostSignificantBits;

    /**
     *  客户端SessionID 少部分有意义的二进制数字
     */
    private long leastSignificantBits;

    /**
     * 最后连接时间
     */
    private Date lastConnectedTime;
}
