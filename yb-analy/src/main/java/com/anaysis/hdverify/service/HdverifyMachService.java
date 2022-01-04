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
package com.anaysis.hdverify.service;


import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.anaysis.hdverify.entity.HdverifyMach;

import java.util.Date;
import java.util.List;

/**
 * 设备间隔状态表Interval-服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface HdverifyMachService {
    HdverifyMach getId(Integer id);

    HdverifyMach update(HdverifyMach hdverifyMach);

    HdverifyMach save(HdverifyMach hdverifyMach);

    List<HdverifyMach> getHistory(Date querydate);

    List<HdverifyMach> getByrun();

    List<HdverifyMach> getBymaId(Integer maId);

    SuperviseBoxinfo isStopByMaid(Integer maId);//判定设备是否停机状态

    //设备的工序查询条件；或者全部设备条件；或者部门条件
    List<SuperviseBoxinfoVo> getMachList(SuperviseBoxinfoVo superviseBoxinfoVo);
}
