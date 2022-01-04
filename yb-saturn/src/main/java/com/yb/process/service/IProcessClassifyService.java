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
import com.yb.process.entity.ProcessClassify;
import com.yb.process.vo.PrModelVO;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.vo.PyModelVO;

import java.util.List;

/**
 * 工序分类表_yb_process_classify 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProcessClassifyService extends IService<ProcessClassify> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processClassify
     * @return
     */
    IPage<ProcessClassifyVO> selectProcessClassifyPage(IPage<ProcessClassifyVO> page, ProcessClassifyVO processClassify);

    /**
     *获取所有的工序信息
     */
    List<ProcessClassifyVO> getProClassifys();

    /**
     *获取所有的工序信息,顺序
     */
    IPage<ProcessClassifyVO> getSortProClassifys(IPage<ProcessClassifyVO> page, ProcessClassifyVO processClassify);
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
}
