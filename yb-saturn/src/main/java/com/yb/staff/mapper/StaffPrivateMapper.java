package com.yb.staff.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.staff.entity.StaffPrivate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StaffPrivateMapper extends BaseMapper<StaffPrivate> {
    StaffPrivate getPrivateInfo(@Param("usprv") StaffPrivate usprv);
}
