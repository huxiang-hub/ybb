package com.anaysis.mysqlmapper;

import com.anaysis.entity.BoxinfoViewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoxinfoViewDAO {
    List<BoxinfoViewEntity> getlist();
}
