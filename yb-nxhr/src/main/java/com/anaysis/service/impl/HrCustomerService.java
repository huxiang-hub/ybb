package com.anaysis.service.impl;

import com.anaysis.entity.CrmCustomer;
import com.anaysis.entity.HrCustomer;
import com.anaysis.sqlservermapper.HrCustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:44
 **/
@Service
public class HrCustomerService {
    @Autowired
    private HrCustomerMapper customerMapper;

    public List<HrCustomer> list() {
        return customerMapper.list();
    }

    public List<String> getAllErpIds() {
        return customerMapper.getAllErpIds();
    }

    public List<CrmCustomer> getByErpIds(List<String> erpIds) {
        return customerMapper.getByErpIds(erpIds);
    }
}
