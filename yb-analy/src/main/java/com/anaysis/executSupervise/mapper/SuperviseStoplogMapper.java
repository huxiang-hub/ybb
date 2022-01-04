package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.SuperviseStoplog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @author my
 * @date 2020-06-16
 * Description: 停机记录_yb_supervise_stoplog，小停机记录log日志信息，超过X分钟以下记录 Mapper
 */
@Mapper
public interface SuperviseStoplogMapper extends BaseMapper<SuperviseStoplog> {

}