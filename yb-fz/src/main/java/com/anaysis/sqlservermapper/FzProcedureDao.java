package com.anaysis.sqlservermapper;

import com.anaysis.sqlservermapper.domain.FzProcedureDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FzProcedureDao {
    List<FzProcedureDO> list();

    FzProcedureDO getId(Integer id);
}
