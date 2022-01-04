package com.anaysis.command.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author my
 * @date 2020-08-21
 * Description: 发送指令_yb_hdverify_command
 */
@Data
@TableName("yb_hdverify_command")
public class HdverifyCommand implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String uuid;

    /**
     * 指令类型1、控制2、设置3、查询
     */
    private Integer hcType;

    /**
     * 指令内容
     */
    private String sendContent;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 返回内容
     */
    private String returnContent;

    /**
     * 返回时间
     */
    private Date returnTime;

    /**
     * 发送是否成功1成功0失败
     */
    private Integer isSend;

    /**
     * 重发几次
     */
    private Integer sendNum;

    /**
     * 返回是否成功1成功0失败
     */
    private Integer isReturn;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updaateAt;


}
