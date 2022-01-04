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
package com.anaysis.executSupervise.service.impl;

import com.anaysis.executSupervise.entity.ExecuteState;
import com.anaysis.executSupervise.entity.SuperviseExecute;
import com.anaysis.executSupervise.mapper.SuperviseExecuteMapper;
import com.anaysis.executSupervise.service.SuperviseExecuteService;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备当前状态表boxinfo-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
@Slf4j
public class SuperviseExecuteServiceImpl extends ServiceImpl<SuperviseExecuteMapper, SuperviseExecute> implements SuperviseExecuteService {

    @Autowired
    private SuperviseExecuteMapper executMapper;


    @Override
    public SuperviseExecute getExecutByBno(String uuid) {
        return executMapper.getExecutByBno(uuid);
    }

    @Override
    public List<SuperviseExecute> getExecutListByStatus(String status) {
        return executMapper.getExecutList(status);
    }

    @Override
    public Map<String, String> getOrderName(Integer sdId) {
        return executMapper.getOrderName(sdId);
    }

    @Override
    public SuperviseExecute getUuidByMaId(Integer maId) {
        return executMapper.getExecutByMaid(maId);
    }


    @Override
    public int updateC1Byuuid(SuperviseBoxinfoVo boxinfo, ExecuteState exestate) {
        Date currtime = exestate.getCreateAt();
        SuperviseExecute oldexecute = getExecutByBno(boxinfo.getUuid());//获取当前订单状态信息
        SuperviseExecute newexecute = new SuperviseExecute();//需要更新后的数据信息
        newexecute.setUuid(boxinfo.getUuid());
        newexecute.setStartTime(oldexecute.getStartTime());//默认老的开始时间

        int startnum = (oldexecute.getStartNum() != null) ? oldexecute.getStartNum() : 0;
        Integer curr_num = boxinfo.getNumberOfDay() - startnum;
        newexecute.setEndNum(boxinfo.getNumberOfDay());
        newexecute.setCurrNum(curr_num);
        newexecute.setEsId(exestate.getId());
        if (oldexecute == null) {
            log.error("superExecute");
        }


        if (oldexecute.getExeStatus() != null && oldexecute.getExeStatus().equalsIgnoreCase(exestate.getStatus())) {//判断主状态相同的情况，判断事件不相同的时候设置新的事件Event
            if (oldexecute.getEvent() == null || (oldexecute.getEvent() != null && !oldexecute.getEvent().equalsIgnoreCase(exestate.getEvent()))) {
                newexecute.setEvent(exestate.getEvent());//设定新的事件信息
            }
            newexecute.setExeStatus(oldexecute.getExeStatus());//老的赋值给新的对象
        } else {
            //不相同的主状态信息
            newexecute.setExeStatus(exestate.getStatus());
            newexecute.setEvent(exestate.getEvent());
            newexecute.setStartTime(currtime);//更换主要状态时候更新开始时间
        }
        newexecute.setUpdateAt(currtime);
        return executMapper.updateC1Byuuid(newexecute);
    }

    @Override
    public int update(SuperviseExecute superviseExecute) {
        return executMapper.updateC1Byuuid(superviseExecute);
    }

}
