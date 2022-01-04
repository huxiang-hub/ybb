package org.springblade.system.feign;

import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by SUMMER
 * @date 2020/3/28.
 */
@FeignClient(
        value = "yb-analy",
        fallback = CheckClientFallBack.class
)
public interface ICheckClient {

    @GetMapping("/exprod/start")
    R exeStatus(@RequestParam("maId") Integer maId,@RequestParam("uuid") String uuid);
}
