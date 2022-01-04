package com.yb.workbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.workbatch.entity.SuperviseExestop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author my
 * @date 2020-06-16
 * Description: 当前停机/接单 缓存表_yb_supervise_exestop Mapper
 */
@Mapper
public interface SuperviseExestopMapper extends BaseMapper<SuperviseExestop> {

    /**
     * 根据uuid获取停机缓存信息
     * @param uuid
     * @return
     */
    SuperviseExestop getByUuid(@Param("uuid") String uuid);

    SuperviseExestop stopInfo(Integer maId);


}