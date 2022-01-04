package com.vim.chatapi.staff.vo;

import com.vim.chatapi.staff.entity.WorkbatchShiftinfo;
import lombok.Data;

@Data
public class WorkbatchShiftinfoVO extends WorkbatchShiftinfo {

    private String dpName;
    private  String workDate;
    private String workStartTime;
    private String workEndTime;

}
