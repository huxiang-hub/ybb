package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.ProdProcelink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Mapper
public interface ProdProcelinkMapper extends BaseMapper<ProdProcelink> {

    @Select("")
    List<ProdProcelink> getByPtId(String ptId);
}
