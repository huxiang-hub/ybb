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
package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteFault;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备停机故障记录表_yb_execute_fault视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteFaultVO对象", description = "设备停机故障记录表_yb_execute_fault")
public class ExecuteFaultVO extends ExecuteFault {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String usName;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String maName;
    /**
     * 设备ID
     */
    private Integer maId;
//    /**
//     * 停机间隔时间
//     */
//    private Integer duration;
    /**
     * 所属车间
     */
    @ApiModelProperty(value = "车间名称")
    private String dpName;
    private String name;
    @ApiModelProperty(value = "停机理由(父)")
    private String downtimeReasons;
    @ApiModelProperty(value = "停机理由(子)")
    private String fname;
    private Integer machineId;
    private Integer exceptionTime;
}
