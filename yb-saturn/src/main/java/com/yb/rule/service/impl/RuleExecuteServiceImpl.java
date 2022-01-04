package com.yb.rule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yb.common.constant.LocalEnum;
import com.yb.rule.entity.RuleExecute;
import com.yb.rule.mapper.RuleExecuteMapper;
import com.yb.rule.request.RuleExecuteSaveDTO;
import com.yb.rule.request.RuleExecuteSaveRequest;
import com.yb.rule.response.RuleExecuteListVO;
import com.yb.rule.service.RuleExecuteService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tomcat.jni.User;
import org.springblade.common.exception.CommonException;
import org.springblade.common.modelMapper.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: 换膜/产能规则设定
 * @Author my
 * @Date Created in 2020/9/18 15:03
 */
@Service
@Slf4j
public class RuleExecuteServiceImpl implements RuleExecuteService {

    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private RuleExecuteMapper ruleExecuteMapper;

    @Override
    public void save(RuleExecuteSaveRequest request) {

        Boolean LeftParenthesis = false;
        Boolean rightParenthesis = false;

        if (request.getRpType() == 1) {
            if (request.getMouldTime() == null) {
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "请输入换型时间");
            }
        } else {
            if (request.getCapacity() == null) {
                throw new CommonException(HttpStatus.NOT_FOUND.value(), "请输入标准产能");
            }
        }
        //条件sql
        StringBuilder whereSql = new StringBuilder();
        //json条件内容
        StringBuilder conditions = new StringBuilder();
        whereSql.append(" and ma_id = " + request.getMaId());
        for (int i = 0; i < request.getRuleExecuteSaveDTOS().size(); i++) {
            RuleExecuteSaveDTO o = request.getRuleExecuteSaveDTOS().get(i);
            String type = o.getRuleSettingType().getType();
            String desc = o.getRuleSettingType().getDesc();
            String leftBrackets = o.getLeftParenthesis() == true ? "(" : "";
            String rightBrackets = o.getRightParenthesis() == true ? ")" : "";
            if (i == 0) {
                whereSql.append(" and " + leftBrackets + o.getField() + o.getRuleSettingCondition() + o.getValue() + type + rightBrackets);
                continue;
            }
            //右模糊查询处理
            if (o.getRuleSettingCondition().trim().equals("%")) {
                whereSql.append(leftBrackets + o.getField() + "like" + "'" + o.getValue() + o.getRuleSettingCondition() + "'" + type + rightBrackets);
            } else if (o.getRuleSettingCondition().trim().equals("between")) {
                //处理条件为范围时后的,获取最小值最大值
                List<String> result = Arrays.asList(o.getValue().split(","));
                whereSql.append(leftBrackets + o.getField() + " BETWEEN " + result.get(0) + " and " + result.get(1) + type + rightBrackets);
            } else {
                whereSql.append(leftBrackets + o.getField() + o.getRuleSettingCondition() + o.getValue() + type + rightBrackets);
            }
            conditions.append(o.getName() + o.getRuleSettingCondition() + desc + " ");
        }
        String condition = JSONObject.toJSONString(request.getRuleExecuteSaveDTOS());

        if (request.getId() != null) {
            //修改
            ruleExecuteMapper.update(condition, whereSql.toString(), request.getId(), request);
            return;
        }
        //新增
        RuleExecute ruleExecute = ModelMapperUtil.getStrictModelMapper().map(request, RuleExecute.class);
        ruleExecute.setStatus(1);
        ruleExecute.setCreateAt(new

                Date());
        ruleExecute.setGenerateSql(whereSql.toString());
        ruleExecute.setCondition(condition);
        ruleExecuteMapper.save(ruleExecute);
        System.out.println(whereSql);
    }

    @Override
    public void delete(List<Integer> ids) {
        ruleExecuteMapper.delete(ids);
    }

    @Override
    public List<RuleExecuteListVO> list(Integer maId) {

        List<RuleExecuteListVO> list = ruleExecuteMapper.list(maId);
        return list;
    }

    @Override
    public void sqlQuery(String maId, Integer sdId, String whereSql) {
        ruleExecuteMapper.sqlQuery(maId, sdId, whereSql);
    }

    @Override
    public List<WorkbatchOrdlink> test(RuleExecuteSaveRequest request) {
        //条件sql
        StringBuilder whereSql = new StringBuilder();
        whereSql.append(" and ma_id = " + request.getMaId());
        for (int i = 0; i < request.getRuleExecuteSaveDTOS().size(); i++) {
            RuleExecuteSaveDTO o = request.getRuleExecuteSaveDTOS().get(i);
            String type = o.getRuleSettingType().getType();
            String leftBrackets = o.getLeftParenthesis() == true ? "(" : "";
            String rightBrackets = o.getRightParenthesis() == true ? ")" : "";
            if (i == 0) {
                whereSql.append(" and " + leftBrackets + o.getField() + o.getRuleSettingCondition() + o.getValue() + type + rightBrackets);
                continue;
            }
            //右模糊查询处理
            if (o.getRuleSettingCondition().trim().equals("%")) {
                whereSql.append(leftBrackets + o.getField() + "like" + "'" + o.getValue() + o.getRuleSettingCondition() + "'" + type + rightBrackets);
            } else if (o.getRuleSettingCondition().trim().equals("between")) {
                //处理条件为范围时后的,获取最小值最大值
                List<String> result = Arrays.asList(o.getValue().split(","));
                whereSql.append(leftBrackets + o.getField() + " BETWEEN " + result.get(0) + " and " + result.get(1) + type + rightBrackets);
            } else {
                whereSql.append(leftBrackets + o.getField() + o.getRuleSettingCondition() + o.getValue() + type + rightBrackets);
            }
        }

        List<WorkbatchOrdlink> workbatchOrdlinks = workbatchOrdlinkMapper.ruleQuery(whereSql.toString());
        return workbatchOrdlinks;
    }

}



