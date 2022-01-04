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
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.vo.ExecuteWasteVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 质量检查废品表_yb_execute_waste Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecuteWasteMapper extends BaseMapper<ExecuteWaste> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeWaste
     * @return
     */
    List<ExecuteWasteVO> selectExecuteWastePage(IPage page, ExecuteWasteVO executeWaste);

    Integer selectWasteBySdId(Integer sdId);

    List<ExecuteWasteVO> getWateByTime(Date startTime, Date endTime);
}
