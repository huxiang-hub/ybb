package com.vim.chatapi.staff.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.vim.chatapi.staff.entity.ActsetFlow;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("yb_actset_flow")
@ApiModel(value = "StaffFlow", description = "yb_actset_flow")
public class ActsetFlowVO extends ActsetFlow {
    private String avatar;
    private String realName;


}
