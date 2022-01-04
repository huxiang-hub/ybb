package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.MaterMtinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-30
 */

@Mapper
public interface MaterMtinfoMapper extends BaseMapper<MaterMtinfo> {

    @Select("select erp_id from yb_mater_mtinfo")
    List<String> getAllErpIds();
}
