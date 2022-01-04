package com.anaysis.sqlservermapper;

import com.anaysis.entity.CrmCustomer;
import com.anaysis.entity.ProcessWorkinfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XyCrmCustomerDao {
    List<CrmCustomer> list();
}
