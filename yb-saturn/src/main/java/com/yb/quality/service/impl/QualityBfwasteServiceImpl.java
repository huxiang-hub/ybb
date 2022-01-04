package com.yb.quality.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.quality.entity.QualityBfwaste;
import com.yb.quality.mapper.QualityBfwasteMapper;
import com.yb.quality.service.QualityBfwasteService;
import com.yb.quality.vo.QualityBfVO;
import com.yb.quality.vo.QualityBfwasteVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityBfwasteServiceImpl extends ServiceImpl<QualityBfwasteMapper, QualityBfwaste> implements QualityBfwasteService {


    @Override
    public List<QualityBfwasteVO> qualityBfwasteList(Integer maId, Integer wsId, String targetDay) {
        return baseMapper.qualityBfwasteList(maId, wsId, targetDay);
    }

    @Override
    public IPage<QualityBfVO> qualityBfVOList(IPage<QualityBfVO> iPage, Integer exId) {
        List<QualityBfVO> qualityBfVOS = baseMapper.qualityBfVOList(iPage, exId);
        return iPage.setRecords(qualityBfVOS);
    }
}
