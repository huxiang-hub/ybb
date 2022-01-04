package com.vim.chatapi.user.vo;

import lombok.Data;

/**
 * @author by SUMMER
 * @date 2020/4/6.
 */
@Data
public class UserDto {

    /**
     * 员工工号
     */
    private String jobnum;
    /**
     * 公司地址
     */
    private String address;
    /**
     * 员工所在部门名称
     */
    private String dpName;
    /**
     * 厂区名称
     */
    private String fname;
    /**
     * 员工职位名称
     */
    private String jobs;


    private String name;


}
