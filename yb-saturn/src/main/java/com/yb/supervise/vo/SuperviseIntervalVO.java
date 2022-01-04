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
package com.yb.supervise.vo;

import com.yb.supervise.entity.SuperviseInterval;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备清零日志表视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseIntervalVO对象", description = "设备状态间隔表")
public class SuperviseIntervalVO extends SuperviseInterval {
    private static final long serialVersionUID = 1L;
    /****
     * 补充关联表查询  限制弹窗时间
     */
    private Integer limitTime; //设置限制时间

    /****
     * 补充关联表查询 限制记录时间
     */
    private Integer syslimitTime;

    /**
     * 每一天查询的总的数量
     */
    private Integer dayCount;
    /**
     *
     */
    private  String name;
    /**
     * 班次名称
     */
    private String ckName;
    /**
     * 班次id
     */
    private Integer ckId;
    /**
     * 设备名称
     */
    private String maName;
}
