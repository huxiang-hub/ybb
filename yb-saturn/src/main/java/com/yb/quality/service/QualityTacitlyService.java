package com.yb.quality.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.quality.entity.QualityTacitly;

import java.util.List;
import java.util.Map;

public interface QualityTacitlyService extends IService<QualityTacitly> {
    List<QualityTacitly> selectQualityTacitlyList(QualityTacitly qualityTacitly);

    /**
     * 根据工序分类和检查类型查询表详情
     * @param pyId
     * @param checkType
     * @return
     */
    QualityTacitly getQualityTacitly(Integer pyId, String checkType);
}
