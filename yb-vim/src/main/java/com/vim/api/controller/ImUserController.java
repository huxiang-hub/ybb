package com.vim.api.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vim.message.service.IImMessageService;
import com.vim.tio.StartTioRunner;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImUserFriendService;
import com.vim.user.service.IImUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;

/**
 * 前端控制器
 *
 * @author 乐天
 * @since 2018-10-07
 */
@RestController
@RequestMapping("/api/user")
public class ImUserController {

    @Resource
    private StartTioRunner startTioRunner;

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService imUserService;

    @Resource
    @Qualifier(value = "imUserFriendService")
    private IImUserFriendService imUserFriendService;

    @Resource
    @Qualifier(value = "iImMessageService")
    private IImMessageService iImMessageService;

    /**
     * 用户信息初始化
     *
     * @param request request
     * @return json
     */
    @RequestMapping("init")
    public Map<String, Object> list(HttpServletRequest request) {
        Map<String, Object> objectMap = new HashMap<>();

        //获取好友信息
//        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String username ="";
                ImUser user = imUserService.getByLoginName(username);
        objectMap.put("friends", imUserFriendService.getUserFriends(user.getId()));

        //获取本人信息
        String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        QueryWrapper<ImUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name", username);
        user.setAvatar(host + user.getAvatar());
        user.setPassword(null);
        objectMap.put("me", user);

        //用户的群组信息
        objectMap.put("groups", imUserService.getChatGroups(user.getId()));
        return objectMap;
    }


    /**
     * 获取群组的用户
     *
     * @param chatId 群组id
     * @return 用户List
     */
    @RequestMapping("chatUserList")
    public List<ImUser> chatUserList(String chatId) {
        return imUserService.getChatUserList(chatId);
    }

    /**
     * 发送信息给用户
     * 注意：目前仅支持发送给在线用户
     * @param userId  接收方id
     * @param  msg 消息内容
     */
    /*@PostMapping("sendMsg")
    public void sendMsg(String userId, String msg) throws Exception {
        ServerGroupContext serverGroupContext = startTioRunner.getAppStarter().getWsServerStarter().getServerGroupContext();
        ChannelContext cc = WsOnlineContext.getChannelContextByUser(userId);
        SendInfo sendInfo = new SendInfo();
        sendInfo.setCode(ChatType.MSG_USER.getType());
        Message message = new Message();
        message.setId("system");
        message.setFromid("system");
        message.setContent(msg);
        message.setMine(false);
        message.setTimestamp(System.currentTimeMillis());
        message.setType(ChatType.FRIEND.getType());
        message.setAvatar("/img/icon.png");
        message.setUsername("系统消息");
        sendInfo.setMessage(message);
        if(cc!=null && !cc.isClosed){
            //WsResponse wsResponse = WsResponse.fromText(new ObjectMapper().writeValueAsString(sendInfo), TioServerConfig.CHARSET);  //TODO 修改
            WsResponse wsResponse = WsResponse.fromText("", TioServerConfig.CHARSET);
            Tio.sendToUser(serverGroupContext, userId, wsResponse);
        }else {
            saveMessage(message, ChatType.UNREAD.getType(),userId);
        }
    }

    private void saveMessage(Message message, String readStatus,String userId) {
        ImMessage imMessage = new ImMessage();
        imMessage.setToId(userId);
        imMessage.setFromId(message.getFromid());
        imMessage.setSendTime(System.currentTimeMillis());
        imMessage.setContent(message.getContent());
        imMessage.setReadStatus(readStatus);
        imMessage.setType(message.getType());
        iImMessageService.saveMessage(imMessage);
    }*/
}
