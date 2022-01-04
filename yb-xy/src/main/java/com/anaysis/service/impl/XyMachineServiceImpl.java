package com.anaysis.service.impl;

import com.anaysis.service.XyMachineService;
import com.anaysis.sqlservermapper.XyMachineDao;
import com.anaysis.sqlservermapper.domain.XyMachineDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XyMachineServiceImpl implements XyMachineService {
    @Autowired
    XyMachineDao xymachinedao;

    @Override
    public List<XyMachineDO> list(String iGzzxIds) {
        if (iGzzxIds != null && iGzzxIds.length() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("iGzzxId", iGzzxIds);
            map.put("sort", "a.id");
            map.put("order", "desc");
            return xymachinedao.list(map);
        }
        return null;
    }
}

