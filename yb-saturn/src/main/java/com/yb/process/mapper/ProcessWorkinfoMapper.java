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
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.vo.ProcessWorkinfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 工序表--租户的工序内容（可以依据行业模版同步） Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProcessWorkinfoMapper extends BaseMapper<ProcessWorkinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processWorkinfo
     * @return
     */
    List<ProcessWorkinfoVO> selectProcessWorkinfoPage(IPage page, ProcessWorkinfoVO processWorkinfo);

    List<String> getProcessIdByOrderId(Integer id);

    List<ProcessWorkinfo> getProcessListByOrderId(List<String> processId);

    ProcessWorkinfoVO getProcessWorkInByName(String processName);
    /*根据部件id查工序*/
    List<ProcessWorkinfoVO> selectByPtId(Integer ptId);

    /*查询订单对应批次对应部件对应工序带排产的数量（该批次计划数量-该工序已排产总数量）*/
    Integer unfinishNum(Integer prId, Integer ptId, Integer wbId);



    /**
     * 自定义分页
     *
     * @param page
     * @param processWorkinfo
     * @return
     */
    List<ProcessWorkinfoVO> selectSortProcessWorkinfoPage(IPage page, ProcessWorkinfoVO processWorkinfo);
    /*查询分类下的工序*/
    List<ProcessWorkinfoVO> workInfoBy(Integer pyId);
    /**
     *更具ID找到本条详情
     */
    ProcessWorkinfoVO selectSortProcessWorkinfo(Integer id);

    /**
     * 有设备的工序集合
     * @return
     */
    List<ProcessWorkinfo> listByMachine();
}
