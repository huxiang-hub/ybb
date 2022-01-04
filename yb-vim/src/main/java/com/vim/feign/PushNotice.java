package com.vim.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vim.api.entity.Message;
import com.vim.api.entity.SendInfo;
import com.vim.message.entity.ImMessage;
import com.vim.message.service.IImMessageService;
import com.vim.tio.StartTioRunner;
import com.vim.tio.TioServerConfig;
import com.vim.tio.WsOnlineContext;
import org.springblade.common.tool.ChatType;
import org.springblade.core.tool.api.R;
import org.springblade.message.feign.ImMessageClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.common.WsResponse;

import javax.annotation.Resource;

/**
 * 系统消息推送接口
 *
 * @author by SUMMER
 * @date 2020/4/3.
 */
@RestController
@RequestMapping("/api/notice")
public class PushNotice implements ImMessageClient {

    @Resource
    private StartTioRunner startTioRunner;

    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService messageService;

    /***
     *  系统推送到手机====消息推送接口
     * @param userId   用户id
     * @param msg   消息内容
     * @param type  推送消息类型
     * @throws Exception
     */
    @PostMapping("/pushToUser")
    public R sendMsgToUser(@RequestParam("userId") String userId,
                           @RequestParam("msg") String msg,
                           @RequestParam("type") String type) throws Exception {
        ServerGroupContext serverGroupContext = startTioRunner.getAppStarter()
                .getWsServerStarter().getServerGroupContext();
        ChannelContext cc = WsOnlineContext.getChannelContextByUser(userId);
        SendInfo sendInfo = new SendInfo();
        sendInfo.setCode(ChatType.MSG_MAC.getType());  //系统通知消息
        Message message = new Message();
        message.setId("system");
        message.setFromid(ChatType.MSG_SYSTEM.getType());
        message.setContent(msg);
        message.setMine(false);
        message.setTimestamp(System.currentTimeMillis());
        message.setType(type);
        message.setAvatar("/img/icon.png");
        message.setUsername("系统消息");
        sendInfo.setMessage(message);
        //  对方是否在线
        if (cc != null && !cc.isClosed) {
            WsResponse wsResponse = WsResponse.fromText(
                    new ObjectMapper().writeValueAsString(sendInfo),
                    TioServerConfig.CHARSET);  //TODO 修改
            //WsResponse wsResponse = WsResponse.fromText("", TioServerConfig.CHARSET);
            Tio.sendToUser(serverGroupContext, userId, wsResponse);
            //消息入库
            saveMessage(message, ChatType.READED.getType(), userId);
        } else {
            // 不在线消息入库
            saveMessage(message, ChatType.UNREAD.getType(), userId);
        }
        return R.data(msg);
    }

    /***
     * 系统推送到到机台 =====发送消息给机台
     * @param maId
     * @param msg
     * @param type
     * @return
     * @throws Exception
     */
    @PostMapping("/pushToMac")
    public R sendMsgToMac(@RequestParam("maId") String maId,
                          @RequestParam("msg") String msg,
                          @RequestParam("type") String type) throws Exception {
        ServerGroupContext serverGroupContext = startTioRunner.getAppStarter()
                .getWsServerStarter().getServerGroupContext();
        ChannelContext cc = WsOnlineContext.getChannelContextByUser(maId);
        SendInfo sendInfo = new SendInfo();
        sendInfo.setCode(ChatType.MSG_MAC.getType());  //系统通知消息
        Message message = new Message();
        message.setId("system");
        message.setFromid(ChatType.MSG_SYSTEM.getType());
        message.setContent(msg);
        message.setMine(false);
        message.setTimestamp(System.currentTimeMillis());
        message.setType(type);
        message.setAvatar("/img/icon.png");
        message.setUsername("系统消息");
        sendInfo.setMessage(message);
        //  对方是否在线
        if (cc != null && !cc.isClosed) {
            WsResponse wsResponse = WsResponse.fromText(
                    new ObjectMapper().writeValueAsString(sendInfo),
                    TioServerConfig.CHARSET);  //TODO 修改
            //WsResponse wsResponse = WsResponse.fromText("", TioServerConfig.CHARSET);
            Tio.sendToUser(serverGroupContext, maId, wsResponse);
            // 消息入库
            saveMessage(message, ChatType.READED.getType(), maId);
        } else {
            // 不在线消息入库
            saveMessage(message, ChatType.UNREAD.getType(), maId);
        }
        return R.data(msg);
    }

    /**
     * 保存未读消息
     *
     * @param message
     * @param readStatus
     * @param userId
     */
    private synchronized void saveMessage(Message message, String readStatus, String userId) {
        ImMessage imMessage = new ImMessage();
        imMessage.setToId(userId);
        imMessage.setFromId(message.getFromid());
        imMessage.setSendTime(System.currentTimeMillis());
        imMessage.setContent(message.getContent());
        imMessage.setReadStatus(readStatus);
        imMessage.setType(message.getType());
        messageService.saveMessage(imMessage);
    }
}
