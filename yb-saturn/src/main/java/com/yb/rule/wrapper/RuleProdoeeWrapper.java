package com.yb.rule.wrapper;

import com.yb.rule.entity.RuleProdoee;
import com.yb.rule.vo.RuleProdoeeVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

public class RuleProdoeeWrapper extends BaseEntityWrapper<RuleProdoee, RuleProdoeeVO> {
    public static RuleProdoeeWrapper build(){
        return new RuleProdoeeWrapper();
    }
    @Override
    public RuleProdoeeVO entityVO(RuleProdoee ruleProdoee) {
        RuleProdoeeVO ruleProdoeeVO = BeanUtil.copy(ruleProdoee, RuleProdoeeVO.class);
        return ruleProdoeeVO;
    }
}
