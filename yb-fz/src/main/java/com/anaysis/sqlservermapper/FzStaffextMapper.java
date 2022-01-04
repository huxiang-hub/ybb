package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseStaffext;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 基础信息表_yb_base_staffext Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface FzStaffextMapper extends BaseMapper<BaseStaffext> {
    List<BaseStaffext> list();
}
