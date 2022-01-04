package com.yb.staff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.staff.entity.StaffPrivate;
import com.yb.staff.mapper.StaffPrivateMapper;
import com.yb.staff.service.StaffPrivateService;
import org.springframework.stereotype.Service;

@Service
public class StaffPrivateServiceImpl extends ServiceImpl<StaffPrivateMapper, StaffPrivate> implements StaffPrivateService {

    @Override
    public StaffPrivate getPrivateInfo(StaffPrivate usPrivate){
        return baseMapper.getPrivateInfo(usPrivate);
    }
}
