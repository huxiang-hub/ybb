package com.vim.chatapi.friend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vim.chatapi.friend.entity.*;
import com.vim.chatapi.user.entity.SaUser;
import com.vim.chatapi.user.service.IChatUserService;
import com.vim.chatapi.utils.HttpClientUtils;
import com.vim.chatapi.utils.IMSig;
import com.vim.chatapi.utils.ObjectMapperUtil;
import com.vim.user.entity.ImUserFriend;
import com.vim.user.service.IImUserFriendService;
import com.vim.user.service.IImUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author by SUMMER
 * @date 2020/3/30.
 */
@RestController
@RequestMapping("/api/friend")
@AllArgsConstructor
public class ChatFriendController {

    private IImUserFriendService friendService;

    @Resource
    @Qualifier(value = "imUserService")
    private IImUserService userService;
    @Autowired
    private IChatUserService chatUserService;


    private Map<String, String> getMapIM(){
        String userId = "administrator";
        String userSig = IMSig.generateUserSig(userId);
        Map<String,String> map = new HashMap<>();
        map.put("sdkappid","1400396088");
        map.put("identifier","administrator");
        map.put("usersig", userSig);
        map.put("random","99999999");
        map.put("contenttype","json");
        return map;
    }

    /**
     * 同意添加好友
     */
    @PostMapping("/addFriend")
    @Transactional(rollbackFor = Exception.class)
    public R addFriend(String userChatNo, String friendChatNo) {
        String IM;
        try {
            IM = IMAddFriend(userChatNo, friendChatNo);//im添加好友
        } catch (Exception e) {
            return R.fail("添加好友失败");
        }

        Integer imResult = getIMResult(IM);
        switch (imResult){
            case 30003: {
                return R.fail("请求的用户帐号不存在");
            }
            case 30010: {
                return R.fail("自己的好友数已达系统上限");
            }
            case 30014: {
                return R.fail("对方的好友数已达系统上限");
            }
            case 30540: {
                return R.fail("请求过于频繁,请稍后再试");
            }
            case 30001: {
                return R.fail("对方已是您好友");
            }
        }
        if(imResult != 0){
            return R.fail("添加好友失败");
        }
        Date currDate = new Date();
        ImUserFriend userFriend = new ImUserFriend();
        userFriend.setUserId(userChatNo);
        userFriend.setFriendId(friendChatNo);
        userFriend.setCreateDate(currDate);
        userFriend.setTopFlag(0);      //默认不置顶。
        //好友是相互的
        ImUserFriend friendUser = new ImUserFriend();
        friendUser.setUserId(friendChatNo);
        friendUser.setFriendId(userChatNo);
        friendUser.setCreateDate(currDate);
        friendUser.setTopFlag(0); //默认不置顶。
        if (!friendService.save(userFriend)) {
            return R.fail("添加好友失败");
        }
        if (!friendService.save(friendUser)) {
            return R.fail("添加好友失败");
        }
        return R.success("同意添加对方为好友");
    }

    /**
     * 删除好友
     *
     * @param userChatNo
     * @param friendChatNo
     * @return
     */
    @PostMapping("/delFriend")
    public R delFriend(String userChatNo, String friendChatNo) {
        String deleteCode;
        try {
            deleteCode = IMDeleteFriend(userChatNo, friendChatNo);
        } catch (Exception e) {
            return R.fail("删除好友失败");
        }
        int imResult = getIMResult(deleteCode);
        switch (imResult){
            case 30003 :{
                return R.fail("请求的用户帐号不存在");
            }
            case 31704 :{
                return R.fail("对方已不是你的好友");
            }
            case 31707 :{
                return R.fail("请求过于频繁，请稍后重试。");
            }
        }
        if(imResult != 0){
            return R.fail("删除好友失败");
        }
        if (!friendService.delFriend(userChatNo, friendChatNo)) {
            return R.fail("删除好友失败");
        }
        return R.success("成功删除好友");
    }

