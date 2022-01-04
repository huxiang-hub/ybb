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
package com.anaysis.entity;

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
 * 订单表_yb_order_ordinfo实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_order_ordinfo")
@ApiModel(value = "OrderOrdinfo对象", description = "订单表_yb_order_ordinfo")
public class OrderOrdinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private Integer pdId;
    /**
     * 订单名称
     */
    @ApiModelProperty(value = "订单名称")
    private String odName;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private Integer odCount;
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 预计废品
     */
    @ApiModelProperty(value = "预计废品")
    private Integer wasteCount;
    /**
     * 订货id
     */
    @ApiModelProperty(value = "订货厂家id")
    private Integer cmId;
    /**
     * 订货厂家缩写
     */
    @ApiModelProperty(value = "订货厂家缩写")
    private String cmShortname;
    /**
     * 订货厂家名称
     */
    @ApiModelProperty(value = "订货厂家名称")
    private String cmName;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date limitDate;
    /**
     * 审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过
     */
    @ApiModelProperty(value = "审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过  ")
    private Integer auditStatus;
    /**
     * 生产状态   0 未执行  1 正在执行 2 已完成
     */
    @ApiModelProperty(value = "生产状态   0 未执行  1 正在执行 2 已完成")
    private Integer productionState;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer usId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String remark;
   /* @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;*/

}
