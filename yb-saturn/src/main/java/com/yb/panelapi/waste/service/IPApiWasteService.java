package com.yb.panelapi.waste.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.panelapi.waste.entity.WasteVo;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
public interface IPApiWasteService extends IService<QualityBfwaste> {

    List<WasteVo> getWaste(String mId);
}
