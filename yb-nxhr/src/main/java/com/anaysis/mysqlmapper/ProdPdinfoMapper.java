package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.ProdPdinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Mapper
public interface ProdPdinfoMapper extends BaseMapper<ProdPdinfo> {

    @Select("select erp_id from yb_prod_pdinfo")
    List<String> getAllErpIds();

    @Select("select * from yb_prod_pdinfo where erp_id = #{erpId}")
    ProdPdinfo getByErpId(String erpId);

    @Select("select distinct pd_no from yb_prod_pdinfo")
    List<String> selectPdNoList();

}
