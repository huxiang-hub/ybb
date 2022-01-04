package com.yb.quality.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.quality.entity.QualityBfwaste;
import com.yb.quality.vo.QualityBfVO;
import com.yb.quality.vo.QualityBfwasteVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QualityBfwasteMapper extends BaseMapper<QualityBfwaste> {

    /**
     * 查询机台自检列表数据
     * @param maId
     * @param wsId
     * @param targetDay
     * @return
     */
    List<QualityBfwasteVO> qualityBfwasteList(@Param("maId") Integer maId,
                                              @Param("wsId")Integer wsId,
                                              @Param("targetDay")String targetDay);

    /**
     * 查询机台自检数据
     * @param iPage
     * @param exId
     * @return
     */
    List<QualityBfVO> qualityBfVOList(@Param("iPage") IPage<QualityBfVO> iPage,
                                      @Param("exId") Integer exId);
}
