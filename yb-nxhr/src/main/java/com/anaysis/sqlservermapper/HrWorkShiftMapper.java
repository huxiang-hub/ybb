package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseClassinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/12/10 10:42
 **/
@Mapper
public interface HrWorkShiftMapper {

    @Select("SELECT ws.ShiftID AS bcName, m.ObjID AS erpMachineId FROM WorkShiftID ws LEFT JOIN MACHINES m ON ws.Machine = m.MachCode")
    List<BaseClassinfo> getAll();
}
