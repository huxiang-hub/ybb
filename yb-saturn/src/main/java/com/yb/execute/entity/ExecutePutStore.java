package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_execute_putstore")
@ApiModel(value = "ExecutePutStore对象", description = "yb_execute_putstore")
public class ExecutePutStore implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *  id                   int not null comment '流水id',
     *    es_id                int comment '执行对应执行ID',
     *    put_num              int comment '入库数量',
     *    put_time             timestamp comment '入库操作时间',
     *    us_id                int comment '入库操作人',
     *    put_addr             varchar(100) comment '入库位置',
     *    receiver             int comment '接收人',
     *    receiver_time        timestamp comment '签收时间',
     *    create_at            timestamp comment '创建时间',
     */
    /**
     * 流水id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 执行对应执行ID
     */
    @ApiModelProperty(value = "执行单id")
    private Integer exId;
    /**
     * 入库数量
     */
    @ApiModelProperty(value = "入库数量")
    private Integer putNum;
    /**
     * 入库操作时间
     */
    @ApiModelProperty(value = "入库操作时间")
    private Date putTime;
    /**
     * 入库操作人
     */
    @ApiModelProperty(value = "入库操作人")
    private Integer usId;
    /**
     * 入库位置
     */
    @ApiModelProperty(value = "入库位置")
    private String putAddr;
    /**
     * 接收人
     */
    @ApiModelProperty(value = "接收人")
    private  Integer receiver;
    /**
     * 签收时间
     */
    @ApiModelProperty(value = "签收时间")
    private Date receiverTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 原因
     */
    @ApiModelProperty(value = "创建时间")
    private String result;
    /**
     *
     */
    @ApiModelProperty(value = "签收总量")
    private Integer receiverNum;
    /**
     * 1未签收
     * 2已签收
     * 3拒绝
     */
    @ApiModelProperty(value = "签收总量")
    private Integer status;

}
