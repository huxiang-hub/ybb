package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.SuperviseRegular;
import com.yb.execute.mapper.SuperviseRegularMapper;
import com.yb.execute.service.SuperviseRegularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuperviseRegularServiceImpl extends ServiceImpl<SuperviseRegularMapper, SuperviseRegular> implements SuperviseRegularService {

    @Autowired
    private  SuperviseRegularMapper superviseRegularMapper;


}
