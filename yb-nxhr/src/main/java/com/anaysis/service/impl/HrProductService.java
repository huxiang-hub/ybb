package com.anaysis.service.impl;

import com.anaysis.entity.HrProduct;
import com.anaysis.entity.ProdClassify;
import com.anaysis.entity.ProdPartsinfo;
import com.anaysis.entity.ProdPdinfo;
import com.anaysis.sqlservermapper.HrProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:47
 **/
@Service
public class HrProductService {
    @Autowired
    private HrProductMapper productMapper;

    public List<HrProduct> list() {
        return productMapper.list();
    }

    public List<String> getCtnAllErpIds() {
        return productMapper.getCtnAllErpIds();
    }

    public List<String> getOffsetAllErpIds() {
        return productMapper.getOffsetAllErpIds();
    }

    public List<String> getSheetBoardAllErpIds() {
        return productMapper.getSheetBoardAllErpIds();
    }

    public List<ProdPdinfo> getByErpIds(List<String> erpIds) {
        return productMapper.getProdByErpIds(erpIds);
    }

    public ProdClassify getProdClassByErpProdId(String erpId) {
        return productMapper.getProdClassByErpProdId(erpId);
    }

    public List<ProdPartsinfo> getPartsByProdErpId(String erpId) {
        return productMapper.getPartsByProdErpId(erpId);
    }

    public List<String> getProcessByProd(String erpId) {
        return productMapper.getProcessByProd(erpId);
    }
}
