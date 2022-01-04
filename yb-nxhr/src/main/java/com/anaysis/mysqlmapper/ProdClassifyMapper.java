package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.ProdClassify;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Mapper
public interface ProdClassifyMapper extends BaseMapper<ProdClassify> {

    @Select("")
    ProdClassify getByErpId(String erpId);
}
