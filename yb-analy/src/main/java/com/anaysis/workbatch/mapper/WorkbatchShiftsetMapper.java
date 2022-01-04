package com.anaysis.workbatch.mapper;

import com.anaysis.workbatch.entity.WorkbatchShiftset;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface WorkbatchShiftsetMapper extends BaseMapper<WorkbatchShiftset> {

    WorkbatchShiftset getByFirst();

    WorkbatchShiftset getNowWsTime(Integer maId);

    WorkbatchShiftset getNowWsDate(@Param("maId") Integer maId);
}
