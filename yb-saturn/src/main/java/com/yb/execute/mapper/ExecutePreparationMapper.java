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
import com.yb.execute.dto.ExecutePreparationDTO;
import com.yb.execute.entity.ExecutePreparation;
import com.yb.execute.vo.ExecutePreparationVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生产准备记录_yb_execute_preparation Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecutePreparationMapper extends BaseMapper<ExecutePreparation> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executePreparation
     * @return
     */
    List<ExecutePreparationDTO> selectExecutePreparationPage(IPage page, ExecutePreparationDTO executePreparation);

    /**
     *
     * 保养，换膜准备 向yb_execute_preparation
     * @param
     * @return
     */
    boolean savePreparationo(ExecutePreparation preparation);
    /***
     *
     *保养时向yb_execute_state表中加入执行Id和maId
     */
    boolean saveState(ExecutePreparationVO executePreparation);
    /***
     * 更新生产准备表的结束时间
     * @param preparation
     * @return
     */
    boolean updataExecutePreparation(ExecutePreparation preparation);
    /***
     * 获取指定Id记录
     * @param id
     * @return
     */
    ExecutePreparation getExecutePreparationById(Integer id);


}
