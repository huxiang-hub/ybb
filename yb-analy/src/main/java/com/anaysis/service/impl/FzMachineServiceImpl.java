package com.anaysis.service.impl;

import com.anaysis.entity.FzMachineDO;
import com.anaysis.fz.mapper.FzMachineMapper;
import com.anaysis.service.FzMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FzMachineServiceImpl implements FzMachineService {
    @Autowired
    FzMachineMapper fzmachinedao;

    @Override
    public List<FzMachineDO> list(String iGzzxIds) {
        if (iGzzxIds != null && iGzzxIds.length() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("iGzzxId", iGzzxIds);
            map.put("sort", "a.id");
            map.put("order", "desc");
            return fzmachinedao.list(map);
        }
        return null;
    }
}

