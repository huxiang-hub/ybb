package com.vim.chatapi.group.controller;

import com.alibaba.fastjson.JSON;
import com.vim.chatapi.utils.QrCode;
import com.vim.tio.TioWsMsgHandler;
import com.vim.tio.WsOnlineContext;
import com.vim.user.entity.ImChatGroup;
import com.vim.user.entity.ImChatGroupUser;
import com.vim.user.entity.ImUser;
import com.vim.user.service.IImChatGroupService;
import com.vim.user.service.IImChatGroupUserService;
import com.vim.user.service.IImUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;

import javax.websocket.server.PathParam;
import java.util.*;

/**
 * @author by SUMMER
 * @date 2020/3/27.
 */
@RestController
@RequestMapping("/api/group")
public class ChatGroupController {

    private static Logger log = LoggerFactory.getLogger(TioWsMsgHandler.class);

    //群聊
    @Autowired
    private IImChatGroupService groupService;
    //群成员
    @Autowired
    private IImChatGroupUserService groupUserService;
    //用户
    @Autowired
    private IImUserService userService;

    /**
     * 创建群聊
     *
     * @param userIds
     * @return
     */
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public R createGroup(String[] userIds) {
        Date currDate = new Date();
        ImChatGroup chatGroup = new ImChatGroup();
        chatGroup.setMaster(userIds[0]);  //第一个用户默认为群主
        chatGroup.setCreateBy(userIds[0]);
        //默认值
        chatGroup.setName("群聊");
        chatGroup.setAvatar("https://aecpm.alicdn.com/simba/img/TB183NQapLM8KJjSZFBSutJHVXa.jpg");
        chatGroup.setUpdateBy(userIds[0]);
        chatGroup.setCreateDate(currDate);
        if (!groupService.save(chatGroup)) {
            return R.fail("创建群聊失败");
        }
        //在群里添加成员
        ImChatGroupUser groupUser = new ImChatGroupUser();
        for (int i = 0, length = userIds.length; i < length; i++) {
            groupUser.setChatGroupId(chatGroup.getId());
            groupUser.setUserId(userIds[i]);
            groupUser.setTopFlag(0);    //是否置顶，默认不置顶
            groupUser.setCreateDate(currDate);
            if (!groupUserService.save(groupUser)) {
                R.fail("添加群成员失败======");
            }
        }
        List<ImUser> groupUsers = userService.getChatUserList(chatGroup.getId());
        //创建群聊需要重新绑定群中所有用户的信息。
        for (ImUser imUser : groupUsers) {
            ChannelContext channelContext = WsOnlineContext.getChannelContextByUser(imUser.getId());
            //判断对方是否在线。在线才绑定
            if (channelContext != null && !channelContext.isClosed) {
                Tio.bindUser(channelContext, imUser.getId());
                // 在线用户绑定到上下文 用于发送在线消息
                WsOnlineContext.bindUser(imUser.getId(), channelContext);
                //绑定群组
                Tio.bindGroup(channelContext, chatGroup.getId());
                //log.info("============印聊WebSocket连接成功============");
            }
        }
        Map<String, Object> result = new HashMap<>();
        //群信息
        result.put("chatGroup", chatGroup);
        //群成员
        result.put("groupUsers", groupUsers);
        return R.data(result);
    }

    /**
     * 查看群聊基本信息
     */
    @GetMapping("/getGroupInfo")
    public R getGroupInfo(String chatGroupId) {
        ImChatGroup chatGroup = groupService.getById(chatGroupId);
        List<ImUser> groupUsers = userService.getChatUserList(chatGroupId);
        Map<String, Object> result = new HashMap<>();
        //群信息
        result.put("chatGroup", chatGroup);
        //群成员
        result.put("groupUsers", groupUsers);
        return R.data(result);
    }

    /**
     * 更新群聊信息
     *
     * @param chatGroup
     * @return
     */
    @PostMapping("/updateGroupInfo")
    public R updateGroup(@RequestBody ImChatGroup chatGroup) {
        if (!groupService.updateById(chatGroup)) {
            return R.fail("更新群信息失败");
        }
        return R.data(chatGroup);
    }

    /**
     * 添加群聊人员信息
     *
     * @param chatGroupId
     * @param userIds
     * @return
     */
    @PostMapping("/addGroupUser")
    @Transactional(rollbackFor = Exception.class)
    public R addGroupUser(@PathParam("chatGroupId") String chatGroupId,
                          @PathParam("userIds") String[] userIds) {
        ImChatGroupUser groupUser = new ImChatGroupUser();
        Date currDate = new Date();
        for (int i = 0, length = userIds.length; i < length; i++) {
            groupUser.setChatGroupId(chatGroupId);
            groupUser.setUserId(userIds[i]);
            groupUser.setCreateDate(currDate);
            if (!groupUserService.save(groupUser)) {
                R.fail("添加群成员失败======");
            }
        }
        List<ImUser> userList = userService.getChatUserList(chatGroupId);
        return R.data(userList);
    }

