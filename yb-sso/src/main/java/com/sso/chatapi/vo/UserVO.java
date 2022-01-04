package com.sso.chatapi.vo;


import com.sso.chatapi.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserVO extends User {

    private String code;
    private int flag;
    /*****/
    private String jobNum;
    private Integer staffextId;
    private String curraddr;
    private Integer staffinfoId;
    @ApiModelProperty(value = "钉钉免登code")
    private String authCode;
    @ApiModelProperty(value = "钉钉标识符")
    private String apUnique;

}
