package com.anaysis.sqlservermapper;

import com.anaysis.entity.ProcessClasslink;
import com.anaysis.entity.ProcessWorkinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FzProessWorkinfoDao {
    List<ProcessWorkinfo> list();

    ProcessClasslink getClasslink(@Param("Id") Integer Id);
}
