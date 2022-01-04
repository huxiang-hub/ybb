package com.yb.quartz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.quartz.entity.BaseTask;
import com.yb.panelapi.user.utils.R;
import com.yb.quartz.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author my
 */
@Controller
@RequestMapping("/common/job")
@Api(tags = "定时任务管理")
public class JobController extends BladeController {
    @Autowired
    private JobService jobService;

    @GetMapping()
    String taskScheduleJob() {
        return "common/job/job";
    }


    @GetMapping("/add")
    String add() {
        return "common/job/add";
    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long id, Model model) {
        BaseTask job = jobService.get(id);
        model.addAttribute("job", job);
        return "common/job/edit";
    }
    @GetMapping("/page")
    @ApiOperation(value = "分页")
    @ResponseBody
    public R list(BaseTask baseTask, Query query) {
        IPage<BaseTask> pages = jobService.page(Condition.getPage(query), baseTask);
        return R.ok(pages);
    }

    /**
     * 信息
     */
    @GetMapping("/info")
    @ApiOperation(value = "详情")
    public R info(Long id) {
        BaseTask taskScheduleJob = jobService.get(id);
        return R.ok().put("taskScheduleJob", taskScheduleJob);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @ResponseBody
    public R save(BaseTask taskScheduleJob) {
        if (jobService.save(taskScheduleJob) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改")
    @ResponseBody
    public R update(BaseTask taskScheduleJob) {
        jobService.update(taskScheduleJob);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ApiOperation(value = "删除")
    @ResponseBody
    public R remove(Long id) {
        if (jobService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ApiOperation(value = "批量删除")
    @ResponseBody
    public R remove(@RequestParam("ids[]") Long[] ids) {
        jobService.batchRemove(ids);
        return R.ok();
    }

    @PostMapping(value = "/changeJobStatus")
    @ApiOperation(value = "修改任务状态")
    @ResponseBody
    public R changeJobStatus(Long id, String cmd) {
        String label = "停止";
        if ("start".equals(cmd)) {
            label = "启动";
        } else {
            label = "停止";
        }
        try {
            jobService.changeStatus(id, cmd);
            return R.ok("任务" + label + "成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok("任务" + label + "失败");
    }

}
