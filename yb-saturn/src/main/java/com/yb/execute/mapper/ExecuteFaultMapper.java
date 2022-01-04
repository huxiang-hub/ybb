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
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.vo.ExecuteFaultParamVO;
import com.yb.execute.vo.ExecuteFaultRequest;
import com.yb.execute.vo.ExecuteFaultVO;
import com.yb.execute.vo.FaultMachineVO;
import com.yb.yilong.request.MachineDownPageRequest;
import com.yb.yilong.response.MachineDownPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备停机故障记录表_yb_execute_fault Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface ExecuteFaultMapper extends BaseMapper<ExecuteFault> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeFault
     * @return
     */
    List<ExecuteFaultVO> selectExecuteFaultPage(IPage page, @Param("executeFault") ExecuteFaultVO executeFault);

    int getCount(Integer mId);

    List<ExecuteFaultVO> selectByOrderId(Integer id);

    List<ExecuteFault> getClassfyList();

    List<ExecuteFaultVO> selectExecuteFaultPages(IPage page, ExecuteFaultVO executeFault);

    /**
     * 根据设备id,排产单id,时间区间查询停机列表
     *
     * @param maId      设备id
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return
     */
    List<FaultMachineVO> getExecuteFaultList(@Param("maId") Integer maId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询需要主动上报的停机
     *
     * @param maId
     * @return
     */
    ExecuteFault getStopInitiative(@Param("maId") Integer maId);

    /**
     * 查询停机总未确认数量
     *
     * @param startDate
     * @param endDate
     * @param maId
     * @return
     */
    Integer getAllUnconfirmedNum(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("maId") Integer maId);

    /**
     * 后台停机列表查询
     *
     * @param executeFaultRequest
     * @param page
     * @return
     */
    List<ExecuteFaultVO> executeFaultList(@Param("executeFaultRequest") ExecuteFaultRequest executeFaultRequest,
                                          @Param("page") IPage<ExecuteFaultVO> page);

    List<MachineDownPageVO> downPage(IPage<MachineDownPageVO> page, @Param("request") MachineDownPageRequest request);
}
