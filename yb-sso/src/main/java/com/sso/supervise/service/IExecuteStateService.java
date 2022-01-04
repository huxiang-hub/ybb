package com.sso.supervise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sso.supervise.entity.ExecuteState;

/**
 * 执行表状态_yb_execute_state 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteStateService extends IService<ExecuteState> {
    /**
     * 获取当前人的执行状态表信息
     */
     ExecuteState getLeaderAide(int userId);

    int saveState(ExecuteState state);
}
