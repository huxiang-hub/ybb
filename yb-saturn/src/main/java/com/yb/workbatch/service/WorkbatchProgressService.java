package com.yb.workbatch.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.workbatch.entity.WorkbatchProgress;
import com.yb.workbatch.vo.WorkbatchProgressVO;

public interface WorkbatchProgressService extends IService<WorkbatchProgress> {
    /**
     * 分页查询
     * @param page
     * @param workbatchProgressVO
     * @return
     */
    IPage selectWorkbatchProgressPage(IPage<String> page, WorkbatchProgressVO workbatchProgressVO);

    /**
     * 分页查询主工序
     * @param page
     * @param workbatchProgressVO
     * @return
     */
    IPage workbatchProgressPage(IPage<String> page, WorkbatchProgressVO workbatchProgressVO);
}
