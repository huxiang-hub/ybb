package org.springblade.system.feign;

import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author by SUMMER
 * @date 2020/3/2
 */
@Component
public class CheckClientFallBack implements ICheckClient {
    @Override
    public R exeStatus(@RequestParam("maId") Integer maId, @RequestParam("uuid") String uuid) {
        return R.fail("服务调用失败");
    }
}
