package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteTraycard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "生成标识卡参数")
public class ExecuteTraycardVO extends ExecuteTraycard {
    /*不删除的id集合*/
    @ApiModelProperty(value = "不删除的id集合")
    private List<Integer> etIdList;
    /*是否覆盖 0 不覆盖, 1 覆盖*/
    @ApiModelProperty(value = "是否覆盖 0 不覆盖, 1 覆盖")
    private Integer etStatus;
    /*已变红总量*/
    @ApiModelProperty(value = "已变红总量")
    private Integer contNum;
    /*已变红台数*/
    @ApiModelProperty(value = "已变红台数")
    private Integer redNum;
    /*是否变红*/
    @ApiModelProperty(value = "是否变红 0未变红,1已变红")
    private Integer redStatus;
    @ApiModelProperty(value = "班次名称")
    private String wsName;

    @ApiModelProperty(value = "用户名称")
    private String usName;

    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "产品名称")
    private String pdName;
    @ApiModelProperty(value = "工序名称")
    private String prName;
    @ApiModelProperty(value = "工单编号")
    private String wbNo;
    @ApiModelProperty(value = "设备名称")
    private String maName;
    @ApiModelProperty(value = "编号")
    private String traycardNO;
    @ApiModelProperty(value = "生产时间")
    private String productTime;
    @ApiModelProperty(value = "最后打印时间")
    private String printTime;

}
