package com.anaysis.service.impl;

import com.anaysis.entity.*;
import com.anaysis.mysqlmapper.ProdPdinfoMapper;
import com.anaysis.service.*;
import com.anaysis.sqlservermapper.HrProcessMapper;
import com.anaysis.sqlservermapper.HrProductMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Service
public class ProdPdinfoServiceImpl extends ServiceImpl<ProdPdinfoMapper, ProdPdinfo> implements IProdPdinfoService {

    @Autowired
    private HrProductService hrProductService;
    @Autowired
    private ProdPdinfoMapper prodPdinfoMapper;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdClassifyService prodClassifyService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private HrProcessMapper hrProcessMapper;
    @Autowired
    private HrProductMapper hrProductMapper;


    /**
     * todo 暂时不用
     */
    @Override
    public void syn() {
        List<String> myErpIds = prodPdinfoMapper.getAllErpIds();
        List<String> hrErpIds = hrProductService.getCtnAllErpIds();
        List<String> addErpIds = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<ProdPdinfo> prodPdinfos = hrProductService.getByErpIds(addErpIds);
        for (ProdPdinfo prodPdinfo : prodPdinfos) {
            ProdClassify prodClassify = prodClassifyService.getByErpId(prodPdinfo.getErpId());
            // 添加产品类型
            if (null != prodClassify) {
                prodPdinfo.setPcId(prodClassify.getId());
            } else {
                ProdClassify addProdClassify = hrProductService.getProdClassByErpProdId(prodPdinfo.getErpId());
                prodClassifyService.save(addProdClassify);
                prodPdinfo.setPcId(addProdClassify.getId());
            }
            prodPdinfoMapper.insert(prodPdinfo);
            List<ProcessWorkinfo> processWorkinfos1 = hrProcessMapper.getByErpProId(prodPdinfo.getErpId());

            // 添加产品部件
            List<ProdPartsinfo> prodPartsinfos = hrProductService.getPartsByProdErpId(prodPdinfo.getErpId());
            for (ProdPartsinfo prodPartsinfo : prodPartsinfos) {
                prodPartsinfo.setPdId(prodPdinfo.getId());
                prodPartsinfoService.save(prodPartsinfo);
                // 产品部件工序关联
                List<String> processErpIds = hrProductService.getProcessByProd(prodPartsinfo.getErpId());
                List<ProcessWorkinfo> processWorkinfos = processWorkinfoService.getByErpIds(processErpIds);
                for (ProcessWorkinfo processWorkinfo : processWorkinfos) {
                    ProdProcelink prodProcelink = new ProdProcelink();
                    prodProcelink.setPtId(prodPartsinfo.getId());
                    prodProcelink.setPrId(processWorkinfo.getId());
                    prodProcelinkService.save(prodProcelink);
                }
            }
        }
    }

    /**
     * 同步增量产品
     */
    @Override
    public void sync() {
        Map<String, List<String>> notImport = this.notImport();
        List<String> ctn = notImport.get("ctn");
        List<String> off = notImport.get("off");
        List<String> sheet = notImport.get("sheet");
        List<ProdPdinfo> list = new ArrayList<>();
        if (ctn.size() > 0) {
            List<ProdPdinfo> ctnProdPdinfos = hrProductMapper.getProdCtnByErpIds(ctn);
            list.addAll(ctnProdPdinfos);
        }
        if (off.size() > 0) {
            List<ProdPdinfo> offProdPdinfos = hrProductMapper.getProdOffByErpIds(off);
            list.addAll(offProdPdinfos);
        }
        if (sheet.size() > 0) {
            List<ProdPdinfo> sheetProdPdinfos = hrProductMapper.getProdSheetByErpIds(sheet);
            list.addAll(sheetProdPdinfos);
        }
        for (ProdPdinfo prodPdinfo : list) {
            this.save(prodPdinfo);
            ProdPartsinfo prodPartsinfo = new ProdPartsinfo();
            prodPartsinfo.setPdId(prodPdinfo.getId());
            prodPartsinfo.setPtName(prodPdinfo.getPdName());
            prodPartsinfo.setPtNo(prodPdinfo.getPdNo());
            prodPartsinfo.setPtType(prodPdinfo.getPtType());
            prodPartsinfo.setPtClassify(2);
            prodPartsinfo.setPdType(1);
            prodPartsinfo.setErpId(prodPartsinfo.getErpId());
            prodPartsinfoService.save(prodPartsinfo);
            saveProdProc(prodPdinfo, prodPartsinfo.getId());
        }

    }

    @Override
    public ProdPdinfo getByErpId(String erpId) {
        return prodPdinfoMapper.getByErpId(erpId);
    }

    public Map<String, List<String>> notImport() {
        Map<String, List<String>> map = new HashMap<>();
        List<String> myErpIds = prodPdinfoMapper.getAllErpIds();
        List<String> hrCtnErpIds = hrProductService.getCtnAllErpIds();
        List<String> hrOffErpIds = hrProductService.getOffsetAllErpIds();
        List<String> hrSheetErpIds = hrProductService.getSheetBoardAllErpIds();
        List<String> notImportCtn = hrCtnErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<String> notImportOff = hrOffErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        List<String> notImportSheet = hrSheetErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        map.put("ctn", notImportCtn);
        map.put("off", notImportOff);
        map.put("sheet", notImportSheet);
        return map;
    }

    /**
     * 保存产品工序关联
     */
    public void saveProdProc(ProdPdinfo pdinfo, Integer ptId) {
        List<HrBomProc> bomProcs = hrProductMapper.selectProductProcess(pdinfo.getPdNo());
        List<ProcessWorkinfo> processWorkinfos = processWorkinfoService.list();
        Map<String, Integer> prIdMap = processWorkinfos.stream().collect(Collectors.toMap(ProcessWorkinfo::getPrName, ProcessWorkinfo::getId, (k1, k2) -> k2));
        for (HrBomProc bomProc : bomProcs) {
            ProdProcelink prodProcelink = new ProdProcelink();
                prodProcelink.setPdId(pdinfo.getId());
                prodProcelink.setPtId(ptId);
                prodProcelink.setPrId(prIdMap.get(bomProc.getProcessCode()));
                prodProcelink.setSortNum(bomProc.getProcessNo());
                prodProcelink.setIsUsed(1);
                prodProcelinkService.save(prodProcelink);
        }
    }
}