package com.yb.mater.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.mater.entity.ExecuteMaterials;
import com.yb.mater.vo.ExecuteMaterialsVO;

import java.util.List;

/**
* @author lzb
* @date 2021-01-10
*/

public interface IExecuteMaterialsService extends IService<ExecuteMaterials> {

    ExecuteMaterials savaByBarCode(String barCode, Integer maId, Integer sfId, Integer wfId, Integer usId);

    List<ExecuteMaterialsVO> getListByWfId(Integer wfId);

    int getBarCodeByExists(String barCode);
}
