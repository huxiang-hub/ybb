package com.anaysis.mysqlmapper;

import com.anaysis.entity.SuperviseBoxinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoxInfoDAO extends BaseMapper<SuperviseBoxinfo> {
    List<SuperviseBoxinfo> list();


}
