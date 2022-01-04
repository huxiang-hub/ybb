package com.vim.chatapi.utils;

import com.tencentyun.TLSSigAPIv2;

public class IMSig {

    private static long sdkAppid=1400396088;

    private static String secretkey="db43991244f1e14984839360b9b1b36b3189259fb0d812cae14be5739069ff2f";

    private static long expire = 60*60*24*7;

    public static String generateUserSig(String userId) {
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(sdkAppid,secretkey);
        return tlsSigAPIv2.genSig(userId,expire);
    }
}
