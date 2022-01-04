package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseDeptinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 10:33
 **/
@Mapper
public interface HrDeptMapper {

    List<BaseDeptinfo> list();

    @Select("select DepartmentID from HRDepartment")
    List<String> getAllErpId();

    BaseDeptinfo getByErpId(String dpErp);

    List<BaseDeptinfo> getAll();

    List<BaseDeptinfo> getByErpIds(List<String> erpIds);
}
