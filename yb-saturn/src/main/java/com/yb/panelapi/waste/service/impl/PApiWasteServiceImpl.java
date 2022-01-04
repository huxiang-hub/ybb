package com.yb.panelapi.waste.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.panelapi.waste.entity.WasteVo;
import com.yb.panelapi.waste.mapper.PApiWasteMapper;
import com.yb.panelapi.waste.service.IPApiWasteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Service
public class PApiWasteServiceImpl extends ServiceImpl<PApiWasteMapper, QualityBfwaste> implements IPApiWasteService {

    @Autowired
    private PApiWasteMapper apiWasteMapper;

    @Override
    public List<WasteVo> getWaste(String mId) {
        return apiWasteMapper.getWaste(mId);
    }
}
