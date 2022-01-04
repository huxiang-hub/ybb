package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchMainshift;
import com.yb.workbatch.mapper.WorkbatchMainshiftMapper;
import com.yb.workbatch.service.IWorkbatchMainshiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzb
 * @date 2020-09-28
 */

@Service
public class WorkbatchMainshiftServiceImpl extends ServiceImpl<WorkbatchMainshiftMapper, WorkbatchMainshift> implements IWorkbatchMainshiftService {

    @Autowired
    private WorkbatchMainshiftMapper workbatchMainshiftMapper;

    @Override
    public void changeStatus(Integer id) {
        WorkbatchMainshift workbatchMainshift = workbatchMainshiftMapper.selectById(id);
        if (workbatchMainshift != null) {
            if (workbatchMainshift.getIsUsed() == 0) {
                workbatchMainshift.setIsUsed(1);
            } else {
                workbatchMainshift.setIsUsed(0);
            }
            workbatchMainshiftMapper.updateById(workbatchMainshift);
        }
    }

    @Override
    public List<WorkbatchMainshift> validList() {
        List<WorkbatchMainshift> vos = workbatchMainshiftMapper.selectList(new QueryWrapper<WorkbatchMainshift>().eq("is_used", 1));
        return vos;
    }
}
