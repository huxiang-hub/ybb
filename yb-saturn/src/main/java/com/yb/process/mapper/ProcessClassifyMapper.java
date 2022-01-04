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
package com.yb.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.vo.PyModelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工序分类表_yb_process_classify Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProcessClassifyMapper extends BaseMapper<ProcessClassify> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processClassify
     * @return
     */
    List<ProcessClassifyVO> selectProcessClassifyPage(IPage page, ProcessClassifyVO processClassify);
    /**
     *获取所有的工序信息
     */
    List<ProcessClassifyVO> getProClassifys();

    /**
     *获取所有的工序信息,顺序
     */
    List<ProcessClassifyVO> getSortProClassifys(IPage page, ProcessClassifyVO processClassify);
    /**
     * 修改所有状态为删除
     */
    boolean updateIsDel(List<Integer> ids);

    /**
     *数字字典查询所有的工序类型 和 ID
     */
    List<PyModelVO> getPrModelVO();

    /**
     * 根据排产班次id获取工序分类
     * @param wfId
     * @return
     */
    ProcessClassify getProcessClassifyByWfId(Integer wfId);

    @Select("SELECT py_id FROM yb_process_classlink WHERE pr_id = #{prId}")
    Integer getClassifyByPrId(Integer prId);

    @Select("SELECT pr_id FROM yb_process_classlink WHERE py_id = #{clId}")
    List<Integer> getPrIdsByClaId(Integer clId);
}
