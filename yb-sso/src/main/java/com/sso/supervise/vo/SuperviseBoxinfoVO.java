package com.sso.supervise.vo;


import com.sso.supervise.entity.SuperviseBoxinfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 设备当前状态表boxinfo-视图视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseBoxinfoVO对象", description = "设备当前状态表boxinfo-视图")
public class SuperviseBoxinfoVO extends SuperviseBoxinfo {
    private static final long serialVersionUID = 1L;
    private Integer odId;
    private String name;
    private Integer odCount;
    private Integer completeNum;
    private Integer unfinished;
    private Date limitDate;
    private String odName;
    private Integer wasteNum;
    private Integer countNum;
    private List<Integer> maNums;

}
