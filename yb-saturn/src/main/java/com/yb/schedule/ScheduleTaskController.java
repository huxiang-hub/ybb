package com.yb.schedule;

import io.swagger.annotations.Api;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by SUMMER
 * @date 2020/3/16.
 */
@RestController
@Api(description = "定时任务")
@RequestMapping("/task")
public class ScheduleTaskController {

    @GetMapping("/startCron")
    public R startCron() {
        return R.success("成功");
    }
}
