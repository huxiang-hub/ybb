package org.springblade.message.feign;

import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by SUMMER
 * @date 2020/4/3.
 */
@FeignClient(value = "yb-vim",
        fallback = ImMessageClientFallBack.class)
public interface ImMessageClient {

    @PostMapping("/api/notice/pushToUser")
    R sendMsgToUser(@RequestParam("userId") String userId,
                    @RequestParam("msg") String msg,
                    @RequestParam("type") String type) throws Exception;


    @PostMapping("/api/notice/pushToMac")
    R sendMsgToMac(@RequestParam("maId") String maId,
                   @RequestParam("msg") String msg,
                   @RequestParam("type") String type) throws Exception;
}
