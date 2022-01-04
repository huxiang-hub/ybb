package com.vim.chatapi.utils;

import com.vim.chatapi.friend.entity.IMUpdateUser;
import com.vim.chatapi.friend.entity.ProfileItem;
import org.springblade.core.tool.utils.StringUtil;
import org.springblade.system.user.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IMUpdateUserUtil {

    public static String updateIM(User user) throws IOException {

        String userId = "administrator";
        String userSig = IMSig.generateUserSig(userId);
        Map<String,String> map = new HashMap<>();
        map.put("sdkappid","1400396088");
        map.put("identifier","administrator");
        map.put("usersig", userSig);
        map.put("random","99999999");
        map.put("contenttype","json");
        String url = "https://console.tim.qq.com/v4/profile/portrait_set?";

        List<ProfileItem> profileItemList = new ArrayList<>();
        String name = user.getName();
        if(!StringUtil.isEmpty(name)){
            ProfileItem profileItem1 = new ProfileItem();
            profileItem1.setTag("Tag_Profile_IM_Nick");
            profileItem1.setValue(name);//修改用户呢称
            profileItemList.add(profileItem1);
        }

        /*ProfileItem profileItem2 = new ProfileItem();
        profileItem2.setTag("");
        profileItem2.setValue(userVO.getPhone());//用户电话*/
        String avatar = user.getAvatar();
        if(!StringUtil.isEmpty(avatar)){
            ProfileItem profileItem3 = new ProfileItem();
            profileItem3.setTag("Tag_Profile_IM_Image");
            profileItem3.setValue(avatar);//头像
            profileItemList.add(profileItem3);
        }

        Integer sex = user.getSex();
        if(sex == 0 || sex == 1){
            ProfileItem profileItem4 = new ProfileItem();
            String se = sex != 0 ? "男" : "女";
            profileItem4.setTag("Tag_Profile_IM_Gender");
            profileItem4.setValue(se);//性别
            profileItemList.add(profileItem4);
        }
        String curraddr = user.getCurraddr();
        if(!StringUtil.isEmpty(curraddr)){
            ProfileItem profileItem5 = new ProfileItem();
            profileItem5.setTag("Tag_Profile_IM_Location");
            profileItem5.setValue(curraddr);//地址
            profileItemList.add(profileItem5);
        }
//        profileItemList.add(profileItem2);
        IMUpdateUser iMUpdateUser = new IMUpdateUser();
        iMUpdateUser.setFrom_Account(user.getChatNo().toString());
        iMUpdateUser.setProfileItem(profileItemList);
        return HttpClientUtils.doPost(url,iMUpdateUser,map);
    }
}
