package com.yb.common.quartz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.common.quartz.entity.BaseTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 定时任务调度信息信息 dao
 *
 * @author my
 */
@Mapper
public interface BaseTaskMapper {
    BaseTask get(Long id);

    List<BaseTask> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(BaseTask task);

    int update(BaseTask task);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<BaseTask> page(IPage page, BaseTask baseTask);

}