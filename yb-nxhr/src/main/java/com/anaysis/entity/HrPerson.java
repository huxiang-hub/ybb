package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lzb
 * @Date 2020/12/1
 **/
@Data
public class HrPerson extends BladeUser {

    @ApiModelProperty(value = "工号（系统生成-也可以手工修改）")
    private String jobnum;

    @ApiModelProperty(value = "岗位（角色）1.机长2.班长3.车间主管4排产员")
    private Integer jobs;

    @ApiModelProperty(value = "籍贯（出生地）")
    private String hometown;

    @ApiModelProperty(value = "身份证")
    private String idcard;

    @ApiModelProperty(value = "学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6")
    private Integer education;

}
