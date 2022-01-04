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

import com.yb.execute.entity.ExecuteState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 执行表状态_yb_execute_state视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteStateVO对象", description = "执行表状态_yb_execute_state")
public class ExecuteStateVO extends ExecuteState {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("接收前端传过来的flag标识符")
    private Integer flag;
    /**
     * 执行状态ID
     */
    @ApiModelProperty(value = "执行状态ID")
    private Integer esId;
    /**
     * 1、装版 2、调试3、保养
     */
    @ApiModelProperty(value = "1、装版 2、调试3、保养")
    private String readyType;
    /**
     * 状态1正常0异常
     */
    @ApiModelProperty(value = "状态1正常0异常")
    private String event;
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;
    /**
     * 测试张数
     */
    @ApiModelProperty(value = "测试张数")
    private Integer testPaper;
    /**
     * 测试纸张修正值
     */
    @ApiModelProperty(value = "测试纸张修正值")
    private Integer usePaper;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date finishAt;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    private Integer endNum;
    private Integer maType;//设备类型

    private Integer startNum;

    private Integer maId;
    private Integer usId;
    private Integer sdId;

    private Integer countNum;

    private Integer boxNum;

    private Integer wasteNum;

    private Integer sumDuraction;

    private Integer countExecuteNumber;

    private Integer machineRuntimeDuratction;

    private Integer productNum;

    /**
     * 页面展示
     */
    private Integer prId;//工序id
    private Integer wfId;//工序id
    private Integer ckId;//班次id
    private Integer wsId;//班次id
    private Integer dpId;//车间id
    private String dpName;//车间名称
    private String orderName;
    private String orderNo;
    private String machineName;//设备名称
    private String machineNo;
    private String batchNo;
    private Integer workBatchId;
    private  String ordRemark;
    private Date batchCreateAt;
    private Date batchStartTime;
    private Date batchEndTime;
    private Integer batchDuration;
    private String batchNoInput;
    private String maIdInput;
    private String startDateInput;
    private String endDateInput;
    private String produceType;
    private String produceTime;
    private String produceColor;
    private String produceName;
    private String startTime;//查询条件,开始时间
    private String endTime;//查询条件,结束时间

    private String prName;
    private Double difficultNum;
    private Integer timeSet;
    private Integer pcount;



}
