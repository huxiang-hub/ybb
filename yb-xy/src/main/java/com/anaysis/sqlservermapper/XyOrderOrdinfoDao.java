package com.anaysis.sqlservermapper;

import com.anaysis.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface XyOrderOrdinfoDao {
//    订单集合
    List<OrderOrdinfo> list();
//    订单编号查询订单
    OrderOrdinfoVO listByOdNo(@Param("odNo") String odNo);
//    查询产品分类
    List<ProdClassify> getProdClassify();
//    订单查产品
    ProdPdinfoVO getProdPdinfo(@Param("pdCode") String pdCode);
//    产品作为部件
    ProdPartsinfoVO getProdPdinfoVO(@Param("pdId") Integer pdId);
//    产品查部件
    List<ProdPartsinfoVO> getProdPartsinfo(@Param("erpId") String erpId);

    //    查部件
    ProdPartsinfoVO getOneProdPartsinfo(@Param("erpId") String erpId, @Param("partErp") String partErp);

//    部件查物料
    List<MaterProdlinkVO> getMaterProdlinkByAPComp(@Param("erpId")String erpId);
//    部件查辅料
    List<MaterProdlinkVO> getMaterProdlinkByAPCompFL(@Param("erpId")String erpId);
//    查询所有物料
    List<MaterMtinfoVO> getMaterMtinfo();
    //    查询所有物料类型
    List<MaterClassfiy> getMaterClassify();
//    部件查工序
    List<ProdProcelinkVO> getProdProcelink(@Param("erpId") String erpId);
//    查询工单（批次）
    OrderWorkbatchVO getOrderWorkbatch(String wbNo);
//    排产关联物料
    List<MaterBatchlink> getMaterBatchlink(String erpId);
//      产品作为部件的
    Integer getPdw(String wbNo);
    //      产品作为部件的
    Integer getPdy(String wbNo);

    //    产品查物料
    List<MaterProdlinkVO> getMaterProdlinkByPd(String wbNo);
    //    产品查辅料
    List<MaterProdlinkVO> getMaterProdlinkByPdFL(String wbNo);

    // 产品查未完成工序
    List<ProdProcelinkVO> getlinkW(@Param("wbNo") String wbNo);
    // 产品查完成工序
    List<ProdProcelinkVO> getlinkY(@Param("wbNo") String wbNo);

    //查询部件
    ProdPartsinfoVO getPartBySCDHBJID(String SCDHBJID);

    //查询最终交期
    String getFinalTime(@Param("DDBH") String DDBH, @Param("SCDBH") String SCDBH, @Param("KHDDH") String KHDDH);
}
