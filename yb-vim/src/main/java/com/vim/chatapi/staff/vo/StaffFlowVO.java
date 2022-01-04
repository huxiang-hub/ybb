package com.vim.chatapi.staff.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.vim.chatapi.staff.entity.StaffFlow;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("yb_staff_flow")
@ApiModel(value = "StaffFlow", description = "yb_staff_flow")
public class StaffFlowVO extends StaffFlow {

}
