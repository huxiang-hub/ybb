package com.vim.chatapi.user.vo;

import lombok.Data;
import org.springblade.system.user.entity.User;



@Data
public class UserVO extends User {

    private String code;
    private int flag;
    /*****/
    private String jobNum;
    private Integer staffextId;
    private String curraddr;
    private Integer staffinfoId;

}
