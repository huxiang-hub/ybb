package com.anaysis.fz.mapper;

import com.anaysis.entity.FzMachineDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FzMachineMapper {
    List<FzMachineDO> list(Map<String, Object> map);
}
