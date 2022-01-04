package com.vim.tio;

import com.vim.api.entity.Message;
import com.vim.api.entity.SendInfo;
import com.vim.message.entity.ImMessage;
import com.vim.message.service.IImMessageService;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImUserService;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.common.tool.ChatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.http.common.HttpResponseStatus;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import org.codehaus.jackson.map.ObjectMapper;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * websocket 处理函数
 *
 * @author 乐天
 * @since 2018-10-08
 */
@Component
public class TioWsMsgHandler implements IWsMsgHandler {

    private static Logger log = LoggerFactory.getLogger(TioWsMsgHandler.class);

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//    @Resource
//    private DefaultTokenServices defaultTokenServices;

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;

    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService iImMessageService;

    //用户
    @Autowired
    private IImUserService userService;


    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     *
     * @param request        request
     * @param httpResponse   httpResponse
     * @param channelContext channelContext
     * @return HttpResponse
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) {
        //String token = request.getParam("token");
        String userId = request.getParam("userId");
        String maId = request.getParam("maId");
        if (userId != null && !"".equals(userId)) {
            try {
                //OAuth2Authentication auth2Authentication = defaultTokenServices.loadAuthentication(token);
                //org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth2Authentication.getUserAuthentication().getPrincipal();
                //user.getUsername()
                //String userId = imUserService.getByLoginName(token).getId();
                //绑定用户
                Tio.bindUser(channelContext, userId);
                // 在线用户绑定到上下文 用于发送在线消息
                WsOnlineContext.bindUser(userId, channelContext);
                //绑定群组
                List<ImChatGroup> groups = imUserService.getChatGroups(userId);
                for (ImChatGroup group : groups) {
                    Tio.bindGroup(channelContext, group.getId());
                }
                log.info("============印聊WebSocket连接成功============");
            } catch (Exception e) {
                e.printStackTrace();
                httpResponse.setStatus(HttpResponseStatus.getHttpStatus(401));
            }
        }
        if (maId != null && !"".equals(maId)) {
            try {
                //绑定用户
                Tio.bindUser(channelContext, maId);
                // 在线用户绑定到上下文 用于发送在线消息
                WsOnlineContext.bindUser(maId, channelContext);
                log.info("============机台WebSocket连接成功============");
            } catch (Exception e) {
                e.printStackTrace();
                httpResponse.setStatus(HttpResponseStatus.getHttpStatus(401));
            }
        }
        return httpResponse;
    }

    /**
     * @param httpRequest    httpRequest
     * @param httpResponse   httpResponse
     * @param channelContext channelContext
     * @throws Exception Exception
     * @author tanyaowu tanyaowu
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /**
     * 字符消息（binaryType = blob）过来后会走这个方法
     *
     * @param wsRequest      wsRequest
     * @param text           text
     * @param channelContext channelContext
     * @return obj
     */
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SendInfo sendInfo = objectMapper.readValue(text, SendInfo.class);
            System.out.println("连接心跳检测=====" + text + "=====" + df.format(new Date()));
            //心跳检测包
            if (ChatType.MSG_PING.getType().equals(sendInfo.getCode())) {
                WsResponse wsResponse = WsResponse.fromText(text, TioServerConfig.CHARSET);
                Tio.send(channelContext, wsResponse);
            }
            //真正的消息
            //所有好友消息 包括通知
            else if (ChatType.MSG_FRIEND.getType().equals(sendInfo.getCode())) {
                Message message = sendInfo.getMessage();
                message.setMine(false);
                WsResponse wsResponse = WsResponse.fromText(objectMapper
                        .writeValueAsString(sendInfo), TioServerConfig.CHARSET);
                //单聊
                SetWithLock<ChannelContext> channelContextSetWithLock =
                        Tio.getChannelContextsByUserid(channelContext.groupContext, message.getId());
                //用户没有登录，存储到离线文件
                if (channelContextSetWithLock == null || channelContextSetWithLock.size() == 0) {
                    saveMessage(message, ChatType.UNREAD.getType());
                } else {
                    Tio.sendToUser(channelContext.groupContext, message.getId(), wsResponse);
                    //入库操作
                    saveMessage(message, ChatType.READED.getType());
                }
            }
            //所有群组消息。
            else if (ChatType.MSG_GROUP.getType().equals(sendInfo.getCode())) {   //群聊
                Message message = sendInfo.getMessage();
                message.setMine(false);
                WsResponse wsResponse = WsResponse.fromText(objectMapper
                        .writeValueAsString(sendInfo), TioServerConfig.CHARSET);

                Tio.sendToGroup(channelContext.groupContext, message.getId(), wsResponse);
                //入库操作

                saveMessage(message, ChatType.READED.getType());
            }
            //准备就绪，需要发送离线消息
            else if (ChatType.MSG_READY.getType().equals(sendInfo.getCode())) {
                //登录的时候发送未读消息
                sendOffLineMessage(channelContext, objectMapper);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        //返回值是要发送给客户端的内容，一般都是返回null
        return null;
    }

    /**
     * 未读消息
     *
     * @param channelContext channelContext，objectMapper objectMapper
     * @throws IOException 抛出异常
     */
    private void sendOffLineMessage(ChannelContext channelContext, ObjectMapper objectMapper) throws IOException {
        List<ImMessage> imMessageList = iImMessageService.getUnReadMessage(channelContext.userid);
        for (ImMessage imMessage : imMessageList) {
            Message message = new Message();
            message.setId(imMessage.getToId());
            message.setMine(false);
            message.setType(imMessage.getType());
            ImUser imUser = imUserService.getByFromId(imMessage.getFromId());
            SendInfo sendInfo = new SendInfo();
            if (imUser != null) {
                //个人消息
                message.setUsername(imUser.getName());
                message.setAvatar(imUser.getAvatar());
                sendInfo.setCode(ChatType.MSG_FRIEND.getType());
            } else {
                //机台消息。
                sendInfo.setCode(ChatType.MSG_MAC.getType());
            }
            message.setCid(String.valueOf(imMessage.getId()));
            message.setContent(imMessage.getContent());
            message.setTimestamp(System.currentTimeMillis());
            message.setFromid(imMessage.getFromId());
            sendInfo.setMessage(message);
            WsResponse wsResponse = WsResponse.fromText(objectMapper
                    .writeValueAsString(sendInfo), TioServerConfig.CHARSET);
            Tio.sendToUser(channelContext.groupContext, message.getId(), wsResponse);
        }
    }

    /**
     * 保存信息
     *
     * @param message    信息
     * @param readStatus 是否已读
     */
    private void saveMessage(Message message, String readStatus) {
        ImMessage imMessage = new ImMessage();
        imMessage.setToId(message.getId());
        imMessage.setFromId(message.getFromid());
        imMessage.setSendTime(System.currentTimeMillis());
        imMessage.setContent(message.getContent());
        imMessage.setReadStatus(readStatus);
        imMessage.setType(message.getType());
        iImMessageService.saveMessage(imMessage);
    }

}