    /**
     * 拒绝添加好友通知
     */
    @PostMapping("/rejectFriend")
    public R rejectFriend(String friendChatNo, String content) {
        return R.success("拒绝添加对方为好友");
    }

    /**
     * 搜索，查询好友。
     *
     * @param search tenantId
     * @return
     */
    @PostMapping("/searchFriend")
    public R searchFriend(String search, String tenantId) {
        SaUser user = friendService.searchFriend(search, tenantId);
        if (user == null) {
            return R.fail("没有找到好友");
        }
        return R.data(user);
    }

    /**
     * 查看好友信息
     *
     * @param friendChatNo
     * @return
     */
    @GetMapping("/getFriendInfo")
    public R getFriendInfo(String friendChatNo) {

        return R.data(userService.getFriendInfo(friendChatNo));
    }


    /**
     * 好友聊天消息置顶
     */
    @PostMapping("/friendChatOnTop")
    public R friendChatOnTop(@RequestParam("userChatNo") String userChatNo,
                             @RequestParam("friendChatNo") String friendChatNo,
                             @RequestParam("topFlag") Integer topFlag) {
        ImUserFriend userFriend = friendService.getFriend(userChatNo, friendChatNo);
        userFriend.setTopFlag(topFlag);
        if (!friendService.updateByPrimary(userFriend)) {
            return R.fail("置顶失败");
        }
        return R.success("置顶成功");
    }

    /**
     * 获取好友列表
     * @param userChatNo
     * @return
     */
    @RequestMapping("/friendList")
    public R friendList(@RequestParam("userChatNo") String userChatNo){
        List<SaUser> saUserList = friendService.friendList(userChatNo);
        return R.data(saUserList);
    }
    /**
     * 申请加好友
     * @param userChatNo
     * @return
     */
    @RequestMapping("/askAddFriend")
    public R askAddFriend(@RequestParam("userChatNo") String userChatNo,
                        @RequestParam("friendChatNo")  String friendChatNo){
        String askAddFriend;
        try {
            askAddFriend = IMAskAddFriend(userChatNo, friendChatNo);
        } catch (Exception e) {
            return R.fail("加好友申请失败");
        }
        AskResult askResult = ObjectMapperUtil.toObject(askAddFriend, AskResult.class);
        int errorCode = askResult.getErrorCode();
        switch (errorCode){
            case 90048: {
                return R.fail("请求的用户帐号不存在");
            }
            case 20004: {
                return R.fail("网络异常，请重试");
            }
        }
        if(errorCode != 0){
            return R.fail("加好友申请失败");
        }

        return R.success("加好友申请成功");
    }


