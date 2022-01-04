package com.yb.workbatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.workbatch.entity.WorkbatchMainshift;

import java.util.List;

/**
 * @author lzb
 * @date 2020-09-28
 */

public interface IWorkbatchMainshiftService extends IService<WorkbatchMainshift> {

    /**
     * 启用停用
     * @param id
     */
    void changeStatus(Integer id);

    List<WorkbatchMainshift> validList();

}
