package com.yb.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.rule.entity.RuleScaleset;
import com.yb.rule.response.RuleScalesetListVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 * @author my
 * @date 2020-09-22
 * Description: 标准产能维度设置_yb_rule_scaleset Mapper
 */
@Mapper
public interface RuleScalesetMapper extends BaseMapper<RuleScaleset> {

    List<RuleScalesetListVO> list();

}