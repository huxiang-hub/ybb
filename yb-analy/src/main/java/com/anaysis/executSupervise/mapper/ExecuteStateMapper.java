package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.ExecuteState;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/18.
 */
@Mapper
public interface ExecuteStateMapper extends BaseMapper<ExecuteState> {

    ExecuteState getStateByMaId(Integer maId);
    int updateState(ExecuteState state);

    List<ExecuteState> getExecutC1(ExecuteState execute);

    ExecuteState getB1Execute(ExecuteState execute);
}
