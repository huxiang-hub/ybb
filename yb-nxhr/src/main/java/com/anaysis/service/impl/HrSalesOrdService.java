package com.anaysis.service.impl;

import com.anaysis.entity.HrSalesOrd;
import com.anaysis.sqlservermapper.HrSalesOrdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:49
 **/
@Service
public class HrSalesOrdService {
    @Autowired
    private HrSalesOrdMapper salesOrdMapper;

    public List<HrSalesOrd> list() {
        return salesOrdMapper.list();
    }
}
