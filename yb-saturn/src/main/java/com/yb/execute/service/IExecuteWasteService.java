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
package com.yb.execute.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.execute.entity.ExecuteWaste;
import com.yb.execute.vo.ExecuteWastReportVO;
import com.yb.execute.vo.ExecuteWasteNumberVO;
import com.yb.execute.vo.ExecuteWasteVO;
import com.yb.panelapi.order.entity.QualityWasteReportDto;
import com.yb.panelapi.user.utils.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 质量检查废品表_yb_execute_waste 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteWasteService extends IService<ExecuteWaste> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeWaste
     * @return
     */
    IPage<ExecuteWasteVO> selectExecuteWastePage(IPage<ExecuteWasteVO> page, ExecuteWasteVO executeWaste);

    R wasteReport(QualityWasteReportDto wasteReportDto);

    List<ExecuteWasteVO> getWateByTime(Date startTime, Date endTime);

    /**
     * 废品上报
     * @param reportVO
     */
    void submitWaste(ExecuteWastReportVO reportVO);

    /**
     * 获取上两道工序废品
     * @param wfId
     * @return
     */
    List<ExecuteWasteNumberVO> lastTwoWaste(Integer wfId);
}
