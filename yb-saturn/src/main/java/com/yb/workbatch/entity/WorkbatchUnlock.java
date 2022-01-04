package com.yb.workbatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yb_workbatch_unlock")
public class WorkbatchUnlock implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*被调整排产单*/
    private Integer wfId;
    /*调整前顺序*/
    private String beforeSort;
    /*调整后顺序*/
    private String afterSort;
    /*调整对象wfid*/
    private Integer swapWfid;
    /*调整对象之前顺序*/
    private String swapSort;
    /*调整记录数量*/
    private Integer swapNum;
    /*是否交换1是交换2是插入*/
    private Integer isSwap;
    /*us_id*/
    private Integer usId;
    /*调整人姓名*/
    private String usName;
    /*创建时间*/
    private Date createAt;

}
