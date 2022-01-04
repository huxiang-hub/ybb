package com.yb.machine.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.saturn.entity.MainMixbox;

import java.util.List;

/**
 * 印联盒总表
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface MainMixboxMapper extends BaseMapper<MainMixbox> {

    /**
     * 存储总表盒子
     *
     * @param mixbox
     */
    void saveMixBox(@Param("mixbox") MainMixbox mixbox);


    /**
     * 根据租户id获取盒子
     *
     * @param tenantId
     * @return
     */
    List<MainMixbox> findByTenantId(@Param("tenantId") String tenantId);

    /**
     * 根据uuid获取盒子
     * @param uuid
     * @return
     */
    MainMixbox getByUuid(@Param("uuid") String uuid);
}
