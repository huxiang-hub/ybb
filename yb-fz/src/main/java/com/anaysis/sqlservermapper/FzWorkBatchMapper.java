package com.anaysis.sqlservermapper;

import com.anaysis.entity.WorkbatchOrdlink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FzWorkBatchMapper {
//    查询工作中心
    List<Integer> listIGzzxId();
//    查询工作中心下的排产信息
    List<WorkbatchOrdlink> listByIGzzxId(@Param("iGzzxId") Integer iGzzxId);
//    设备查询排产
    WorkbatchOrdlink selectById(@Param("Id") Integer Id);
}
