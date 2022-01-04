package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.SuperviseQuality;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author my
 * @date 2020-07-07
 * Description:  Mapper
 */
@Mapper
public interface SuperviseQualityMapper extends BaseMapper<SuperviseQuality> {

    /**
     * 根据uuid获取当前质检缓存
     * @param uuid
     * @return
     */
    SuperviseQuality getByUuid(String uuid);
}