package com.screen.execute.vo;

import com.screen.execute.entity.WorkbatchOrdlink;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WorkbatchOrdlinkVO extends WorkbatchOrdlink {

    @ApiModelProperty(value = "工序名称")
    private String prName;
}
