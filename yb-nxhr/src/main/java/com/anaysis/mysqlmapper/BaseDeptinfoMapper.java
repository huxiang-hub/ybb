package com.anaysis.mysqlmapper;

import com.anaysis.entity.BaseDeptinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-25
 */

@Mapper
public interface BaseDeptinfoMapper extends BaseMapper<BaseDeptinfo> {

    @Select("select erp_id from yb_base_deptinfo where erp_id IS NOT null")
    List<String> getErpIds();

    @Select("select ")
    List<BaseDeptinfo> getByIds(List<String> addErpIds);

    @Select("select * from yb_base_deptinfo where erp_id = erpId")
    BaseDeptinfo getByErpId(String erpId);
}
