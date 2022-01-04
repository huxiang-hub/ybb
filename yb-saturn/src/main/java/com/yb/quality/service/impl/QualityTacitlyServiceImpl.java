package com.yb.quality.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.quality.entity.QualityTacitly;
import com.yb.quality.mapper.QualityTacitlyMapper;
import com.yb.quality.service.QualityTacitlyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityTacitlyServiceImpl extends ServiceImpl<QualityTacitlyMapper, QualityTacitly> implements QualityTacitlyService {

    @Autowired
    private QualityTacitlyMapper qualityTacitlyMapper;

    @Override
    public List<QualityTacitly> selectQualityTacitlyList(QualityTacitly qualityTacitly) {

        return qualityTacitlyMapper.selectQualityTacitlyList(qualityTacitly);
    }

    @Override
    public QualityTacitly getQualityTacitly(Integer pyId, String checkType) {
        return qualityTacitlyMapper.getQualityTacitly(pyId, checkType);
    }

}
