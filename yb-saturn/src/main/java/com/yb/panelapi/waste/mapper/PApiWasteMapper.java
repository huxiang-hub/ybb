package com.yb.panelapi.waste.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.panelapi.waste.entity.QualityBfwaste;
import com.yb.panelapi.waste.entity.WasteVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/15.
 */
@Mapper
public interface PApiWasteMapper extends BaseMapper<QualityBfwaste> {

    //获取当前工序的废品种类
    List<WasteVo> getWaste(String mId);
}
