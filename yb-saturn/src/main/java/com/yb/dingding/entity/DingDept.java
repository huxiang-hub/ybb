package com.yb.dingding.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_ding_dept")
public class DingDept implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ddId;
    private String parentid;
    private String name;
    private String ext;
    private String createDeptGroup;
    private String autoAddUser;
    private Date createAt;
    private Date updateAt;
}
