package com.anaysis.sqlservermapper;

import com.anaysis.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FzOrderOrdinfoDao {
//    订单集合
    List<OrderOrdinfo> list();
//    订单编号查询订单
    OrderOrdinfo listByOdNo(@Param("odNo") String odNo);
//    查询产品分类
    List<ProdClassify> getProdClassify();
//    订单查产品
    ProdPdinfo getProdPdinfo(@Param("pdId") Integer pdId);
//    产品作为部件
    ProdPartsinfoVO getProdPdinfoVO(@Param("pdId") Integer pdId);
//    产品查部件
    List<ProdPartsinfoVO> getProdPartsinfo(@Param("pdId") Integer pdId);
//    部件查物料
    List<MaterProdlink> getMaterProdlinkByAPComp(@Param("IDAPComp") Integer IDAPComp, @Param("pdId")Integer pdId);
//    查询所有物料
    List<MaterMtinfo> getMaterMtinfo(@Param("pdId")Integer pdId);
//    部件查工序
    List<ProdProcelink> getProdProcelink(@Param("IDComp") Integer IDComp);
}
