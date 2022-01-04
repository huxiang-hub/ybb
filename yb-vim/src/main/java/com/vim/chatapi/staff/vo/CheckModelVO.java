package com.vim.chatapi.staff.vo;



import com.vim.chatapi.staff.entity.ActsetFlow;
import com.vim.chatapi.staff.entity.StaffDayoff;
import com.vim.chatapi.staff.entity.StaffLeave;
import com.vim.chatapi.staff.entity.StaffUscheck;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "", description = "审批模型")
public class CheckModelVO {

    private StaffDayoff staffDayoff;
    private StaffLeave staffLeave;//接收提交的补卡记录
    private ActsetFlow actsetFlow;// 接收提交的请假记录
    private StaffUscheck staffUscheck;//接收补卡记录
}
