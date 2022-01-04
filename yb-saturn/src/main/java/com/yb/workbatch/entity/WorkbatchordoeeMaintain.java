package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_workbatchordoee_maintain")
@ApiModel(value = "WorkbatchordoeeMaintain对象", description = "生产排产OEE保养时间区间yb_workbatchordoee_maintain")
public class WorkbatchordoeeMaintain implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 生产排产OEE保养时间区间ID
     */
    @ApiModelProperty(value = "生产排产OEE保养时间区间ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 排产OEEID
     */
    @ApiModelProperty(value = "排产OEEID")
    private Integer wkOeeId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date maintainStartTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date maintainEndTime;
}
