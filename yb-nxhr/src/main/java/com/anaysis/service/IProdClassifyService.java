package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.ProdClassify;
/**
* @author lzb
* @date 2020-11-26
*/

public interface IProdClassifyService extends IService<ProdClassify> {

    ProdClassify getByErpId(String erpId);
}
