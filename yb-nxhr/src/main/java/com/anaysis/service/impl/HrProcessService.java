package com.anaysis.service.impl;

import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.sqlservermapper.HrProcessMapper;
import com.anaysis.sqlservermapper.HrProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 18:02
 **/
@Service
public class HrProcessService {
    @Autowired
    private HrProductMapper productMapper;
    @Autowired
    private HrProcessMapper processMapper;
    public List<ProcessWorkinfo> getByErpIds(List<String> erpIds) {
        return productMapper.getByErpIds(erpIds);
    }

    public List<ProcessWorkinfo> getByErpId(Integer proId) {
        return processMapper.getByErpId(proId);
    }
}
