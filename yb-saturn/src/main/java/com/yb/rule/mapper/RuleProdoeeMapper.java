package com.yb.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.rule.entity.RuleProdoee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RuleProdoeeMapper extends BaseMapper<RuleProdoee> {
    //新增model字段内容信息
    RuleProdoee selectRuleProdoee(Integer maId, Integer material, Integer planNum, Integer size, Integer model);
    /**
     * 根据规则查询规格
     *
     * @param maId
     * @param material
     * @param planNum
     * @param size
     * @return
     */
    RuleProdoee selectRuleProdoee(Integer maId, Integer material, Integer planNum, Integer size);

    /**
     * 根据设备id查询所有规则信息
     *
     * @param page
     * @param maId
     * @return
     */
    List<RuleProdoee> selectRuleProdoeeByMaId(IPage<RuleProdoee> page, Integer maId);
}
