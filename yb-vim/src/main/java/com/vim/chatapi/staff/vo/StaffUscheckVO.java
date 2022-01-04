package com.vim.chatapi.staff.vo;


import com.baomidou.mybatisplus.annotation.TableName;
import com.vim.chatapi.staff.entity.StaffUscheck;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("yb_staff_uscheck")
@ApiModel(value = "StaffFlow", description = "yb_staff_uscheck")
public class StaffUscheckVO extends StaffUscheck {
    // 记录是下班卡 还是上班卡
    private Integer flag;

    //班次
    private String userCKname;

}
