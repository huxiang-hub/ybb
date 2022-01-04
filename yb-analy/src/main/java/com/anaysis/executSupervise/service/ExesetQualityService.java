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

import com.anaysis.executSupervise.entity.ExesetQuality;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 质量巡检设置_yb_exeset_quality 服务类
 *
 * @author Blade
 * @since 2020-03-10
 */
public interface ExesetQualityService extends IService<ExesetQuality> {
    ExesetQuality getQualitySetByMaId(Integer maId, Integer model);

    int getQualityCount(Integer maId);

    ExesetQuality getQualitySetByUuid(String uuid);

}
