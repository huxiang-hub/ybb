package com.anaysis.sqlservermapper;

import com.anaysis.entity.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2020/11/25 10:36
 **/
public interface HrProductMapper {
    List<HrProduct> list();

    @Select("select")
    List<ProcessWorkinfo> getByErpIds(List<String> erpIds);

    @Select("SELECT ObjID FROM Product WHERE BOM != 'CTN' AND ProductType = 'CTN'")
    List<String> getCtnAllErpIds();

    @Select("SELECT ObjID FROM Product WHERE BOM != 'CTN' AND ProductType = 'OFFSET'")
    List<String> getOffsetAllErpIds();

    @Select("SELECT ObjID FROM Product WHERE  BOM != 'CTN' AND ProductType = 'SheetBoard'")
    List<String> getSheetBoardAllErpIds();

    @Select("SELECT ObjID FROM BomHead")
    List<String> getBomHeadErpIds();

    List<ProdPdinfo> getProdByErpIds(List<String> erpIds);

    @Select("")
    ProdClassify getProdClassByErpProdId(String erpId);

    @Select("")
    List<ProdPartsinfo> getPartsByProdErpId(String erpId);

    @Select("")
    List<String> getProcessByProd(String erpId);

    List<ProdPdinfo> getProdCtnByErpIds(List<String> ctn);

    List<ProdPdinfo> getProdOffByErpIds(List<String> off);

    List<ProdPdinfo> getProdSheetByErpIds(List<String> sheet);

    List<ProdPdinfo> getProdBomHeadByErpIds(List<String> head);

    @Select("SELECT b.ProcessCode as processCode,a.ProcessNo as processNo,a.BOM as bom,a.Machine as machine FROM BomProc a LEFT JOIN MACHINES b ON a.Machine = b.MachCode WHERE a.BOM = #{productNo}")
    List<HrBomProc> selectProductProcess(String productNo);
}
