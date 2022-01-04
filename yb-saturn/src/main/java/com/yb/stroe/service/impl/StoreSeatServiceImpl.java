package com.yb.stroe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.mapper.StoreSeatMapper;
import com.yb.stroe.service.StoreSeatService;
import com.yb.stroe.vo.StoreSeatVO;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreSeatServiceImpl extends ServiceImpl<StoreSeatMapper, StoreSeat> implements StoreSeatService {

    @Autowired
    private StoreSeatMapper storeSeatMapper;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;

    @Override
    public List<StoreSeat> getByAreaId(Integer areaId) {
        QueryWrapper<StoreSeat> wrapper = new QueryWrapper<>();
        if (areaId != null) {
            wrapper.eq("sr_id", areaId);
        }
        return this.list(wrapper);
    }

    @Override
    public List<StoreSeatVO> upStoreSeatList(Integer wfId) {
        Integer upProcessSdId = workbatchOrdlinkMapper.getUpProcessSdIdByWfId(wfId);
        return storeSeatMapper.upStoreSeatList(upProcessSdId);
    }

    @Override
    public List<StoreSeatVO> getStoreSeatByExId(Integer exId) {
        return storeSeatMapper.getStoreSeatByExId(exId);
    }
}
