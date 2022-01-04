package com.yb.mater.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.mater.entity.ExecuteMaterials;
import com.yb.mater.vo.ExecuteMaterialsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lzb
 * @date 2021-01-10
 */

@Mapper
public interface ExecuteMaterialsMapper extends BaseMapper<ExecuteMaterials> {

    List<ExecuteMaterialsVO> getListByWfId(Integer wfId);

    int getBarCodeByExists(String barCode);
}
