package com.yb.rule.service;

import com.yb.rule.request.RuleExecuteSaveRequest;
import com.yb.rule.response.RuleExecuteListVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;

import java.util.List;

/**
 * @Description: 换膜/产能规则设定
 * @Author my
 * @Date Created in 2020/9/18 15:03
 */
public interface RuleExecuteService {

    void save(RuleExecuteSaveRequest request);

    void delete(List<Integer> ids);

    List<RuleExecuteListVO> list(Integer maId);

    void sqlQuery(String maId, Integer sdId, String whereSql);

    List<WorkbatchOrdlink> test(RuleExecuteSaveRequest request);

}
