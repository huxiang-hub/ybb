package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.WorkbatchShift;
import com.anaysis.executSupervise.vo.WorkbatchMachShiftVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkbatchShiftMapper extends BaseMapper<WorkbatchShift> {

    /**
     * 根据设备id获取班次信息
     *
     * @param maId
     * @return
     */
    List<WorkbatchMachShiftVO> findByMaId(@Param("maId") Integer maId);

    //获取设备班次id
    Integer getWsIdByMaId(@Param("maId") Integer maId);

    /**
     * 根据sdid查询工序id
     * @param sdId
     * @return
     */
    Integer getPrIdBySdId(Integer sdId);
}
