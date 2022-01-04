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
package com.anaysis.hdverify.service.impl;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.anaysis.hdverify.entity.HdverifyMach;
import com.anaysis.hdverify.mapper.HdverifyMachMapper;
import com.anaysis.hdverify.service.HdverifyMachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 设备状态间隔表interval-视图 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class HdverifyMachServiceImpl implements HdverifyMachService {

    @Autowired
    private HdverifyMachMapper hdverifyMachMapper;

    @Override
    public HdverifyMach getId(Integer id) {
        return hdverifyMachMapper.getId(id);
    }

    @Override
    public HdverifyMach save(HdverifyMach hdverifyMach) {
        return hdverifyMachMapper.save(hdverifyMach);
    }

    @Override
    public HdverifyMach update(HdverifyMach hdverifyMach) {
        return hdverifyMachMapper.update(hdverifyMach);
    }

    @Override
    public  List<HdverifyMach> getHistory(Date querydate) {
        HdverifyMach query =new  HdverifyMach();
        query.setCreateAt(querydate);//匹配今天的数据信息
        query.setStatus(3);//设定已经验证过的数据列表
        return hdverifyMachMapper.queryByMach(query);
    }

    /****
     *
     * @return
     */
    @Override
    public List<HdverifyMach> getByrun(){
        HdverifyMach query =new  HdverifyMach();
        query.setCreateAt(new Date());//匹配今天的数据信息
        query.setStatus(2);//设定正在运行验证的设备信息列表
        return hdverifyMachMapper.queryByMach(query);
    }

    /****
     * 设备标识位，查询这台设备全部验证，按照时间倒序排列
     * @param maId
     * @return
     */
    @Override
    public List<HdverifyMach> getBymaId(Integer maId){
        HdverifyMach query = new  HdverifyMach();
        query.setMaId(maId);
        return hdverifyMachMapper.queryByMach(query);
    }

    @Override
    public SuperviseBoxinfo isStopByMaid(Integer maId) {
        //判定设备是否停机状态
        SuperviseBoxinfo boxinfo = hdverifyMachMapper.isStopByMaid(maId);
//        if (boxinfo != null && "2".equalsIgnoreCase(boxinfo.getStatus())) {  //判断是停机后返回真信息
//            return boxinfo;
//        }
        return boxinfo;
    }

    @Override
   public List<SuperviseBoxinfoVo> getMachList(SuperviseBoxinfoVo superviseBoxinfoVo){
        return hdverifyMachMapper.getMachList(superviseBoxinfoVo);
    }

}
