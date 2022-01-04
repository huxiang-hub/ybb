package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.MaterClassfiy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author lzb
 * @date 2020-11-30
 */

@Mapper
public interface MaterClassfiyMapper extends BaseMapper<MaterClassfiy> {

    @Select("")
    MaterClassfiy getByErpIds(String erpId);
}
