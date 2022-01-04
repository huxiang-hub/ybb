package com.yb.execute.wrapper;

import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.vo.ExecuteTraycardVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

public class ExecuteTraycardWrapper extends BaseEntityWrapper<ExecuteTraycard, ExecuteTraycardVO> {
    public static ExecuteTraycardWrapper build(){
        return new ExecuteTraycardWrapper();
    }
    @Override
    public ExecuteTraycardVO entityVO(ExecuteTraycard entity) {
        ExecuteTraycardVO executeTraycardVO = BeanUtil.copy(entity, ExecuteTraycardVO.class);
        return executeTraycardVO;
    }
}
