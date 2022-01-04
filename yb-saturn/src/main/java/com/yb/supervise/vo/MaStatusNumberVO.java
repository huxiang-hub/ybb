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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备当前状态表boxinfo-视图视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@ApiModel(value = "MaStatusNumberVO")
public class MaStatusNumberVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /*不同状态的设备数量*/
    @ApiModelProperty(value = "设备数量")
    private Integer statusNumber;
    /*不同状态的设备数量*/
    @ApiModelProperty(value = "接单状态")
    private Integer blnAccept;
    /*设备状态*/
    @ApiModelProperty(value = "设备状态")
    private String status;
    @ApiModelProperty(value = "设备状态+接单状态")
    private String maStatus;
}
