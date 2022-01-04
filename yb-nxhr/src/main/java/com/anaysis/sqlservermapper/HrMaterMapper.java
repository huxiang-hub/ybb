package com.anaysis.sqlservermapper;

import com.anaysis.entity.MaterClassfiy;
import com.anaysis.entity.MaterMtinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/30 09:31
 **/
@Mapper
public interface HrMaterMapper {
    @Select("")
    List<String> getAllErpIds();

    @Select("")
    List<MaterMtinfo> getByErpIds(List<String> addErpIds);

    @Select("")
    MaterClassfiy getMaterClassfiy(String erpId);

    @Select("")
    MaterMtinfo getByProcessId(String erpId);
}
