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
package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 质量检查废品表_yb_execute_waste实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_waste")
@JsonSerialize(nullsUsing = NullSerializer.class)
@ApiModel(value = "ExecuteWaste对象", description = "质量检查废品表_yb_execute_waste")
public class ExecuteWaste implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "执行单ID(可选)")
    private Integer exId;

    @ApiModelProperty(value = "当前生产工序id")
    private Integer exPrid;

    @ApiModelProperty(value = "工序id")
    private Integer prId;

    @ApiModelProperty(value = "设备废品分类")
    private Integer mfId;

    @ApiModelProperty(value = "抽检人员ID")
    private Integer usId;

    @ApiModelProperty(value = "质检类型1订单巡检2车间巡检3公司巡检4上报废品")
    private String wsType;

    @ApiModelProperty(value = "废品数量")
    private Integer waste;

    @ApiModelProperty(value = "原因选择，数据字典，设备卡机1，操作失误2，正反错误3")
    private String reason;

    @ApiModelProperty(value = "输入内容")
    private String remarks;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty("间隔时间分钟")
    private Integer delayTime;

    @ApiModelProperty("是否处理停机弹窗事件0未处理1已处理")
    @JsonSerialize(nullsUsing = NullSerializer.class)
    private Integer handle;

    @ApiModelProperty("弹窗处理时间（操作人员在执行表中查询")
    private Date handleTime;




}
