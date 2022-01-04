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
package com.anaysis.executSupervise.mapper;

import com.anaysis.executSupervise.entity.SuperviseInterval;
import com.anaysis.executSupervise.entity.SuperviseShiftcount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 设备当前状态表boxinfo-视图 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseShiftcountMapper extends BaseMapper<SuperviseInterval> {
    SuperviseInterval getLastState(String uuid);

    SuperviseInterval getId(Integer id);

    int update(SuperviseInterval intervalEntity);

    int save(SuperviseInterval intervalEntity);

    int updateLast(SuperviseInterval intervalEntity);

    int saveAlg(SuperviseShiftcount intervalEntity);

    SuperviseShiftcount getIdAlg(Integer id);

    int updateLastAlg(SuperviseShiftcount algintval);

    SuperviseShiftcount getLastStateLag(String uuid);

    Integer getPcountByMaIdAndStartTimeAndEndTime(@Param("maid") Integer maid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    SuperviseShiftcount getInTimeFirst(@Param("date") Date date, @Param("endDate") Date endDate, @Param("maId") Integer maId);

    SuperviseShiftcount getInTimeLast(@Param("date") Date date, @Param("endDate") Date endDate, @Param("maId") Integer maId);

    SuperviseShiftcount getInTimeLastSecond(@Param("date") Date date, @Param("endDate") Date endDate, @Param("maId") Integer maId, @Param("startNum") Integer startNum);


    SuperviseShiftcount getLastByUuid(@Param("uuid") String uuid);

    int getSumByPcount(@Param("date") Date date, @Param("endDate") Date endDate, @Param("maId") Integer maId);
}
