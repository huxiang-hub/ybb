package com.anaysis.sqlservermapper;

import com.anaysis.entity.BaseStaffinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 人员表_yb_base_staffinfo Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface XyStaffinfoMapper extends BaseMapper<BaseStaffinfo> {

    List<BaseStaffinfo> list();
}
