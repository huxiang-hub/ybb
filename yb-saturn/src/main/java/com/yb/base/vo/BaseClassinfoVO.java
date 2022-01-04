package com.yb.base.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yb.base.entity.BaseClassinfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BaseClassinfoVO对象", description = "班组信息_yb_base_classinfo")
public class BaseClassinfoVO extends BaseClassinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 车间部门名称
     */
    private String dpName;
    /**
     * 组长的staffinfoid
     */
    private Integer jobsId;
    /**
     * 组员staffinfoid
     */
    private String ids;
    /**
     * 组员名称
     */
    private String names;
    /**
     * 组员集合
     */
    private List<BaseStaffinfoVO> baseStaffinfoVOList;

    /**
     * 调换的班组id（newBcId）
     */
    private Integer newBcId;
    /**
     * 调换人员ids
     */
    private String exchangeIds;
    /**
     * 开始日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    /**
     * 结束日期
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
}
