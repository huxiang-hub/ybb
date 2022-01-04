package com.anaysis.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lzb
 * @Date 2020/11/11 15:06
 **/

@FeignClient(value = "yb-saturn",path = "/satapi/DdSendController/InternalH5")
public interface DingClient {
    @GetMapping(value = "asyncsend_v2")
    void asyncsend_v2(@RequestParam("msg") String msg, @RequestParam("userId") String userId, @RequestParam("apUnique") String apUnique);
    @GetMapping(value = "send")
    void send(@RequestParam("msg") String msg, @RequestParam("apUnique") String apUnique);
}
