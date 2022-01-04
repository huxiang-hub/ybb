package com.sso.auth.service.imp;

import com.sso.auth.service.IuserSigService;
import com.tencentyun.TLSSigAPIv2;
import org.springframework.stereotype.Service;

@Service
public class IuserSigServiceImp implements IuserSigService {



    private long sdkAppid=1400396088;

    private String secretkey="db43991244f1e14984839360b9b1b36b3189259fb0d812cae14be5739069ff2f";

    private long expire = 60*60*24*7;

    @Override
    public String generateUserSig(String userId) {
        TLSSigAPIv2 tlsSigAPIv2 = new TLSSigAPIv2(sdkAppid,secretkey);
        return tlsSigAPIv2.genSig(userId,expire);
    }
}



