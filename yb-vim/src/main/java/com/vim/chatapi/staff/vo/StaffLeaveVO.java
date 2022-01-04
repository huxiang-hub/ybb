package com.vim.chatapi.staff.vo;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vim.chatapi.staff.entity.StaffLeave;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@TableName("yb_staff_leave")
@ApiModel(value = "StaffFlow", description = "yb_staff_leave")
public class StaffLeaveVO extends StaffLeave {

    /***
     *部门名称
     *
     */
    private Integer id;
    private Integer dbId;
    private String dpName;
    private Integer model;
    private Integer ckName;
    private Integer stayTime;
    private Date startDate;
    private Date endDate;
    private Date startTime;
    private Date endTime;
    private Integer usId;
    private Date update;
    /**
     * 审核结果
     */
    private String result;
    /**
     * 审核时间
     */
    private Date checkTime;
    /**
     * 班次名称
     */
    private String userCKName;
}
