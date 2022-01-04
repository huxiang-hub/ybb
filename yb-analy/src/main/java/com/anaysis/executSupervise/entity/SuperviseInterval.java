package com.anaysis.executSupervise.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_supervise_interval")
public class SuperviseInterval {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    private Integer maId;
    /**
     * 唯一标识位
     */
    private String uuid;
    /**
     * 状态 1运行2停机3故障4离线
     */
    private Integer status;
    /**
     * 开始时间计数
     */
    private Integer startNum;
    /**
     * 结束时间计数
     */
    private Integer endNum;
    /**
     * 当前盒子的计数
     */
    private Integer pcout;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 间隔时间
     */
    private Integer diffTime;
    /**
     * 区间速度
     */
    private Double currSpeed;
    /**
     * 记录小时
     */
    private Integer targetHour;
    /**
     * 记录分钟
     */
    private Integer targetMin;
    /**
     * 记录日期
     */
    private String targetDay;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 是否接单
     */
    @ApiModelProperty(value = "是否接单 0未接单 1接单")
    private Integer blnAccept;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String wbNo;
}
