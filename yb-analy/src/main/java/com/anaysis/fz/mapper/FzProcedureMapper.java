package com.anaysis.fz.mapper;

import com.anaysis.entity.FzProcedureDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FzProcedureMapper {
    List<FzProcedureDO> list();

    FzProcedureDO getId(Integer id);
}
