package com.anaysis.sqlservermapper;

import com.anaysis.sqlservermapper.domain.FzScMachineJobDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface FzScMachineJobDao {
    List<FzScMachineJobDO> getPageList(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<FzScMachineJobDO> getListByIds(String ids);

    List<FzScMachineJobDO> list(Map<String, Object> map);
}
