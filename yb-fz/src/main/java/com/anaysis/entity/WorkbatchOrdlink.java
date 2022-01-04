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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 生产排产表yb_workbatch_ordlink实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_ordlink")
@ApiModel(value = "WorkbatchOrdlink对象", description = "生产排产表yb_workbatch_ordlink")
public class WorkbatchOrdlink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单ID
     */
    @ApiModelProperty(value = "工单ID")
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 作业批次ID
     */
    @ApiModelProperty(value = "作业批次ID")
    private Integer wbId;
    /**
     * 流水号-设备id
     */
    @ApiModelProperty(value = "流水号-设备id")
    private Integer maId;
    /**
     * ERP工单（待定）
     */
    @ApiModelProperty(value = "ERP工单（待定）")
    private Integer erpWbId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNo;
    /**
     * 部件id
     */
    @ApiModelProperty(value = "部件id")
    private Integer ptId;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件编号")
    private String ptNo;
    /**
     * 部件名称
     */
    @ApiModelProperty(value = "部件名称")
    private String partName;
    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private Integer prId;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;
    /**
     * 工艺说明
     */
    @ApiModelProperty(value = "工艺说明")
    private String prDes;
    /**
     * 工作排序
     */
    @ApiModelProperty(value = "工序排序")
    private Integer sort;
    /**
     * 产品编号
     */
    @ApiModelProperty(value = "产品编号")
    private String pdCode;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    /**
     * 产品图片
     */
    @ApiModelProperty(value = "产品图片")
    private String pdImageurl;
    /**
     * 产品类型
     */
    @ApiModelProperty(value = "产品类型")
    private String pdType;
    /**
     * 计划数量
     */
    @ApiModelProperty(value = "计划数量")
    private Integer planNum;
    /**
     *
     */
    @ApiModelProperty(value = "应交数")
    private Integer planNumber;
    /**
     * 完成数量
     */
    @ApiModelProperty(value = "完成数量")
    private Integer completeNum;
    /**
     * 未完成数量
     */
    @ApiModelProperty(value = "未完成数量")
    private Integer incompleteNum;
    /**
     * 冗余数
     */
    @ApiModelProperty(value = "冗余数")
    private Integer extraNum;
    /**
     * 状态（0起草1发布2正在生产3已完成4已挂起5废弃）
     */
    @ApiModelProperty(value = "状态（0起草1发布2正在生产3已完成4已挂起5废弃）")
    private String status;
    /**
     * 0:待接单，1：生产中，2：生产完成
     */
    @ApiModelProperty(value = "0:待接单，1：生产中，2：生产完成")
    private Integer runStatus;
    /**
     * 计划开始时间
     */
    @ApiModelProperty(value = "计划开始时间")
    private Date startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;
    /**
     * 实际开始时间
     */
    @ApiModelProperty(value = "实际开始时间")
    private Date actuallyStarttime;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    private Date closeTime;
    /**
     * 计划用时
     */
    @ApiModelProperty(value = "计划用时")
    private Integer planTime;
    /**
     * 废品
     */
    @ApiModelProperty(value = "废品")
    private Integer waste;
    /**
     * 废品率
     */
    @ApiModelProperty(value = "废品率")
    private Integer nbwaste;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 排产排序
     */
    @ApiModelProperty(value = "排产排序")
    private Integer sdSort;
    /**
     * 排产时间
     */
    @ApiModelProperty(value = "排产日期")
    private String sdDate;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "创建时间")
        private Date updateAt;

    /**
     * 出勤人数
     */
    @ApiModelProperty(value = "计划出勤人数")
    private Integer dutyNum;
    /**
     * 班次id
     */
    @ApiModelProperty(value = "班次id")
    private Integer wsId;
    /**
     * 车间id
     */
    @ApiModelProperty(value = "车间id")
    private Integer dpId;


}
