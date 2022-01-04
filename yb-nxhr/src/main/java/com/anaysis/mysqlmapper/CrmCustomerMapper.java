package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.CrmCustomer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Mapper
public interface CrmCustomerMapper extends BaseMapper<CrmCustomer> {

    @Select("select erp_id from yb_crm_customer where erp_id is not null")
    List<String> getAllErp();

    @Select("select * from yb_crm_customer where erp_id = #{erpId}")
    CrmCustomer getByErpId(String erpId);
}
