package com.anaysis.fz.mapper;

import com.anaysis.executSupervise.entity.SuperviseInterval;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 设备清零日志表 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseIntervalMapper extends BaseMapper<SuperviseInterval> {

    SuperviseInterval getPreviousLog(@Param("maId") Integer maId);
}
