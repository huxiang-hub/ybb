package com.anaysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.anaysis.entity.ProdProcelink;

import java.util.List;

/**
* @author lzb
* @date 2020-11-26
*/

public interface IProdProcelinkService extends IService<ProdProcelink> {

    List<ProdProcelink> getByPtId(String ptId);
}
