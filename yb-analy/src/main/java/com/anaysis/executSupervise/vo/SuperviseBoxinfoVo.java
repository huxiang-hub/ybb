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
package com.anaysis.executSupervise.vo;

import com.anaysis.executSupervise.entity.SuperviseBoxinfo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备当前状态表boxinfo-视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
public class SuperviseBoxinfoVo extends SuperviseBoxinfo {

    @ApiModelProperty(value="工序")
    private String prName;
    @ApiModelProperty(value="工序id")
    private Integer prId;
    @ApiModelProperty(value="部门名称")
    private String dpName;
    @ApiModelProperty(value="部门id")
    private Integer dpId;

    private Integer sdId;//设定排产单id

    private Integer keepRun;//设备持续运行分钟数

    private String usIds;//获取当前用户信息

}
