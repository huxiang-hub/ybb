package com.vim.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author jobob
 * @since 2018-12-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_user_friend")
public class ImUserFriend extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 好友ID
     */
    private String friendId;

    /**
     * 用户分组
     */
    private String userGroupId;

    /**
     * 好友分组
     */
    private String friendGroupId;

    /**
     * 是否置顶
     */
    private Integer topFlag;

}
