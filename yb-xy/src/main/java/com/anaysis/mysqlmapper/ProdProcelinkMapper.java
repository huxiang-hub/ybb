package com.anaysis.mysqlmapper;


import com.anaysis.entity.ProdProcelink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品分类信息
 */
public interface ProdProcelinkMapper extends BaseMapper<ProdProcelink> {
    List<ProdProcelink> selectByPtIdAndPrId(@Param("ptId") Integer ptId , @Param("prId") Integer prId);
}
