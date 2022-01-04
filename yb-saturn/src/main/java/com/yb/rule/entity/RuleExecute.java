package com.yb.rule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author my
 * @date 2020-09-19
 * Description:
 */
@Data
@TableName("yb_rule_execute")
public class RuleExecute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    private Integer maId;

    /**
     * 类型：数据字典：1换型时间、2标准产能
     */
    private Integer rpType;

    /**
     * 标准时速（小时）标准产能
     */
    private Integer capacity;

    /**
     * 更新换型时间(分钟)（当前默认换型）分钟
     */
    private Integer mouldTime;

    /**
     * 生成sql
     */
    private String generateSql;

    /**
     * 储备字段
     */
    private String reserve;

    /**
     * 状态 0停用1启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 选择条件内容json
     */
    private String condition;
}