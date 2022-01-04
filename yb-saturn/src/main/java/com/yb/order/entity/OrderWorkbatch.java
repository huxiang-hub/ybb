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
package com.yb.order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业批次_yb_order_workbatch实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_order_workbatch")
@ApiModel(value = "OrderWorkbatch对象", description = "作业批次_yb_order_workbatch")
public class OrderWorkbatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Integer odId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号")
    private String batchNo;
    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    /**
     * 状态1新建2正在生产3部分生产4完成
     */
    @ApiModelProperty(value = "状态1新建2正在生产3部分生产4完成")
    private Integer status;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer userId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createAt;
    /**
     * 浪费数量
     */
    @ApiModelProperty(value = "浪费数量")
    private  Integer waste;
    /**
     * 批次截止日期
     */
    @ApiModelProperty(value = "批次截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date closeTime;


}
