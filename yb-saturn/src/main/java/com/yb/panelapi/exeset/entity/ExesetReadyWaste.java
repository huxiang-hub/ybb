package com.yb.panelapi.exeset.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_exeset_readywaste")
public class ExesetReadyWaste {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer maId;
    private Integer model;
    private Integer waste;
    private Integer usId;
    private Date createAt;
    private Date updateAt;
}
