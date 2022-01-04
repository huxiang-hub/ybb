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
package com.yb.workbatch.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 当前时间的对应的班次信息及关联的设备内容集合信息
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@ApiModel(value = "WorkbatchShiftsetNowVO对象", description = "获取当前时间的对应的班次信息及关联的设备内容集合信息")
public class WorkbatchShiftsetNowVO {
    private static final long serialVersionUID = 1L;

    private Integer wsId;
    /*设备名称*/
    private Integer maId;

    private Integer maType;
    /*部门id信息*/
    private Integer dpId;

    /*model为5的时候表示设备id，model为2的时候表示部门信息 分类：1公司2车间部门3工序4设备5设备类型*/
    private Integer model;
    /*班次名称信息*/
    private String ckName;
}
