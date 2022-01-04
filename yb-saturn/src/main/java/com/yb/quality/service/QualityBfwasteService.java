package com.yb.quality.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.quality.entity.QualityBfwaste;
import com.yb.quality.vo.QualityBfVO;
import com.yb.quality.vo.QualityBfwasteVO;

import java.util.List;

public interface QualityBfwasteService extends IService<QualityBfwaste> {

    /**
     * 查询机台排程单列表数据
     * @param maId
     * @param wsId
     * @param targetDay
     * @return
     */
    List<QualityBfwasteVO> qualityBfwasteList(Integer maId, Integer wsId, String targetDay);

    /**
     * 查询机台自检数据
     * @param exId 排程单id
     * @return
     */
    IPage<QualityBfVO> qualityBfVOList(IPage<QualityBfVO> iPage, Integer exId);
}
