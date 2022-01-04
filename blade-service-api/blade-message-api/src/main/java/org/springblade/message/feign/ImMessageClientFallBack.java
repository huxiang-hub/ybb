package org.springblade.message.feign;

import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by SUMMER
 * @date 2020/4/3.
 */
@Component
public class ImMessageClientFallBack implements ImMessageClient {

    @Override
    public R sendMsgToUser(@RequestParam("userId") String userId,
                           @RequestParam("msg") String msg,
                           @RequestParam("type") String type) throws Exception {
        return R.fail("服务暂时不可用");
    }

    @Override
    public R sendMsgToMac(@RequestParam("maId") String maId,
                          @RequestParam("msg") String msg,
                          @RequestParam("type") String type) throws Exception {
        return R.fail("服务暂时不可用");
    }
}
