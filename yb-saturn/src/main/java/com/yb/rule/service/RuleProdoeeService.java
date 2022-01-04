package com.yb.rule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.rule.entity.RuleCondition;
import com.yb.rule.entity.RuleProdoee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RuleProdoeeService extends IService<RuleProdoee> {
    /**
     * 根据规则查询规格
     * @param ruleCondition
     * @return
     */
    RuleProdoee selectRuleProdoee(RuleCondition ruleCondition);

    /**
     * 根据设备id查询所有规则信息
     * @param maId
     * @return
     */
    IPage<RuleProdoee> selectRuleProdoeeByMaId(IPage<RuleProdoee> page, Integer maId);

    /***
     * 根据排产达到oee数据信息
     * @param sdId
     * @param sdDate
     * @param wsId
     * @return
     */
    boolean setRuleOrdoee(Integer sdId, String sdDate, Integer wsId, Integer maId,String status);
}
