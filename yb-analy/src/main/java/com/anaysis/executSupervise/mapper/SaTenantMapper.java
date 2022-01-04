package com.anaysis.executSupervise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.system.entity.Tenant;

import java.util.List;


/**
 * Mapper 接口
 *
 * @author Chill
 */
@Mapper
public interface SaTenantMapper extends BaseMapper<Tenant> {
    String getTenantIdByUuid(@Param("uuid") String uuid);


    List<String> findTenant();
}
