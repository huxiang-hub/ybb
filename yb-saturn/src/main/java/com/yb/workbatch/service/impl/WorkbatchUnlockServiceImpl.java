package com.yb.workbatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.workbatch.entity.WorkbatchUnlock;
import com.yb.workbatch.mapper.WorkbatchUnlockMapper;
import com.yb.workbatch.service.WorkbatchUnlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkbatchUnlockServiceImpl extends ServiceImpl<WorkbatchUnlockMapper, WorkbatchUnlock> implements WorkbatchUnlockService {

    @Autowired
    private WorkbatchUnlockMapper workbatchUnlockMapper;

}
