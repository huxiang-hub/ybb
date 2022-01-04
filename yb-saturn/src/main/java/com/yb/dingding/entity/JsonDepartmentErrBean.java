/**
 * Copyright 2020 bejson.com
 */
package com.yb.dingding.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-09-16 9:48:58
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class JsonDepartmentErrBean implements Serializable {

    private Integer errcode;
    private List<Department> department;
    private String errmsg;
}
