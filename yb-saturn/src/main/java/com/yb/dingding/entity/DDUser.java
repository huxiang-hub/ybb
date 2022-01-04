/**
 * Copyright 2020 bejson.com
 */
package com.yb.dingding.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-09-16 10:11:51
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class DDUser implements Serializable {

    private String unionid;
    private String openId;
    private Boolean isLeader;
    private String mobile;
    private Boolean active;
    private Boolean isAdmin;
    private String avatar;
    private String userid;
    private Boolean isHide;
    private Boolean isBoss;
    private Long hiredDate;
    private String name;
    private String stateCode;
    private String position;
    private List<Long> department;
    private Long order;

}
