package com.anaysis.executSupervise.service.impl;

import com.anaysis.executSupervise.entity.ExecuteState;
import com.anaysis.executSupervise.mapper.ExecuteStateMapper;
import com.anaysis.executSupervise.service.ExecuteStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author by SUMMER
 * @date 2020/3/18.
 */
@Service
public class ExecuteStateServiceImpl extends ServiceImpl<ExecuteStateMapper, ExecuteState> implements ExecuteStateService {

    @Autowired
    private ExecuteStateMapper stateMapper;


    @Override
    public int saveState(ExecuteState state) {
        return stateMapper.insert(state);
    }

    @Override
    public int updateState(ExecuteState state) {
        return stateMapper.updateState(state);
    }

    //查询是否已经插入C1操作信息
    @Override
    public List<ExecuteState> getExecutC1(ExecuteState execute) {
        return stateMapper.getExecutC1(execute);
    }

    @Override
    public ExecuteState getB1Execut(ExecuteState execute) {
        return stateMapper.getB1Execute(execute);
    }
}