    /**
     * 删除群聊人员信息
     *
     * @param chatGroupId
     * @param userIds
     * @return
     */
    @PostMapping("/delGroupUser")
    public R delGroupUser(@PathParam("chatGroupId") String chatGroupId,
                          @PathParam("userIds") String[] userIds) {
        List<ImChatGroupUser> groupUsers = groupUserService.getByGroupId(chatGroupId);
        for (ImChatGroupUser groupUser : groupUsers) {
            for (int i = 0, length = userIds.length; i < length; i++) {
                if (groupUser.getUserId().equals(userIds[i])) {
                    if (!groupUserService.removeById(groupUser)) {
                        return R.fail("移除群成员失败");
                    }
                }
            }
        }
        List<ImUser> userList = userService.getChatUserList(chatGroupId);
        return R.data(userList);
    }

    /**
     * 转让群
     */
    @PostMapping("/tranGroup")
    public R tranGroup(String chatGroupId, String userId) {
        //查询当前的群聊信息
        ImChatGroup group = groupService.getById(chatGroupId);
        //当前的群聊成员。
        List<ImUser> groupUsers = userService.getChatUserList(chatGroupId);
        //判断当前用户是否是群成员
        for (ImUser user : groupUsers) {
            if (userId.equals(user.getId())) {
                group.setMaster(userId);  //更改群主。
                if (!groupService.updateById(group)) {
                    return R.fail("转让群主失败");
                }
            }
        }
        return R.success("转让群主成功");
    }

    /**
     * 解散群
     */
    @PostMapping("/disBandGroup")
    public R disBandGroup(String chatGroupId, String userId) {
        //中间表删除所有群成员
        ImChatGroup chatGroup = groupService.getById(chatGroupId);
        //判断当前用户是否是群主
        if (userId.equals(chatGroup.getMaster())) {
            if (!groupUserService.disBandGroup(chatGroupId)) {
                return R.fail("解散时，解散群聊失败。");
            }
            //删除群
            groupService.removeById(chatGroup);
            return R.success("群聊解散成功");
        }
        return R.fail("只有群主才能解散群聊");
    }

    /**
     * 退出群
     */
    @PostMapping("/dropOutGroup")
    public R dropOutGroup(String chatGroupId, String userId) {
        ImChatGroup group = groupService.getById(chatGroupId);
        if (userId.equals(group.getMaster())) {
            return R.fail("您是群主，需要转让才可以退出群");
        }
        if (!groupUserService.dropOutGroup(chatGroupId, userId)) {
            return R.fail("退出群聊失败");
        }
        return R.success("您已退出群聊");
    }

    /**
     * 获取用户的群聊列表----数量
     */
    @GetMapping("/getGroupByUserId")
    public R getChatGroupByUserId(String userId) {
        //查询用户的群组列表
        List<Map<String, Object>> result = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        List<ImChatGroup> groupList = groupUserService.getChatGroupByUserId(userId);
        for (ImChatGroup chatGroup : groupList) {
            int count = groupUserService.getCount(chatGroup.getId());
            map.put("groupInfo", chatGroup);
            map.put("userCount", count);
        }
        return R.data(result);
    }

    /**
     * 查看群某个成员信息。
     */
    public R getUserInfo(String userId) {
        User user = userService.getUserInfo(userId);
        return R.data(user);
    }

    /**
     * 创建群二维码
     */
    @PostMapping("/groupQrCode")
    public R createGroupQrCode(String chatGroupId) {
        Map<String, Object> map = new HashMap<>();
        ImChatGroup chatGroup = groupService.getById(chatGroupId);
        int count = groupUserService.getCount(chatGroupId);
        map.put("chatGroup", chatGroup);
        map.put("count", count);
        map.put("flag", "yb-group");
        String qrCode = null;
        try {
            qrCode = QrCode.createQrCode(JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.data(qrCode);
    }

    /**
     * 群聊置顶
     */
    @PostMapping("/groupChatOnTop")
    public R groupChatOnTop(@RequestParam("userId") String userId,
                            @RequestParam("groupId") String groupId,
                            @RequestParam("topFlag") Integer topFlag) {
        ImChatGroupUser chatGroup = groupUserService.getGroup(groupId, userId);
        chatGroup.setTopFlag(topFlag);
        if (!groupUserService.updateById(chatGroup)) {
            return R.fail("置顶失败");
        }
        return R.success("置顶成功");
    }
}
