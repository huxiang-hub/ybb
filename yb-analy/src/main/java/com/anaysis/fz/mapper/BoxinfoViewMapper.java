package com.anaysis.fz.mapper;

import com.anaysis.entity.BoxinfoViewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoxinfoViewMapper {
    List<BoxinfoViewEntity> getlist();
}
