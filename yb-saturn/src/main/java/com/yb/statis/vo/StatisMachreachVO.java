package com.yb.statis.vo;

import com.yb.statis.entity.StatisMachreach;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StatisMachreachVO对象", description = "StatisMachreachVO对象")
public class StatisMachreachVO extends StatisMachreach {
    private static final long serialVersionUID = 1L;
    private String conUpdate;
}
