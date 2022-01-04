package com.anaysis.mysqlmapper;


import com.anaysis.entity.ProdPartsinfo;
import com.anaysis.entity.ProdPdinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品分类信息
 */
public interface ProdPartsinfoMapper extends BaseMapper<ProdPartsinfo> {

//    部件名称与产品id 查询 部件id与编号
    List<ProdPartsinfo> byPdIdAndPtName(@Param("ptName") String ptName, @Param("pdId") Integer pdId);
    Integer byPId(Integer pId);

    ProdPartsinfo getByerpId(String erpId);
}
