package com.anaysis.command.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author my
 * @date 2020-08-22
 * Description: 盒子控制设置_yb_machine_setup
 */
@Data
@TableName("yb_machine_setup")
public class MachineSetup implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer maId;

    private String uuid;

    /**
     * 操作类型1正反纸
     */
    private Integer operateType;

    /**
     * 设置信息
     */
    private String msgSetup;

    /**
     * 用户信息
     */
    private String usId;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

}
