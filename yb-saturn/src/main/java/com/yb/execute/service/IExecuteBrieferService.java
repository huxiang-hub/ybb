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
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.vo.*;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.prod.vo.ProdPartsinfoVo;
import org.springblade.core.tool.api.R;

import java.util.List;

/**
 * 生产执行上报信息_yb_execute_briefer 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteBrieferService extends IService<ExecuteBriefer> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeBriefer
     * @return
     */
    IPage<ExecuteBrieferVO> selectExecuteBrieferPage(IPage<ExecuteBrieferVO> page, ExecuteBrieferVO executeBriefer);

    IPage<ExecuteBrieferVO> pageFindList(Integer current, Integer size, ExecuteBrieferVO executeBrieferVO);

    ExecuteBriefer getStartTimeByCondition(Integer exId);

    /*  IPage<ExecuteBrieferVO> selectOrderExecuteBriefer(Integer orderId, String orderName);*/
    /**
     * 查询对应作业批次的对应部件生产状况
     */
    List<ExecuteBriefer> detailBatchNo(ProdPartsinfoVo partsinfoVo);

    /**
     * 获取班次的上报作业总数,良品总数等
     * @param exIds
     * @return
     */
    List<ExecuteBriefer> getNumberByExId(List<Integer> exIds);

    int updateCheck(ExecuteBriefer briefer);

    Integer getTotalByWfid(Integer wfid);

    Integer getTotalBySdid(Integer sdid);

    /**
     * 查询本设备已上报数据
     * @param targetDay 日期
     * @param maId 设备id
     * @param wsId 班次id
     * @return
     */
    List<ReportedVO> getExecuteBrieferList(String targetDay, Integer maId, Integer wsId);

    /**
     * 机台上报列表详情
     * @param id
     * @return
     */
    ExecuteBrieferDetailVO getExecuteBrieferDetail(Integer id);

    /**
     * 查询未上报的数据
     * @param notExecuteBrieferRequest
     * @param page
     * @return
     */
    IPage<ExecuteExamineVO> notExecuteBrieferList(NotExecuteBrieferRequest notExecuteBrieferRequest, IPage<ExecuteExamineVO> page);
}
