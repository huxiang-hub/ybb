package com.yb.dingding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 钉钉用户表
 */
@Data
@TableName("yb_ding_userinfo")
public class DingUserinfo implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userid;
    private String unionid;
    private String openId;
    private String remark;
    private String isLeaderinDepts;
    private String isBoss;
    private String hiredDate;
    private String isSenior;
    private String tel;
    private String department;
    private String workPlace;
    private String email;
    private String orderinDepts;
    private String mobile;
    private String active;
    private String avatar;
    private String isAdmin;
    private String tags;
    private String isHide;
    private String jobnumber;
    private String name;
    private String stateCode;
    private String position;
    private String realAuthed;
    private Date createAt;
    private Date updateAt;
}
