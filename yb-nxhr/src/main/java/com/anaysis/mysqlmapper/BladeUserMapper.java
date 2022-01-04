package com.anaysis.mysqlmapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.anaysis.entity.BladeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzb
 * @date 2020-11-25
 */

@Mapper
public interface BladeUserMapper extends BaseMapper<BladeUser> {

    @Select("select erp_id from blade_user WHERE erp_id IS NOT NULL AND erp_id != ''")
    List<String> getErpIds();

}
