package com.yb.mater.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.mater.entity.MaterInstore;
import com.yb.mater.mapper.MaterInstoreMapper;
import com.yb.mater.service.MaterInstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MaterInstoreServiceImpl extends ServiceImpl<MaterInstoreMapper, MaterInstore> implements MaterInstoreService {

    @Autowired
    private MaterInstoreMapper materInstoreMapper;


}
