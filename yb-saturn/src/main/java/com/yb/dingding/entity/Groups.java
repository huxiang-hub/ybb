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
public class Groups implements Serializable {

    private List<String> classes_list;
    private List<Selected_class> selected_class;
    private long group_id;
    private String group_name;
    private List<String> work_day_list;
    private boolean is_default;
    private int member_count;
    private String type;
    private long default_class_id;
    private List<String> dept_name_list;

}
