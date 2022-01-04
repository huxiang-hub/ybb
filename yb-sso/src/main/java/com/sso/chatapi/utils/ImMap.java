package com.sso.chatapi.utils;

import com.sso.auth.service.IuserSigService;
import com.sso.auth.service.imp.IuserSigServiceImp;

import java.util.HashMap;
import java.util.Map;

public class ImMap {

    public static Map getImMap(){
        IuserSigService sigService = new IuserSigServiceImp();
        String result = sigService.generateUserSig("administrator");
            Map<String,String> map = new HashMap<>();
            map.put("sdkappid","1400396088");
            map.put("identifier","administrator");
            map.put("usersig", result);
            map.put("random","99999999");
            map.put("contenttype","json");
            return map;
    }
}
