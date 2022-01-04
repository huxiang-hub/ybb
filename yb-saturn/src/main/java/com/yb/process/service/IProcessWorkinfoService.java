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
package com.yb.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.vo.ProcessWorkinfoVO;

import java.util.List;

/**
 * 工序表--租户的工序内容（可以依据行业模版同步） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProcessWorkinfoService extends IService<ProcessWorkinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processWorkinfo
     * @return
     */
    IPage<ProcessWorkinfoVO> selectProcessWorkinfoPage(IPage<ProcessWorkinfoVO> page, ProcessWorkinfoVO processWorkinfo);
    /**
     * 根据工序名称查询工序entity
     */
    ProcessWorkinfoVO getProcessWorkInByName(String ProcessName);

    /**
     * 工序未排产数量
     * @param prId
     * @param ptId
     * @param wbId
     * @return
     */
    Integer unfinishNum(Integer prId, Integer ptId, Integer wbId);


    /**
     * 自定义分页
     *
     * @param page
     * @param processWorkinfo
     * @return
     */
    IPage<ProcessWorkinfoVO> selectSortProcessWorkinfoPage(IPage<ProcessWorkinfoVO> page, ProcessWorkinfoVO processWorkinfo);
    /**
     * 根据工序分类id查询工序
     */
    List<ProcessWorkinfoVO> workInfoBy(Integer pyId);
    /**
     *更具ID找到本条详情
     */
    ProcessWorkinfoVO selectSortProcessWorkinfo(Integer id);

    List<ProcessWorkinfo> listByMachine();
}
