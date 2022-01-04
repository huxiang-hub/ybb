package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.ProdPdinfo;
/**
* @author lzb
* @date 2020-11-26
*/

public interface IProdPdinfoService extends IService<ProdPdinfo> {

    void syn();

    void sync();

    ProdPdinfo getByErpId(String erpId);
}
