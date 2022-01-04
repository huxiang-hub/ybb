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
import com.yb.process.entity.ProcessParamlink;
import com.yb.process.vo.ProcessParamlinkVO;

import java.util.List;

/**
 * 工序参数关联表_yb_proc_paramlink 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IProcessParamlinkService extends IService<ProcessParamlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processParamlink
     * @return
     */
    IPage<ProcessParamlinkVO> selectProcessParamlinkPage(IPage<ProcessParamlinkVO> page, ProcessParamlinkVO processParamlink);

    /**
     * 新增工序参数信息
     * @param processParamlink
     */
    String insertProcessParamlink(ProcessParamlink processParamlink);

    /**
     * 修改工序参数信息
     * @param processParamlink
     */
    String updateProcessParamlink(ProcessParamlink processParamlink);

    String deleteProcessParamlink(Integer prId);

    List<ProcessParamlinkVO> selectProcessParamlinkByPrId(Integer prId);
}
