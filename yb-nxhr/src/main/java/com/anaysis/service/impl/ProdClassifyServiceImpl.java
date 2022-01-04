package com.anaysis.service.impl;

import com.anaysis.mysqlmapper.ProdClassifyMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.ProdClassify;
import com.anaysis.service.IProdClassifyService;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Service
public class ProdClassifyServiceImpl extends ServiceImpl<ProdClassifyMapper, ProdClassify> implements IProdClassifyService {

    @Autowired
    private ProdClassifyMapper prodClassifyMapper;

    @Override
    public ProdClassify getByErpId(String erpId) {
        return prodClassifyMapper.getByErpId(erpId);
    }

}