package com.yb.stroe.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.stroe.entity.StoreArea;
import com.yb.stroe.mapper.StoreAreaMapper;
import com.yb.stroe.service.StoreAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreAreaServiceImpl extends ServiceImpl<StoreAreaMapper, StoreArea> implements StoreAreaService {

    @Autowired
    private StoreAreaMapper storeAreaMapper;
    @Override
    public StoreArea selectStoreAreaByStId(Integer stId) {

        return storeAreaMapper.selectStoreAreaByStId(stId);
    }
}
