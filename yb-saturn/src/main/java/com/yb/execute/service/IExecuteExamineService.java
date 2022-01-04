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
import com.yb.base.entity.BasePicture;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.request.ExecuteBrieferinfoRequest;
import com.yb.execute.request.ExecuteExamineRequest;
import com.yb.execute.vo.ExamineParamVO;
import com.yb.execute.vo.ExecuteExamineVO;
import com.yb.execute.vo.TakeStockVO;

import java.util.List;
import java.util.Map;

/**
 * 修改确认表_yb_execute_examine 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IExecuteExamineService extends IService<ExecuteExamine> {

    /**
     * 自定义分页
     *
     * @param page
     * @param executeExamine
     * @return
     */
    IPage<ExecuteExamineVO> selectExecuteExaminePage(IPage<ExecuteExamineVO> page, ExecuteExamineVO executeExamine);

    IPage<ExecuteExamineVO> pageFindList(IPage<ExecuteExamineVO> iPage, ExamineParamVO examineParamVO);

    IPage<ExecuteExamineVO> pageQueryList(IPage<ExecuteExamineVO> iPage, ExecuteBrieferinfoRequest request);

    /**
     * ou
     *
     * @param datesStr
     * @return
     */
    List<ExecuteExamineVO> getOutOfWorkRecord(String datesStr, Integer userId);

    /**
     * 查看详情
     *
     * @param id
     * @return
     */
    ExecuteExamineVO selectExecuteExamine(Integer id);

    /**
     * 获取手机盘点记录
     *
     * @param odNo
     * @param maId
     * @param prId
     * @return
     */
    IPage<TakeStockVO> getPhoneDetail(IPage<TakeStockVO> page, String odNo, Integer maId, Integer prId, Integer exStatus, Integer storeAreaId);

    IPage<TakeStockVO> getPhoneDetailNocheck(IPage<TakeStockVO> page, String wbNo, Integer maId, Integer prId, Integer exStatus, Integer storeAreaId);

    /**
     * 手机盘点确认修改库位或数量
     * @param bfId
     * @param tyId
     * @param exPics
     * @param modifyStNo
     * @param modifyNumber
     */
    void phoneUpdateEtIdTdNo(Integer bfId, Integer tyId, String exPics, String originalStNo, String modifyStNo, Integer originalNumber, Integer modifyNumber, Integer rptUserid, String exMold, Integer exUserid);
    void phoneUpdateEtIdTdNo(ExecuteExamineRequest request);
                             /**
     * 根据上报id和修改类型查询数量和
     * @param bfId 上报id
     * @param exMold 修改类型1、盘点2、报废、3机长
     * @return
     */
    Map<String, Integer> getSumByBfId(Integer bfId, String exMold);

    List<ExecuteExamineVO> getModifyTrayByBfId(Integer bfId, String exMold);

    Map<String, Map<String, Integer>> getAllSumByBfId(Integer bfId);

    List<BasePicture> getImageByBfId(Integer bfId, String exMold);
}
