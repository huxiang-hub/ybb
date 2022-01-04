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
import com.yb.process.entity.ProcessParamlink;
import com.yb.process.vo.ProcessParamlinkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序参数关联表_yb_proc_paramlink Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ProcessParamlinkMapper extends BaseMapper<ProcessParamlink> {

    /**
     * 自定义分页
     *
     * @param page
     * @param processParamlink
     * @return
     */
    List<ProcessParamlinkVO> selectProcessParamlinkPage(IPage page, ProcessParamlinkVO processParamlink);


    List<ProcessParamlinkVO> selectProcessParamlinkByPrId(@Param("prId") Integer prId);
}
