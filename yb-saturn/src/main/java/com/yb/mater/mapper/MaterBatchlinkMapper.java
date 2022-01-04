package com.yb.mater.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.vo.MaterBatchlinkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author my
 * @date 2020-07-25
 * Description: 排产物料关联表_yb_mater_batchlink Mapper
 */
@Mapper
public interface MaterBatchlinkMapper extends BaseMapper<MaterBatchlink> {

    List<MaterBatchlink> findBySdId(@Param("sdId") Integer sdId);

    List<MaterBatchlink> findBySdIds(@Param("sdIds") List<Integer> sdIds);

    List<MaterBatchlink> findBySdIdAndMaterialNameAndIngredientNameAndMaterialDifference(@Param("sdId") Integer sdId, @Param("materialName") String materialName, @Param("ingredientName") String ingredientName, @Param("materialDifference") Integer
            materialDifference);

    /**
     * 分页查询
     *
     * @param materBatchlinkVO
     * @param page
     * @return
     */
    List<MaterBatchlinkVO> getpage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> page);

    /**
     * 查询所有状态信息
     *
     * @return
     */
    List<Integer> getStatus();

    /**
     * 查询未回料数据
     *
     * @param materBatchlinkVO
     * @param page
     * @return
     */
    List<MaterBatchlinkVO> waitPage(MaterBatchlinkVO materBatchlinkVO, IPage<MaterBatchlinkVO> page);
}
