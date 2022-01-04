package com.erp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erp.entity.XYPartsOutPut;
import org.apache.ibatis.annotations.Param;

public interface XYPartsOutPutMapper extends BaseMapper<XYPartsOutPut> {


    XYPartsOutPut get(@Param("maId") Integer maId, @Param("wbNo") String wbNo);

    XYPartsOutPut getByMaId(@Param("maId") Integer maId);

}
