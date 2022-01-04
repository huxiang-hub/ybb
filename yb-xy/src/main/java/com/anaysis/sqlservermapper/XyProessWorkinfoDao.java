package com.anaysis.sqlservermapper;

import com.anaysis.entity.ProcessClassify;
import com.anaysis.entity.ProcessClasslink;
import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.entity.ProcessWorkinfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface XyProessWorkinfoDao {
    List<ProcessWorkinfoVO> list();

    List<ProcessClassify> getClassifyList();

    ProcessClasslink getClasslink(@Param("Id") Integer Id);

}
