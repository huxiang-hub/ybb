package com.yb.workbatch.request;

import com.yb.statis.dto.WorkBatchSortUpdateDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description:
 * @Author my
 * @Date Created in 2020/7/26 12:02
 */

@ApiModel("修改排产排序请求")
@Data
public class WorkBatchSortUpdateRequest {
    @ApiModelProperty("0无下工序，1有下工序(必传)")
    @NotNull
    private Integer type;


    @ApiModelProperty("班次id(必传)")
    @NotNull
    private Integer wsId;

    @ApiModelProperty("日期")
    @NotBlank
    private String sdDate;

    @ApiModelProperty("班次排序修改传输对象集合(必传)")
    @NotEmpty(message = "要修改的班次排序集合不能为空")
    List<WorkBatchSortUpdateDTO> workBatchSortUpdateDTOS;

    @ApiModelProperty("设备Id(必传)")
    @NotNull
    private Integer maId;
}
