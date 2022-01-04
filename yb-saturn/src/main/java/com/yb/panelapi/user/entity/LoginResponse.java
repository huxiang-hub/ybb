package com.yb.panelapi.user.entity;
import com.yb.system.user.entity.SaUser;

import java.util.concurrent.CountDownLatch;

/**
 * 登录信息承载类
 * 
 * @author 秦博
 *
 */
public class LoginResponse {

    private CountDownLatch latch;

    private SaUser user;

    public void setUser(SaUser user) {
        this.user = user;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public SaUser getUser() {
        return user;
    }
}
