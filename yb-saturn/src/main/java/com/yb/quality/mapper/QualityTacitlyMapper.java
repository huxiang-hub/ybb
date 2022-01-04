package com.yb.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.quality.entity.QualityTacitly;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QualityTacitlyMapper extends BaseMapper<QualityTacitly> {
    /**
     * 先查询所需的表和字段
     * @param pyId
     * @param checkType
     * @return
     */
    QualityTacitly getQualityTacitly(@Param("pyId") Integer pyId, @Param("checkType") String checkType);

    List<QualityTacitly> selectQualityTacitlyList(QualityTacitly qualityTacitly);
}
