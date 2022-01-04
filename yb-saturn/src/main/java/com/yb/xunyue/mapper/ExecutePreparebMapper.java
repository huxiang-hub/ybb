package com.yb.xunyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.xunyue.entity.ExecutePrepareb;
import org.apache.ibatis.annotations.Param;

/**
 * 生产准备记录_yb_execute_prepareB Mapper 接口
 *
 * @author BladeX
 * @since 2021-03-30
 */
public interface ExecutePreparebMapper extends BaseMapper<ExecutePrepareb> {

    int getCount(@Param("exId") Integer exId);

}
