package com.sso.supervise.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sso.mapper.ExecuteStateMapper;
import com.sso.supervise.entity.ExecuteState;
import com.sso.supervise.service.IExecuteStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 执行表状态_yb_execute_state 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExecuteStateServiceImpl extends ServiceImpl<ExecuteStateMapper, ExecuteState> implements IExecuteStateService {

    @Autowired
    private ExecuteStateMapper stateMapper;

    /*
    /*获取当前人的执行状态表信息
   * 获取该机长已有的助理
   * */
    @Override
    public ExecuteState getLeaderAide(int userId) {
        return stateMapper.getLeaderAide(userId);
    }

    @Override
    public int saveState(ExecuteState state) {
        return stateMapper.insert(state);

    }
}
