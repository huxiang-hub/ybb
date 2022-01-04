package com.yb.quartz.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.quartz.entity.BaseTask;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

/**
 * 定时任务调度信息信息 服务层
 *
 * @author my
 */
public interface JobService {

    BaseTask get(Long id);

    List<BaseTask> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(BaseTask taskScheduleJob);

    int update(BaseTask taskScheduleJob);

    int remove(Long id);

    int batchRemove(Long[] ids);

    void initSchedule() throws SchedulerException;

    void changeStatus(Long jobId, String cmd) throws SchedulerException;

    void updateCron(Long jobId) throws SchedulerException;

    IPage<BaseTask> page(IPage<BaseTask> page, BaseTask baseTask);
}
