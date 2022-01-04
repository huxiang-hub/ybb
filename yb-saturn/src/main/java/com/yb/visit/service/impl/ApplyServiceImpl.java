package com.yb.visit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.visit.entity.Apply;
import com.yb.visit.mapper.ApplyMapper;
import com.yb.visit.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {

    @Autowired
    private ApplyMapper applyMapper;
}