    /**
     * IM加好友
     *
     * @param friendChatNo
     * @return
     */
    public String IMAddFriend(String userChatNo, String friendChatNo) throws Exception {
        Map<String, String> mapIM = getMapIM();
        String url = "https://console.tim.qq.com/v4/sns/friend_add?";
        AddFirend addFirend = new AddFirend();
        addFirend.setFrom_Account(userChatNo);//用户id
        List<AddFriendItem> addFriendItems = new ArrayList<>();
        AddFriendItem addFriendItem = new AddFriendItem();
        addFriendItem.setTo_Account(friendChatNo);//好友id
        addFriendItem.setRemark("");
        addFriendItem.setGroupName("");
        addFriendItem.setAddSource("AddSource_Type_XXXXXXXX");
        addFriendItem.setAddWording("");
        addFriendItems.add(addFriendItem);
        addFirend.setAddType("");
        addFirend.setForceAddFlags(1);
        addFirend.setAddFriendItem(addFriendItems);
        return HttpClientUtils.doPost(url,addFirend,mapIM);
    }
    /**
     * IM删除好友
     *
     * @param friendChatNo
     * @return
     */
    public String IMDeleteFriend(String userChatNo, String friendChatNo) throws Exception {
        Map<String, String> mapIM = getMapIM();
        String url = "https://console.tim.qq.com/v4/sns/friend_delete?";
        List<String> friendList = new ArrayList<>();
        friendList.add(friendChatNo);
        DeleteFirend deleteFirend = new DeleteFirend();
        deleteFirend.setFrom_Account(userChatNo);
        deleteFirend.setTo_Account(friendList);
        deleteFirend.setDeleteType("Delete_Type_Both");
        return HttpClientUtils.doPost(url, deleteFirend, mapIM);
    }
    /**
     * IM请求加好友
     *
     * @param friendChatNo
     * @return
     */
    public String IMAskAddFriend(String userChatNo, String friendChatNo) throws Exception {
        Map<String, String> mapIM = getMapIM();
        String url = "https://console.tim.qq.com/v4/openim/sendmsg?";
        User user = chatUserService.getOne(new QueryWrapper<User>().eq("chat_no", userChatNo));//查询申请人
        String toJSON = ObjectMapperUtil.toJSON(user);//转json串
        MsgContent msgContent = new MsgContent();
        msgContent.setData(toJSON);
        msgContent.setDesc("askAddFriend");
        MsgBody msgBody = new MsgBody();
        msgBody.setMsgContent(msgContent);
        msgBody.setMsgType("TIMCustomElem");//自定义消息
        List<MsgBody> msgBodyList = new ArrayList<>();
        msgBodyList.add(msgBody);
        AskAddFriend askAddFriend = new AskAddFriend();
        askAddFriend.setSyncOtherMachine(1);//1同步
        askAddFriend.setTo_Account(friendChatNo);
        Random random = new Random();
        int nextInt = random.nextInt(1000000);
        askAddFriend.setMsgRandom(nextInt);
        askAddFriend.setMsgLifeTime(604800);//消息保存时间(七天)
        Integer millis = (int)System.currentTimeMillis();
        askAddFriend.setMsgTimeStamp(millis);
        askAddFriend.setMsgBody(msgBodyList);
        return HttpClientUtils.doPost(url, askAddFriend, mapIM);
    }


    private Integer getIMResult(String IMCode){

        IMResult imAddResult = ObjectMapperUtil.toObject(IMCode, IMResult.class);
        System.out.println(imAddResult);
        List<ResultItem> resultItem = imAddResult.getResultItem();
        ResultItem resultItem1 = resultItem.get(0);
        System.out.println(resultItem1);
        return resultItem1.getResultCode();
    }

    /**
     * IM 添加好友demo
     */

    public static void main(String[] args) throws Exception {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("sdkappid","1400396088");
        hashMap.put("identifier","administrator");
        hashMap.put("usersig","eJw1jtEKgjAYRt9l1yG-c9omdBeRYYQsiLqTtuSnpmuOUKN3T7Quv3M48L3JMZeB7iw6TdIEGAdYTOylHUkJDYDMu1X30lpUJA0ZQCQS4Hw2qHTt8YZTUCqDNbbelb5x-xSr0Rw8CHqRiezXw4DNNcxoMURCyE2RPWx*Zr2pds9Tt232q1-o0YyvwlgwwZcspp8v3yA0lg__");
        hashMap.put("random","99999999");
        hashMap.put("contenttype","json");
        String url = "https://console.tim.qq.com/v4/sns/friend_add?";

        AddFirend addFirend = new AddFirend();
        addFirend.setFrom_Account("1");
        List<AddFriendItem> addFriendItems = new ArrayList<>();
        AddFriendItem addFriendItem = new AddFriendItem();
        addFriendItem.setTo_Account("99999");
        addFriendItem.setRemark("");
        addFriendItem.setGroupName("");
        addFriendItem.setAddSource("AddSource_Type_XXXXXXXX");
        addFriendItem.setAddWording("");
        addFriendItems.add(addFriendItem);
        addFirend.setAddType("");
        addFirend.setForceAddFlags(1);
        addFirend.setAddFriendItem(addFriendItems);

        String res =  HttpClientUtils.doPost(url,addFirend,hashMap);
       System.out.println(res);
        IMResult imResult = ObjectMapperUtil.toObject(res, IMResult.class);
        List<ResultItem> resultItem = imResult.getResultItem();
        System.out.println(imResult);
        System.out.println(resultItem.get(0));
    }

}
