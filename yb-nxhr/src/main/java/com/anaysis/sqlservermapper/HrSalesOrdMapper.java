package com.anaysis.sqlservermapper;

import com.anaysis.entity.HrSalesOrd;
import com.anaysis.entity.OrderOrdinfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:37
 **/
public interface HrSalesOrdMapper {
    List<HrSalesOrd> list();

    @Select("select ObjId from SalesOrd where Status = '已核价'")
    List<String> getAllErpIds();

    List<OrderOrdinfo> getByErpIds(List<String> addErpIds);
}
