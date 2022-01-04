package com.vim.chatapi.user.vo;

import lombok.Data;
import org.springblade.system.user.entity.User;

import java.util.Date;
@Data
public class BaseStaffextVO extends User {
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 学历
     */
    private String education;
    /**
     * 地址
     */
    private Integer ID;
    private Integer sfId;
    private Date birthday;
    private String idcard;
    private String idaddr;
    private String hometown;
    private String curraddr;
    private Date createAt;
    /**
     *部门
     */
    private String dpName;

    /**
     *
     */
    private String jobNum;
    private String factoryName;
    private Integer staffextId;

}
