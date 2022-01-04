package com.vim.chatapi.staff.vo;

import com.vim.chatapi.staff.entity.ActsetFlow;
import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.entity.StaffUscheck;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "StaffDayoff", description = "yb_staff_dayoff")
public class StaffDayoffVO extends StaffDayoff {
    private String dpName;
    private StaffLeave staffLeave;//接收提交的补卡记录
    private ActsetFlow actsetFlow;// 接收提交的请假记录
    private StaffUscheck staffUscheck;//接收补卡记录
    private Integer flag;/**1 就是请假申请 2 就是 1 补卡信息*/
    private Date applyStartTime; // 补卡申请的上班时间
    private Date applyEndTime; // 补卡申请的下班时间
    private Integer model;
    private Integer ckName;
    private Integer stayTime;
    private Date startDate;
    private Date endDate;
    private Date startTime;
    private Date endTime;
    private Integer usId;
    private String realName;
    /**
     * 审核结果
     */
    private String result;
    /**
     * 审核的的时间
     */
    private Date checkTime;





}
