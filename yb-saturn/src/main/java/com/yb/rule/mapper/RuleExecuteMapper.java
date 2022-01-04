package com.yb.rule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yb.rule.entity.RuleExecute;
import com.yb.rule.request.RuleExecuteSaveRequest;
import com.yb.rule.response.RuleExecuteListVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author my
 * @date 2020-09-19
 * Description:  Mapper
 */
@Mapper
public interface RuleExecuteMapper extends BaseMapper<RuleExecute> {

    List<RuleExecuteListVO> list(@Param("maId") Integer maId);

    void save(RuleExecute execute);

    void delete(@Param("ids") List<Integer> ids);

    List<WorkbatchOrdlink> sqlQuery(@Param("maId") String maId, @Param("sdId") Integer sdId, @Param("whereSql") String whereSql);

    void update(@Param("conditions") String conditions, @Param("whereSql") String whereSql, @Param("id") Integer id, @Param("request") RuleExecuteSaveRequest request);
}