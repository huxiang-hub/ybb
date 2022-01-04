package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.CrmCustomer;
/**
* @author lzb
* @date 2020-11-26
*/

public interface ICrmCustomerService extends IService<CrmCustomer> {

    /**
     * 导入客户
     */
    void sync();

    CrmCustomer getByErpId(String cmId);
}
