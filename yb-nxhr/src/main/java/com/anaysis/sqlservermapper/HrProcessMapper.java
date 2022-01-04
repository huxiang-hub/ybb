package com.anaysis.sqlservermapper;

import com.anaysis.entity.BomProcess;
import com.anaysis.entity.ProcessWorkinfo;
import com.anaysis.service.impl.ProcessWorkinfoServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author lzb
 * @Date 2020/11/25 18:04
 **/
@Mapper
public interface HrProcessMapper {
    @Select("")
    List<ProcessWorkinfo> getByErpId(Integer proId);

    List<ProcessWorkinfo> getByErpProId(String proId);

    List<String> getAllErps();

    @Select("SELECT b.BOM as bom, b.ProcessNo as processNo, m.ProcessCode as processCode  FROM BomProc b LEFT JOIN MACHINES m ON b.Machine = m.MachCode\n")
    List<BomProcess> getBomProcess();
}
