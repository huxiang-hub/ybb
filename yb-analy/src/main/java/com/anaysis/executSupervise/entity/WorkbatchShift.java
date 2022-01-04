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
package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 排产班次执行表_yb_workbatch_shift实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_shift")
@ApiModel(value = "WorkbatchShift对象", description = "排产班次执行表_yb_workbatch_shift")
public class WorkbatchShift implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 班次设定表id
     */
    @ApiModelProperty(value = "班次设定表id")
    private Integer wsId;

    /**
     * 排产id
     */
    @ApiModelProperty(value = "排产id")
    private Integer sdId;


    /**
     * H类分类信息：/白班1/晚班2/夜班3
     */
    @ApiModelProperty(value = "H类分类信息：/白班1/晚班2/夜班3")
    private String ckName;

    /**
     * 班次开始时间
     */
    private Date startTime;
    /**
     * 班次结束时间
     */
    private Date endTime;

    /**
     * 区间时间（秒）
     */
    private Integer stayTime;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 排产日期
     */
    private String sdDate;
    /**
     * 状态（-1待排产,0起草1发布2正在生产3已完成4已挂起5废弃6已提交自审核7审核失败）
     */
    private String status;

    /**
     * 排产顺序
     */
    private String sdSort;

    /**
     * 计划数
     */
    private Integer planNum;

    /**
     * 计划总时间
     */
    private Integer planTotalTime;

    /**
     * 换膜时间
     */
    private Integer mouldStay;

    /**
     * 开始生产时间
     */
    private Date proBeginTime;

    /**
     * 结束时间
     */
    private Date proFinishTime;



    //废品数量
    private Integer wasteNum;
    //标准速度
    @ApiModelProperty(value = "速度（标准产能）")
    private Integer speed;

    //状态:0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）
    @ApiModelProperty(value = "0:待接单，1：生产中，2：生产完成  3：未上报（结束生产） 4：未完成（已上报）")
    private Integer shiftStatus;

    private Integer wfsortIslock; //是否锁定排产顺序 0不锁定 1锁定（需要进行授权调整排产顺序）
}
