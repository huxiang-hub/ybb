package com.vim.chatapi.staff.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 搜索对象
 *
 * @author by SUMMER
 * @date 2020/4/6.
 */
@Data
public class SearchVo implements Serializable {

    //手机
    private String phone;
    //工号
    private String jobnum;
    //印聊号
    private String chatNo;
    //名称
    private String name;
    //租户id
    private String tenantId;
}
