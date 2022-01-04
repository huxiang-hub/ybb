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
package com.anaysis.hdverify.mapper;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.anaysis.executSupervise.vo.SuperviseBoxinfoVo;
import com.anaysis.hdverify.entity.HdverifyMach;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface HdverifyMachMapper {
    HdverifyMach getId(Integer id);

    HdverifyMach save(HdverifyMach hdverifyMach);

    HdverifyMach update(HdverifyMach hdverifyMach);

    List<HdverifyMach> queryByMach(HdverifyMach hdverifyMach);

    SuperviseBoxinfo isStopByMaid(Integer maId);//判定设备是否停机状态

    List<SuperviseBoxinfoVo> getMachList(SuperviseBoxinfoVo superviseBoxinfoVo);//查询工厂的设备，并且设定工序名称为条件
}
