package com.yb.execute.wrapper;

import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.vo.ExecuteInfoVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 执行表_yb_execute_info包装类,返回视图层所需的字段
 *
 * @author Blade
 * @since 2020-03-10
 */
public class ExecuteInfoWrapper extends BaseEntityWrapper<ExecuteInfo, ExecuteInfoVO> {

    public static ExecuteInfoWrapper build() {
        return new ExecuteInfoWrapper();
    }

    @Override
    public ExecuteInfoVO entityVO(ExecuteInfo executeInfo) {
        ExecuteInfoVO executeInfoVO = BeanUtil.copy(executeInfo, ExecuteInfoVO.class);

        return executeInfoVO;
    }

}
