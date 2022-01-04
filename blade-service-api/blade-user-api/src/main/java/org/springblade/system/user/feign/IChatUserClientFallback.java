package org.springblade.system.user.feign;

import org.springblade.core.tool.api.R;
import org.springblade.system.user.entity.User;

public class IChatUserClientFallback implements  IChatUserClient {

     @Override
    public R loginByPhoneCode(String phoneNum) {
        return R.data("未获取到注册信息");
    }

    @Override
    public R getPhoneCode(User user) {
        return R.fail("注册失败！");
    }
}
