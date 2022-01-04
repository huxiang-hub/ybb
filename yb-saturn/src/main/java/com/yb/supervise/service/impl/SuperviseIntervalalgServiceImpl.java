package com.yb.supervise.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.supervise.entity.SuperviseIntervalalg;
import com.yb.supervise.mapper.SuperviseIntervalalgMapper;
import com.yb.supervise.service.SuperviseIntervalalgService;
import com.yb.supervise.vo.SuperviseIntervalalgEventVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuperviseIntervalalgServiceImpl extends ServiceImpl<SuperviseIntervalalgMapper, SuperviseIntervalalg> implements SuperviseIntervalalgService {

    @Autowired
    private SuperviseIntervalalgMapper SuperviseIntervalalgMapper;

    @Override
    public void createEvent(SuperviseIntervalalgEventVO superviseIntervalalgEventVO) {
        SuperviseIntervalalgMapper.createEvent(superviseIntervalalgEventVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteSuperviseIntervalalg() {
        SuperviseIntervalalgMapper.copyData();
        SuperviseIntervalalgMapper.renameTable();
//        SuperviseIntervalalgMapper.truncateTable();
    }
}
