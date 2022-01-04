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
package com.yb.actset.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.mapper.ActsetCkflowMapper;
import com.yb.actset.service.IActsetCkflowService;
import com.yb.actset.vo.ActsetCkflowVO;
import com.yb.actset.vo.CheckViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 人员排班表_yb_workbatch_staffwk 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ActsetCkflowServicelmpl extends ServiceImpl<ActsetCkflowMapper, ActsetCkflow> implements IActsetCkflowService {
    @Autowired
    private ActsetCkflowMapper actsetCkflowMapper;
    @Override
    public ActsetCkflowVO getActsetCkflow(String asType,String awType) {

        return actsetCkflowMapper.getActsetCkflow( asType, awType);
    }

    @Override
    public ActsetCkflow getNextActsetCkflow(Integer awId,Integer leve,Integer sort) {

        return actsetCkflowMapper.getNextActsetCkflow(awId,leve,sort);
    }

    @Override
    public ActsetCkflow getUpLevelActsetCkflow(Integer awId,Integer leve,Integer sort) {
        return actsetCkflowMapper.getUpLevelActsetCkflow(awId,leve,sort);
    }

    @Override
    public List<ActsetCkflow> getCheckSort(Integer asIde, Integer leave) {
        return actsetCkflowMapper.getCheckSort(asIde,leave);
    }

    @Override
    public List<CheckViewModel> getCheckSortInfo(Integer asId, Integer leave, Integer orderId) {
        return actsetCkflowMapper.getCheckSortInfo(asId,leave,orderId);
    }

    @Override
    public List<CheckViewModel> getSetCheckSortInfo(Integer asId, Integer leave) {
        return actsetCkflowMapper.getSetCheckSortInfo(asId,leave);
    }
}
