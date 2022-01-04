package com.yb.supervise.service.impl;

import com.yb.supervise.service.ISuperviseStopService;
import com.yb.workbatch.entity.SuperviseExestop;
import com.yb.workbatch.mapper.SuperviseExestopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 停机警告
 * @Author my
 */
@Service
public class SuperviseStopServiceImpl implements ISuperviseStopService {

    @Autowired
    private SuperviseExestopMapper superviseExestopMapper;


    @Override
    public SuperviseExestop stopInfo(Integer maId) {

        SuperviseExestop superviseExestop= superviseExestopMapper.stopInfo(maId);

        return superviseExestop;
    }
}
