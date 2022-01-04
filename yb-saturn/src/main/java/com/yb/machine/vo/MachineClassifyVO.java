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
package com.yb.machine.vo;

import com.yb.machine.entity.MachineClassify;
import com.yb.machine.entity.MachineMainfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 设备型号_yb_mach_classify视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MachineClassifyVO对象", description = "设备型号_yb_mach_classify")
public class MachineClassifyVO extends MachineClassify {
    private static final long serialVersionUID = 1L;
    /**
     * 设备的品牌ID
     */
    private Integer mtId;
    /***
     *
     */
    private Integer maId;
    /*类型下,所有设备*/
    private List<MachineMainfo> machineMainfoList;


    private List<MachineMainfo> mainfos;

}
