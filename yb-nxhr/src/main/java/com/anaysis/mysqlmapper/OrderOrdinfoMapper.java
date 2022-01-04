package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.OrderOrdinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-27
 */

@Mapper
public interface OrderOrdinfoMapper extends BaseMapper<OrderOrdinfo> {

    @Select("select erp_id from yb_order_ordinfo where erp_id !=null")
    List<String> getAllErpIds();
}
