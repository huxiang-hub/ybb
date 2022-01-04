package com.anaysis.parames;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lzb
 * @Date 2021/3/31
 **/
@Data
@ApiModel("调用存储过程参数")
public class ProceduerParam {

    @ApiModelProperty("存储过程名")
    private String procedureName;

    @ApiModelProperty("list参数：与paramMap只传任意一个")
    private List<String> paramList;

    @ApiModelProperty("map参数：与paramList只传任意一个")
    private HashMap<String, String> paramMap;
}
