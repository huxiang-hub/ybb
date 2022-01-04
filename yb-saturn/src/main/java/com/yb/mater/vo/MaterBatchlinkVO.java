package com.yb.mater.vo;

import com.yb.mater.entity.MaterBatchlink;
import lombok.Data;

import java.util.Date;

@Data
public class MaterBatchlinkVO extends MaterBatchlink {

    private String cmName;//客户名称
    private String pdName;//产品名称
    private Date startTime;//查询条件开始时间
    private Date endTime;//查询条件结束时间
}
