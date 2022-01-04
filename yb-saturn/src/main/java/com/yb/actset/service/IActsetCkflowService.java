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
package com.yb.actset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.entity.ActsetCkset;
import com.yb.actset.vo.ActsetCkflowVO;
import com.yb.actset.vo.CheckViewModel;

import java.util.List;

/**
 * 排产班次设定_yb_workbatch_shifts（班次） 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface IActsetCkflowService extends IService<ActsetCkflow> {

    /**
     * 获取具体类型的审核流程
     * @return
     */
    public ActsetCkflowVO getActsetCkflow (String asType,String awType);
    /**
     * 获取下一个审核工序
     */
    ActsetCkflow getNextActsetCkflow(Integer awId,Integer leve,Integer sort);

    /**
     * 获取上一个审核工序
     * @param
     * @return
     */
    ActsetCkflow getUpLevelActsetCkflow(Integer awId,Integer leve,Integer sort);
    /**
     * 通过设置的审核级数获取审核流程已经先后顺序
     * @param leave
     * @return
     */
    List<ActsetCkflow> getCheckSort(Integer asId, Integer leave);

    /**
     * 找出正在审核流程信息
     * @param asId
     * @param leave
     * @return
     */
    List<CheckViewModel> getCheckSortInfo(Integer asId, Integer leave, Integer orderId);
    /**
     * 找出设定的审核流程信息
     * @param asId
     * @param leave
     * @return
     */
    List<CheckViewModel> getSetCheckSortInfo(Integer asId, Integer leave);
}
