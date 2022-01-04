package com.sso.base.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sso.base.entity.BaseStaffinfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 人员表_yb_base_staffinfo视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BaseStaffinfoVO对象", description = "人员表_yb_base_staffinfo")
public class BaseStaffinfoVO extends BaseStaffinfo {
    private static final long serialVersionUID = 1L;
    /**
     * 设备Id
     */
    private Integer maId;

    private String avatar;
    /**
     * 部门相关
     */
    private String dpName;
    private String fullName;
    private Integer parentId;
    private String tenantId;
    private Integer sort;
    /**
     * 个人详情
     */
    private Integer extId;
    private Integer sex;
    private Integer education;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;
    private String idcard;
    private String idaddr;
    private String hometown;
    private String curraddr;

    //导入时中间使用
    private String modelName;
    private String jobsName;
    private String laborerName;
    private String hireTimeString;

    // 显示审核流程图使用
    /***
     * 驳回的结果原因
     */
    private String result;
    /**
     * 审核状态
     */
    private Integer status;

    //班组相关(是否可选)
    private String title;
    private boolean disabled;

    //     原班组
    private String oldClass;
    //    临时班组
    private String newClass;
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    //    临时调班的部门id
    private Integer newDpId;
}
