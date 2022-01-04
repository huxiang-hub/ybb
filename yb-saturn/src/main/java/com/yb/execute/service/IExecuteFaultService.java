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
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.vo.*;
import com.yb.panelapi.order.entity.DownFaultReportDto;
import com.yb.panelapi.user.utils.R;

import java.util.List;

/**
 * 设备停机故障记录表_yb_execute_fault 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteFaultService extends IService<ExecuteFault> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeFault
     * @return
     */
    IPage<ExecuteFaultVO> selectExecuteFaultPage(IPage<ExecuteFaultVO> page, ExecuteFaultVO executeFault);

    int getCount(Integer mId);

    R faultReport(DownFaultReportDto faultReportDto);

    List<ExecuteFaultVO> getDownFaultList(Integer id);

    List<ExecuteFault> getClassfyList();

    IPage<ExecuteFaultVO> selectExecuteFaultPages(IPage<ExecuteFaultVO> page, ExecuteFaultVO executeFault);

    /**
     * 查询停机列表
     * @param executeFaultParamVO
     * @return
     */
    List<ExecuteFaultWfIdVO> getExecuteFaultList(ExecuteFaultParamVO executeFaultParamVO);

    /**
     * 查询停机总未确认数量
     * @param executeFaultParamVO
     * @return
     */
    Integer getAllUnconfirmedNum(ExecuteFaultParamVO executeFaultParamVO);

    /**
     * 后台停机列表查询
     * @param executeFaultRequest
     * @param page
     * @return
     */
    IPage<ExecuteFaultVO> executeFaultList(ExecuteFaultRequest executeFaultRequest, IPage<ExecuteFaultVO> page);
}
