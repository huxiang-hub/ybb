package com.yb.exeset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.exeset.entity.ExesetFaultwaste;
import com.yb.exeset.mapper.ExesetFaultwasteMapper;
import com.yb.exeset.service.IExesetFaultwasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IExesetFaultwasteServiceImpl extends ServiceImpl<ExesetFaultwasteMapper,ExesetFaultwaste>
        implements IExesetFaultwasteService {
    @Autowired
    private ExesetFaultwasteMapper exesetFaultwasteMapper;
    @Override
    public List<ExesetFaultwaste> getExesetFaultwaste(Integer maId) {

        return exesetFaultwasteMapper.getExesetFaultwaste(maId);
    }

    @Override
    public boolean setFaultwaste(ExesetFaultwaste exesetFaultwaste) {

        return exesetFaultwasteMapper.setFaultwaste(exesetFaultwaste);
    }


}
