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

import com.yb.workbatch.entity.WorkbatchShiftset;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 排产班次设定_yb_workbatch_shifts（班次）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbatchShiftsetVO对象", description = "排产班次设定_yb_workbatch_shifts（班次）")
public class WorkbatchShiftsetVO extends WorkbatchShiftset {
    private static final long serialVersionUID = 1L;
//    公司/部门车间/工序/设备名称
    private String name;
    /*班次名称*/
    private String cName;
//    操作人名称
    private String userName;

    private List<WorkbatchShiftsetVO> workbatchShiftsetVOList;

    private String bcIds;
}
