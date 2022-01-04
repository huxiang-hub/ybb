package com.anaysis.mysqlmapper;

import com.anaysis.entity.BladeDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.MachineMainfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-26
 */

@Mapper
public interface MachineMainfoMapper extends BaseMapper<MachineMainfo> {

    @Select("select erp_id from yb_machine_mainfo")
    List<String> getAllErpIds();

    @Select("select code, dict_key as dictKey, dict_value as dictValue from blade_dict WHERE code = 'maType' AND dict_key != -1")
    List<BladeDict> getAllMatype();
}
