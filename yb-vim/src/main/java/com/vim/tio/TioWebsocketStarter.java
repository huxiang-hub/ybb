package com.vim.tio;

import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

/**
 * TIO 配置文件
 *
 * @author 乐天
 * @since 2018-04-10
 */
public class TioWebsocketStarter {

    private WsServerStarter wsServerStarter;
    private ServerGroupContext serverGroupContext;


    /**
     * @author tanyaowu
     */
    public TioWebsocketStarter(int port, TioWsMsgHandler wsMsgHandler) throws Exception {
        wsServerStarter = new WsServerStarter(port, wsMsgHandler);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setName(TioServerConfig.PROTOCOL_NAME);
        serverGroupContext.setServerAioListener(ServerAioListener.me);

        //设置ip监控
        serverGroupContext.setIpStatListener(IpStatListener.me);
        //设置ip统计时间段
        serverGroupContext.ipStats.addDurations(TioServerConfig.IpStatDuration.IPSTAT_DURATIONS);

        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(TioServerConfig.HEARTBEAT_TIMEOUT);
        //如果你希望通过wss来访问，就加上下面的代码吧，不过首先你得有SSL证书（证书必须和域名相匹配，否则可能访问不了ssl）
//		String keyStoreFile = "classpath:config/ssl/keystore.jks";
//		String trustStoreFile = "classpath:config/ssl/keystore.jks";
//		String keyStorePwd = "214323428310224";
//		serverGroupContext.useSsl(keyStoreFile, trustStoreFile, keyStorePwd);

    }

    public WsServerStarter getWsServerStarter() {
        return wsServerStarter;
    }


}
