package com.yb.rule.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.rule.entity.RuleScaleset;
import com.yb.rule.mapper.RuleScalesetMapper;
import com.yb.rule.response.RuleScalesetListVO;
import com.yb.rule.service.RuleScalesetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author my
 * @date 2020-09-22
 * Description: 标准产能维度设置_yb_rule_scaleset ServiceImpl
 */
@Service
@Slf4j
public class RuleScalesetServiceImpl implements RuleScalesetService {

    @Autowired
    private RuleScalesetMapper ruleScalesetMapper;


    @Override
    public List<RuleScalesetListVO> list() {
        List<RuleScalesetListVO> list = ruleScalesetMapper.list();
        return list;
    }
}

