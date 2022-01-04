package com.anaysis.service.impl;

import com.anaysis.entity.CrmCustomer;
import com.anaysis.entity.ProdPdinfo;
import com.anaysis.mysqlmapper.OrderOrdinfoMapper;
import com.anaysis.mysqlmapper.ProdPdinfoMapper;
import com.anaysis.service.ICrmCustomerService;
import com.anaysis.service.IProdPdinfoService;
import com.anaysis.sqlservermapper.HrSalesOrdMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anaysis.entity.OrderOrdinfo;
import com.anaysis.service.IOrderOrdinfoService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @date 2020-11-27
 */

@Service
public class OrderOrdinfoServiceImpl extends ServiceImpl<OrderOrdinfoMapper, OrderOrdinfo> implements IOrderOrdinfoService {

    @Autowired
    private OrderOrdinfoMapper orderOrdinfoMapper;
    @Autowired
    private HrSalesOrdMapper hrSalesOrdMapper;
    @Autowired
    private IProdPdinfoService prodPdinfoService;
    @Autowired
    private ICrmCustomerService crmCustomerService;

    /**
     * 同步订单（同步订单之前同步产品、客户）
     */
    @Override
    public void sync() {
        List<OrderOrdinfo> orderOrdinfos;
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            orderOrdinfos = hrSalesOrdMapper.getByErpIds(notImport);
        } else {
            return;
        }
        for (OrderOrdinfo orderOrdinfo : orderOrdinfos) {
            ProdPdinfo prodPdinfo = prodPdinfoService.getByErpId(orderOrdinfo.getProdErpId());
            CrmCustomer crmCustomer = crmCustomerService.getByErpId(orderOrdinfo.getCustomerErpId());
            // 订单关联客户、产品
            orderOrdinfo.setCmId(crmCustomer.getId());
            orderOrdinfo.setPdId(prodPdinfo.getId());
            orderOrdinfo.setProductionState(0);
            orderOrdinfo.setAuditStatus(3);
            orderOrdinfoMapper.insert(orderOrdinfo);
        }
    }

    /**
     * 未同步销售订单erpIds
     * @return
     */
    public List<String> notImport() {
        List<String> myErpIds = orderOrdinfoMapper.getAllErpIds();
        List<String> hrErpIds = hrSalesOrdMapper.getAllErpIds();
        List<String> notImport = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        return notImport;
    }
}