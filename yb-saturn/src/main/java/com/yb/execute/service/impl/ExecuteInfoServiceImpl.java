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
package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.mapper.ExecuteInfoMapper;
import com.yb.execute.service.IExecuteInfoService;
import com.yb.execute.vo.ExecuteInfoVO;
import com.yb.statis.request.ShiftReachExeinfoRequest;
import com.yb.yilong.request.WbNoInfoRequest;
import com.yb.yilong.response.WbNoInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 执行单_yb_execute_fault 服务实现类
 */
@Service
public class ExecuteInfoServiceImpl extends ServiceImpl<ExecuteInfoMapper, ExecuteInfo> implements IExecuteInfoService {

    @Autowired
    private ExecuteInfoMapper executeInfoMapper;

    @Override
    public IPage<ExecuteInfoVO> selectExecuteInfoPage(IPage<ExecuteInfoVO> page, ExecuteInfoVO executeInfo) {
        return page.setRecords(baseMapper.selectExecuteInfoPage(page, executeInfo));
    }

    @Override
    public List<ExecuteInfo> getSdIdsbyTime(Date startTime, Date endTime, Integer maId) {
        return executeInfoMapper.getSdIdsbyTime(startTime, endTime, maId);
    }

    @Override
    public List<WbNoInfoVO> wbNoInfo(WbNoInfoRequest request) {
        String replace = request.getTargetDay().replace("-", "");
        request.setTargetTime(replace);
        return executeInfoMapper.wbNoInfo(request);
    }

    @Override
    public int upExeTime(Integer exId, Date currTime) {
        return executeInfoMapper.upExeTime(exId, currTime);
    }

    @Override
    public int upEndTime(ExecuteInfo executeInfo) {
        return executeInfoMapper.upEndTime(executeInfo.getId(), executeInfo.getEndTime(),executeInfo.getUsId());
    }

    @Override
    public int upFinishTime(Integer exId, Date currTime) {
        return executeInfoMapper.upFinishTime(exId, currTime);
    }

    @Override
    public List<ExecuteInfoVO> getReachExecuteInfo(ShiftReachExeinfoRequest request){
        return executeInfoMapper.getReachExecuteInfo(request);
    }
}


