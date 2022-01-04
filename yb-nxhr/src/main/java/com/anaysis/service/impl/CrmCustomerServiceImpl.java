package com.anaysis.service.impl;

import com.anaysis.mysqlmapper.CrmCustomerMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.CrmCustomer;
import com.anaysis.service.ICrmCustomerService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Service
public class CrmCustomerServiceImpl extends ServiceImpl<CrmCustomerMapper, CrmCustomer> implements ICrmCustomerService {

    @Autowired
    private CrmCustomerMapper crmCustomerMapper;
    @Autowired
    private HrCustomerService hrCustomerService;

    /**
     * 同步客户
     */
    @Override
    public void sync() {
        List<CrmCustomer> crmCustomers;
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            crmCustomers = hrCustomerService.getByErpIds(notImport);
            this.saveBatch(crmCustomers);
        }
    }

    @Override
    public CrmCustomer getByErpId(String erpId) {
        return crmCustomerMapper.getByErpId(erpId);
    }

    public List<String> notImport() {
        List<String> myErpIds = crmCustomerMapper.getAllErp();
        List<String> hrErpIds = hrCustomerService.getAllErpIds();
        List<String> notImport = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        return notImport;
    }
}