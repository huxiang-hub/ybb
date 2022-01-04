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
package com.yb.supervise.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.mapper.SuperviseExecuteMapper;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.supervise.vo.SuperviseExecuteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备清零日志表 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class SuperviseExecuteServiceImpl extends ServiceImpl<SuperviseExecuteMapper, SuperviseExecute> implements ISuperviseExecuteService {

    @Autowired
    private SuperviseExecuteMapper superviseExecuteMapper;
    @Override
    public IPage<SuperviseExecuteVO> selectSuperviseExecutePage(IPage<SuperviseExecuteVO> page, SuperviseExecuteVO superviseExecute) {
        return page.setRecords(baseMapper.selectSuperviseExecutePage(page, superviseExecute));
    }


    @Override
    public Integer getCurrNum(Integer maId) {
        return superviseExecuteMapper.getCurrNum(maId);
    }

    @Override
    public Integer getCurrNumByOderId(Integer maId, Integer sdId) {
        return superviseExecuteMapper.getCurrNumByOderId(maId,sdId);
    }

    @Override
    public boolean updateStateToSuperviseExecute(SuperviseExecute execute) {
        return superviseExecuteMapper.updateStateToSuperviseExecute(execute);
    }

    @Override
    public SuperviseExecute getExecuteStateByOdId(Integer sdId) {
        return superviseExecuteMapper.getExecuteStateByOdId(sdId);
    }

    @Override
    public SuperviseExecute getExecuteOrder(Integer maId) {
        return superviseExecuteMapper.getExecuteOrder(maId);
    }

    @Override
    public boolean updateSuperviseExecuteBymMaId(Integer maId, String usIds) {
        return superviseExecuteMapper.updateSuperviseExecuteBymMaId(maId,usIds);
    }

    @Override
    public IPage<SuperviseExecuteVO> findExecuteOrderStatus(Integer current, Integer size, SuperviseExecuteVO superviseExecuteVO) {
        List<SuperviseExecuteVO> executeOrderStatus = superviseExecuteMapper.findExecuteOrderStatus(current, size,
                superviseExecuteVO.getOdName(), superviseExecuteVO.getUserName(), superviseExecuteVO.getEquipmentName());

        for(SuperviseExecuteVO viseExecuteVO : executeOrderStatus){
            if(!viseExecuteVO.getStatus().equals("3")){
                Date date = new Date();
                Date closeTime = viseExecuteVO.getCloseTime();
                long hour = date.getTime() - closeTime.getTime();
                double days = hour / 1000 / 60 ;
                double daysRound = Math.floor(days);
                long h = (long) Math.abs(daysRound);
                if(daysRound > 0){
                    viseExecuteVO.setExceedDate(h);
                    viseExecuteVO.setOdStatus("已超期");
                }else{
                    viseExecuteVO.setExceedDate(0L);
                    viseExecuteVO.setOdStatus("未超期");
                }
            }else {
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long exceedDate = viseExecuteVO.getEndTime().getTime() - viseExecuteVO.getCloseTime().getTime();
                double days = exceedDate / 1000 / 60 ;
                double daysRound = Math.floor(days);
                long h = (long) Math.abs(daysRound);
                viseExecuteVO.setExceedDate(h);
                if(h < 0){
                    viseExecuteVO.setOdStatus("超前完成");
                }else if(h == 0){
                    viseExecuteVO.setOdStatus("正常交付");
                }else {
                    viseExecuteVO.setOdStatus("延期完成");
                }
            }
        }

        IPage page = new Page();
        page.setRecords(executeOrderStatus);
        Integer total = superviseExecuteMapper.executeOrderCount(
                superviseExecuteVO.getOdName(), superviseExecuteVO.getUserName(), superviseExecuteVO.getEquipmentName());
        page.setTotal(total);
        long pages = total / size;
        if (total % size != 0) {
            pages++;
        }
        page.setPages(pages);
        return page;
    }

    @Override
    public SuperviseExecuteVO getBeanByUUID(String uuid) {
        return superviseExecuteMapper.getBeanByUUID(uuid);
    }
    /**
     * 删除对应设备id（ma_id）集合的数据
     */
    @Override
    public boolean removerListByMaid(List<Integer> maIds) {
        Integer result = superviseExecuteMapper.removerListByMaid(maIds);
        return result != null && result >= 1;
    }

    @Override
    public Map<String, Integer> getStartNum(Integer maId) {
        Map<String, Integer> map = superviseExecuteMapper.getStartNum(maId);
        return map;
    }

}
