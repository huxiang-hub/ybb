package com.anaysis.service.impl;

import com.anaysis.entity.BoxInfoEntity;
import com.anaysis.mysqlmapper.BoxInfoDAO;
import com.anaysis.service.BoxInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BoxInfoServiceImpl implements BoxInfoService {
    @Resource
    private BoxInfoDAO boxInfoMapper;

    @Override
    public List<BoxInfoEntity> getList() {
        return null;
    }
}
