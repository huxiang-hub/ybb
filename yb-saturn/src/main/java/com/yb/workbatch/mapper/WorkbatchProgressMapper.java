package com.yb.workbatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.workbatch.entity.WorkbatchProgress;
import com.yb.workbatch.vo.WorkbatchProgressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WorkbatchProgressMapper extends BaseMapper<WorkbatchProgress> {
    /**
     * 分页查询
     * @param page
     * @param workbatchProgress
     * @return
     */
    List<WorkbatchProgress> selectWorkbatchProgressPage(IPage<WorkbatchProgress> page, WorkbatchProgress workbatchProgress);

    /**
     * 分组查询工单编号
     * @param page
     * @param workbatchProgressVO
     * @return
     */
    List<String> selectGroupWbNo(IPage<String> page, WorkbatchProgressVO workbatchProgressVO);

    /**
     * 根基工单编号查询主计划信息
     * @param wbNoList
     * @return
     */
    List<WorkbatchProgressVO> workbatchProgressPage(@Param("wbNoList") List<String> wbNoList);
}
