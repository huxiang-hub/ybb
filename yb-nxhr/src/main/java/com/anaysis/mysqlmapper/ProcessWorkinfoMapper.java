package com.anaysis.mysqlmapper;

import com.anaysis.entity.ProcessWorkinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-25
 */

@Mapper
public interface ProcessWorkinfoMapper extends BaseMapper<ProcessWorkinfo> {

    @Select("select pr_name from yb_process_workinfo")
    List<String> getAllErps();

    @Select("select * from yb_process_workinfo where erp_id = #{erpId}")
    ProcessWorkinfo getByErpId(String erpId);

    @Select("")
    List<ProcessWorkinfo> getByErpIds(List<String> processErpIds);
}
