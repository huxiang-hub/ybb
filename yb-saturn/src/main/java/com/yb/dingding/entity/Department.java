/**
 * Copyright 2020 bejson.com
 */
package com.yb.dingding.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Auto-generated: 2020-09-16 9:48:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Department implements Serializable {

    private String ext;
    private Boolean createDeptGroup;
    private String name;
    private Integer id;
    private Boolean autoAddUser;
    private Long parentid;
}
