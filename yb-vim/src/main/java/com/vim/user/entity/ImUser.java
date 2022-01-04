package com.vim.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vim.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 乐天
 * @since 2018-10-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ImUser  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String avatar;

    private String name;

    private String sign;

    @TableField(exist = false)
    private String jobnum;

    @TableField(exist = false)
    private Integer topFlag;

    private String mobile;

    private String email;

    @TableField(exist = false)
    private String curraddr;

    private String password;

    @TableField(exist = false)
    private String dpName;

    @TableField(exist = false)
    private Integer sex;

    @TableField("login_name")
    private String loginName;

    @TableField("default_group_id")
    private String defaultGroupId;
}
