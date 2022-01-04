package com.yb.dingding.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2020-11-09 9:50:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Selected_class implements Serializable {

    private long class_id;
    private String class_name;
    private List<Sections> sections;
    private Setting setting;

}
