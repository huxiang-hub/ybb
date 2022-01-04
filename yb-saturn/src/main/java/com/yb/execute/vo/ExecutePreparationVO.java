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

import com.yb.execute.entity.ExecutePreparation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 生产准备记录_yb_execute_preparation视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecutePreparationVO对象", description = "生产准备记录_yb_execute_preparation")
public class ExecutePreparationVO extends ExecutePreparation {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    @ApiModelProperty(value = "设备Id")
    private Integer maId;
    @ApiModelProperty(value = "执行状态ID")
    private Integer ofId;
    @ApiModelProperty(value = "封装前端是开始操作还是结束操作")
    private Integer flag;
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    @ApiModelProperty(value = "结束时间")
    private Date finishAt;
    @ApiModelProperty(value = "1、装版 2、调试3、保养")
    private String readyType;
}
