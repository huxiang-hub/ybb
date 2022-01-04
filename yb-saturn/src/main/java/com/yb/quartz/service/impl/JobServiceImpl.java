package com.yb.quartz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.quartz.entity.BaseTask;
import com.yb.quartz.ScheduleJob;
import com.yb.common.quartz.mapper.BaseTaskMapper;
import com.yb.quartz.QuartzManager;
import com.yb.quartz.QuartzConstant;
import com.yb.quartz.ScheduleJobUtils;
import com.yb.quartz.service.JobService;
import org.quartz.SchedulerException;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定时任务调度信息 服务层
 *
 * @author ruoyi
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private BaseTaskMapper baseTaskMapper;

    @Autowired
    QuartzManager quartzManager;

    @Override
    public BaseTask get(Long id) {
        return baseTaskMapper.get(id);
    }

    @Override
    public List<BaseTask> list(Map<String, Object> map) {
        return baseTaskMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return baseTaskMapper.count(map);
    }

    @Override
    public int save(BaseTask taskScheduleJob) {
        BladeUser user = SecureUtil.getUser();
        if (user != null) {
            taskScheduleJob.setCreateBy(user.getUserId().toString());
        }
        taskScheduleJob.setCreateDate(Date.from(Instant.now()));
        return baseTaskMapper.save(taskScheduleJob);
    }

    @Override
    public int update(BaseTask taskScheduleJob) {
        return baseTaskMapper.update(taskScheduleJob);
    }

    @Override
    public int remove(Long id) {
        try {
            BaseTask scheduleJob = get(id);
            quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            return baseTaskMapper.remove(id);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public int batchRemove(Long[] ids) {
        for (Long id : ids) {
            try {
                BaseTask scheduleJob = get(id);
                quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            } catch (SchedulerException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return baseTaskMapper.batchRemove(ids);
    }

    @Override
    public void initSchedule() throws SchedulerException {
        // 这里获取任务信息数据
        List<BaseTask> jobList = baseTaskMapper.list(new HashMap<String, Object>(16));
        for (BaseTask scheduleJob : jobList) {
            if ("1".equals(scheduleJob.getJobStatus())) {
                ScheduleJob job = ScheduleJobUtils.entityToData(scheduleJob);
                quartzManager.addJob(job);
            }

        }
    }

    @Override
    public void changeStatus(Long jobId, String cmd) throws SchedulerException {
        BaseTask scheduleJob = get(jobId);
        if (scheduleJob == null) {
            return;
        }
        if (QuartzConstant.STATUS_RUNNING_STOP.equals(cmd)) {
            quartzManager.deleteJob(ScheduleJobUtils.entityToData(scheduleJob));
            scheduleJob.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
        } else {
            if (!QuartzConstant.STATUS_RUNNING_START.equals(cmd)) {
            } else {
                scheduleJob.setJobStatus(ScheduleJob.STATUS_RUNNING);
                quartzManager.addJob(ScheduleJobUtils.entityToData(scheduleJob));
            }
        }
        update(scheduleJob);
    }

    @Override
    public void updateCron(Long jobId) throws SchedulerException {
        BaseTask scheduleJob = get(jobId);
        if (scheduleJob == null) {
            return;
        }
        if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
            quartzManager.updateJobCron(ScheduleJobUtils.entityToData(scheduleJob));
        }
        update(scheduleJob);
    }

    @Override
    public IPage<BaseTask> page(IPage<BaseTask> page, BaseTask baseTask) {
        return page.setRecords(baseTaskMapper.page(page, baseTask));
    }

}
