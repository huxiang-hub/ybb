package com.anaysis.service;

import com.anaysis.sqlservermapper.domain.FzScMachineJobDO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface FzScMachineJobService {
    List<FzScMachineJobDO> getPageList(Map<String, Object> map);

    int count(Map<String, Object> map);

    List<FzScMachineJobDO> getListByIds(String ids);

    List<FzScMachineJobDO> list(Map<String, Object> map);
}
