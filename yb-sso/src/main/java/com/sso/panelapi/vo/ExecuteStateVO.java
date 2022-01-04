package com.sso.panelapi.vo;

import com.sso.supervise.entity.ExecuteState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

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
    private String orderName;
    private String orderNo;
    private String machineName;
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

    private String prName;
    private Integer difficultNum;
}
