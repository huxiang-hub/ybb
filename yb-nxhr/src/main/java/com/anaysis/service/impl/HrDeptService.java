package com.anaysis.service.impl;

import com.anaysis.entity.BaseDeptinfo;
import com.anaysis.sqlservermapper.HrDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:46
 **/
@Service
public class HrDeptService {
    @Autowired
    private HrDeptMapper deptMapper;

    public List<BaseDeptinfo> list() {
        return deptMapper.list();
    }

    public List<String> getAllErpId() {
        return deptMapper.getAllErpId();
    }

    public BaseDeptinfo getByErpId(String dpErp) {
        return deptMapper.getByErpId(dpErp);
    }

    public List<BaseDeptinfo> getByErpIds(List<String> addErpIds) {
        return deptMapper.getByErpIds(addErpIds);
    }

    public List<BaseDeptinfo> getAll() {
        return deptMapper.getAll();
    }
}
