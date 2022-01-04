package com.anaysis.sqlservermapper;

import com.anaysis.entity.CrmCustomer;
import com.anaysis.entity.HrCustomer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:35
 **/
public interface HrCustomerMapper {

    List<HrCustomer> list();

    @Select("select ObjID from Customer")
    List<String> getAllErpIds();

    List<CrmCustomer> getByErpIds(List<String> erpIds);
}
