package com.anaysis.sqlservermapper;

import com.anaysis.entity.MachineMainfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 11:49
 **/
@Mapper
public interface HrMachineMapper {

    @Select("SELECT ObjID FROM MACHINES")
    List<String> getAllErpIds();

    List<MachineMainfo> getByErpIds(List<String> erpIds);
}
