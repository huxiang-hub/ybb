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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.actset.entity.ActsetCheckLog;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.mapper.ActsetCheckLogMapper;
import com.yb.actset.service.IActsetCheckLogService;
import com.yb.actset.vo.*;
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
public class ActsetCheckLogServicelmpl extends ServiceImpl<ActsetCheckLogMapper, ActsetCheckLog> implements IActsetCheckLogService {
    @Autowired
    private ActsetCheckLogMapper actsetCheckLogMapper;

    @Override
    public Integer getLogCount(Integer orderId) {

        return actsetCheckLogMapper.getLogCount(orderId);
    }

    @Override
    public ActsetCheckLogVO getActsetCheckLog(CheckModelVO modelVO) {

        return actsetCheckLogMapper.getActsetCheckLog(modelVO);
    }

    @Override
    public IPage<OrderCheckModelVO> getOrderCheckLogs(CheckModelVO modelVO, IPage<OrderCheckModelVO> page) {

        return page.setRecords(actsetCheckLogMapper.getOrderCheckLogs(modelVO, page));
    }

    @Override
    public IPage<ProductCheckModelVO> getProdCheckLogs(CheckModelVO modelVO, IPage<ProductCheckModelVO> page) {

        return page.setRecords(baseMapper.getProdCheckLogs(modelVO, page));
    }

    @Override
    public IPage<StoregeCheckModelVO> getStoregeCheckLogs(CheckModelVO modelVO, IPage<StoregeCheckModelVO> page) {

        return page.setRecords(baseMapper.getStoregeCheckLogs(modelVO, page));
    }

    @Override
    public ActsetCkflow getAwIdByType(String asType, String awType) {

        return baseMapper.getAwIdByType(asType, awType);
    }

    @Override
    public ActsetCheckLog getMoreNewCheckLog(Integer orderId) {
        return baseMapper.getMoreNewCheckLog(orderId);
    }

    @Override
    public IPage<OrderCheckModelVO> getOrderCheckLogsAndPdName(CheckModelVO modelVO, IPage<OrderCheckModelVO> page) {

        return page.setRecords(actsetCheckLogMapper.getOrderCheckLogsAndPdName(modelVO, page));
    }


}
