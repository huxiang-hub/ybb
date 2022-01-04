package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.SuperviseExestop;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author my
 * @date 2020-06-16
 * Description: 当前停机表_yb_supervise_exestop Mapper
 */
@Mapper
public interface SuperviseExestopMapper extends BaseMapper<SuperviseExestop> {

    /**
     * 根据uuid获取停机缓存信息
     * @param uuid
     * @return
     */
    SuperviseExestop getByUuid(@Param("uuid") String uuid);
}