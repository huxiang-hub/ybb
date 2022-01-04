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
package com.yb.exeset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.exeset.entity.ExesetFault;
import com.yb.exeset.vo.ExesetFaultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 故障停机设置_yb_exeset_fault Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExesetFaultMapper extends BaseMapper<ExesetFault> {

    /**
     * 自定义分页
     *
     * @param page
     * @param exesetFault
     * @return
     */
    List<ExesetFaultVO> selectExesetFaultPage(IPage page, ExesetFaultVO exesetFault);

    /**
     *  通过设备id查询当前设备设置的停机弹出信息
     *
     */

    ExesetFault selectExesetFault(Integer maId);
    /***
     * 故停机窗口设置 设置弹出窗口 ，窗口小时时间 等
     * remark    yb_exeset_fault;
     */
    public boolean updateFaultPoP(ExesetFault fault);


}
