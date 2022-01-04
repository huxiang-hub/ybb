package com.anaysis.service.impl;

import com.anaysis.entity.FzScMachineJobDO;
import com.anaysis.fz.mapper.FzScMachineJobMapper;
import com.anaysis.service.FzScMachineJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FzScMachineJobServiceImpl implements FzScMachineJobService {
    @Autowired
    FzScMachineJobMapper fzscmachinejobdao;

    @Override
    public List<FzScMachineJobDO> getPageList(Map<String, Object> map) {
//        Map<String, Object> querymap = new HashMap<>();
//        querymap.put("machineId", machineId);
//        querymap.put("iGzzxId", iGzzxIds);
        return fzscmachinejobdao.getPageList(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return fzscmachinejobdao.count(map);
    }

    @Override
    public List<FzScMachineJobDO> getListByIds(String ids){
        return fzscmachinejobdao.getListByIds(ids);
    }

    @Override
    public List<FzScMachineJobDO> list(Map<String, Object> map){
        return fzscmachinejobdao.list(map);
    }
}
