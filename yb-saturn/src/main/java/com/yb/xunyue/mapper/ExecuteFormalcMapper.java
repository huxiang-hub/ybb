package com.yb.xunyue.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.xunyue.entity.ExecuteFormalc;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper 接口
 *
 * @author BladeX
 * @since 2021-03-30
 */
public interface ExecuteFormalcMapper extends BaseMapper<ExecuteFormalc> {

    int getCount(@Param("exId") Integer exId);

}
