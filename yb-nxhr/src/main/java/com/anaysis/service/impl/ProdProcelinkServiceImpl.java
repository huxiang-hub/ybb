package com.anaysis.service.impl;

import com.anaysis.mysqlmapper.ProdProcelinkMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.ProdProcelink;
import com.anaysis.service.IProdProcelinkService;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Service
public class ProdProcelinkServiceImpl extends ServiceImpl<ProdProcelinkMapper, ProdProcelink> implements IProdProcelinkService {

    @Autowired
    private ProdProcelinkMapper prodProcelinkMapper;

    @Override
    public List<ProdProcelink> getByPtId(String ptId) {
        return prodProcelinkMapper.getByPtId(ptId);
    }
}