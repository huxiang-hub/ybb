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
package com.yb.exeset.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.exeset.entity.ExesetNetwork;
import com.yb.exeset.mapper.ExesetNetworkMapper;
import com.yb.exeset.service.IExesetNetworkService;
import com.yb.exeset.vo.ExesetNetworkVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网络设置管理_yb_exeset_network 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class ExesetNetworkServiceImpl extends ServiceImpl<ExesetNetworkMapper, ExesetNetwork> implements IExesetNetworkService {
    @Autowired
    private ExesetNetworkMapper networkSettingMapper;
    @Override
    public IPage<ExesetNetworkVO> selectExesetNetworkPage(IPage<ExesetNetworkVO> page, ExesetNetworkVO exesetNetwork) {
        return page.setRecords(baseMapper.selectExesetNetworkPage(page, exesetNetwork));
    }
    /**
     * 设置机台网络接口
     * @return
     */
    @Override
    public ExesetNetwork getNetwork(Integer maId) {

        return networkSettingMapper.getNetwork(maId);
    }

    @Override
    public boolean setNetwork(ExesetNetwork exesetNetwork) {
        return networkSettingMapper.setNetwork(exesetNetwork);
    }

    @Override
    public boolean setIsChart(Integer maId,Integer isChart) {

        return networkSettingMapper.setIsChart(maId,isChart);
    }



}
