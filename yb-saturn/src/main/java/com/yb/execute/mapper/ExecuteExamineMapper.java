/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yb.execute.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.vo.ExamineParamVO;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecuteExamineVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 上报审核表_yb_execute_examine Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecuteExamineMapper extends BaseMapper<ExecuteExamine> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeExamine
     * @return
     */
    List<ExecuteExamineVO> selectExecuteExaminePage(IPage page, ExecuteExamineVO executeExamine);

    List<ExecuteExamineVO> pageFindExamineList(IPage<ExecuteExamineVO> page, @Param("examineParamVO") ExamineParamVO examineParamVO);

    Integer executeExamineCount(Integer sdId, String orderName);

    List<ExecuteExamineVO> getOutOfWorkRecord(String datesStr,Integer userId);

    ExecuteExamineVO selectExecuteExamine(Integer esId);

    List<ExecuteExamineVO> getModifyTrayByBfId(Integer bfId, String exMold);
}
