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
package com.anaysis.executSupervise.service;


import com.anaysis.executSupervise.entity.SuperviseInterval;
import com.anaysis.executSupervise.entity.SuperviseShiftcount;

/**
 * 设备间隔状态表Interval-服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface SuperviseIntervalService {
    SuperviseInterval getId(Integer id);

    SuperviseInterval getLastState(String uuid);

    int update(SuperviseInterval intervalEntity);

    int save(SuperviseInterval intervalEntity);

    int setStatusLog(SuperviseInterval intervalEntity);

    int updateLast(SuperviseInterval oldIntval);

    int saveAlg(SuperviseShiftcount algintval);

    SuperviseShiftcount getAlgid(Integer id);

    int updateLastAlg(SuperviseShiftcount oldIntval);

    SuperviseShiftcount getLastStateLag(String uuid);
}
