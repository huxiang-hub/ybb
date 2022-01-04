package com.anaysis.fz.mapper;

import com.anaysis.entity.FzScMachineJobDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FzScMachineJobMapper {
    List<FzScMachineJobDO> getPageList(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<FzScMachineJobDO> getListByIds(String ids);

    List<FzScMachineJobDO> list(Map<String, Object> map);
}
