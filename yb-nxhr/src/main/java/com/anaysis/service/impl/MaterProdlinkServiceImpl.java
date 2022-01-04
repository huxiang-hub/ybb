package com.anaysis.service.impl;

import com.anaysis.entity.*;
import com.anaysis.mysqlmapper.MaterProdlinkMapper;
import com.anaysis.service.*;
import com.anaysis.sqlservermapper.HrMaterMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-30
 */

@Service
public class MaterProdlinkServiceImpl extends ServiceImpl<MaterProdlinkMapper, MaterProdlink> implements IMaterProdlinkService {

    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IMaterMtinfoService materMtinfoService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private HrMaterMapper hrMaterMapper;


    @Override
    public void syn() {
        List<ProdPartsinfo> list = prodPartsinfoService.list();
        for (ProdPartsinfo prodPartsinfo : list) { // 所有产品部件
            String ptIds = prodPartsinfo.getPtIds();
            String[] ids = ptIds.split("\\|"); // 产品的部件ids
            for (String ptId : ids) {
                List<ProdProcelink> prodProcelinks = prodProcelinkService.getByPtId(ptId); // 部件的工序
                for (ProdProcelink prodProcelink : prodProcelinks) {
                    ProcessWorkinfo processWorkinfo = processWorkinfoService.getById(prodProcelink.getPrId());
                    MaterMtinfo materMtinfo = hrMaterMapper.getByProcessId(processWorkinfo.getErpId());
                    MaterMtinfo myMaterMtinfo = materMtinfoService.getByErpId(materMtinfo.getErpId());// 工序的物料
                    MaterProdlink materProdlink = new MaterProdlink();
                    materProdlink.setPdId(prodPartsinfo.getId());
                    materProdlink.setMlId(myMaterMtinfo.getId());
                    materProdlink.setPrId(prodProcelink.getId());
                    materProdlink.setPtId(Integer.valueOf(ptId));
                    this.save(materProdlink);
                }
            }
        }
    }
}