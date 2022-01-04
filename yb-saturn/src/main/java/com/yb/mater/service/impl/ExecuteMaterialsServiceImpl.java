package com.yb.mater.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.mater.entity.ExecuteMaterials;
import com.yb.mater.mapper.ExecuteMaterialsMapper;
import com.yb.mater.service.IExecuteMaterialsService;
import com.yb.mater.vo.ExecuteMaterialsVO;
import com.yb.mater.vo.HrMaterial;
import com.yb.synchrodata.service.impl.NxhrMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lzb
 * @date 2021-01-10
 */

@Service
public class ExecuteMaterialsServiceImpl extends ServiceImpl<ExecuteMaterialsMapper, ExecuteMaterials> implements IExecuteMaterialsService {

    @Autowired
    private NxhrMaterialService nxhrMaterialService;
    @Autowired
    private ExecuteMaterialsMapper executeMaterialsMapper;

    @Override
    public ExecuteMaterials savaByBarCode(String barCode, Integer maId, Integer sfId, Integer wfId, Integer usId) {
        HrMaterial hrMaterial = nxhrMaterialService.selectByBarCode(barCode);
        if (null == hrMaterial) {
            return null;
        }
        ExecuteMaterials executeMaterials = new ExecuteMaterials();
        List<ExecuteMaterials> list = this.list(Wrappers.<ExecuteMaterials>lambdaQuery().eq(ExecuteMaterials::getBarCode, barCode));
        if (list.size() > 0) {
            return null;
        }
        executeMaterials.setMatName(hrMaterial.getDescription());
        executeMaterials.setMatNum(Integer.parseInt(hrMaterial.getStockMeter().setScale(0, BigDecimal.ROUND_UP).toString()));
        executeMaterials.setMaId(maId);
        executeMaterials.setWsId(sfId);
        executeMaterials.setWfId(wfId);
        executeMaterials.setUsId(usId);
        executeMaterials.setBarCode(barCode);
        save(executeMaterials);
        return executeMaterials;
    }

    @Override
    public List<ExecuteMaterialsVO> getListByWfId(Integer wfId) {
        return executeMaterialsMapper.getListByWfId(wfId);
    }

    @Override
    public int getBarCodeByExists(String barCode) {
        return executeMaterialsMapper.getBarCodeByExists(barCode);
    }
}