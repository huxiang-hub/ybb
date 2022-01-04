/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户管理管理_yb_crm_customer实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_crm_customer")
@ApiModel(value = "CrmCustomer对象", description = "客户管理_yb_crm_customer")
public class CrmCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;
    /**
     * 客户编码（当前默认4个英文字母，如果不够的用V替代）
     */
    @ApiModelProperty(value = "客户编码（当前默认4个英文字母，如果不够的用V替代）")
    @TableField("cm_no")
    private String cmNo;
    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    @TableField("cm_name")
    private String cmName;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "缩写名")
    @TableField("cm_shortname")
    private String cmShortname;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company")
    private String company;
    /**
     * 状态1起草2提交(待审批)3同意4不同意
     */
    @ApiModelProperty(value = "社会唯一编号")
    @TableField("unique_no")
    private String uniqueNo;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "所在地址")
    @TableField("addr")
    private String addr;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("create_at")
    private Date createAt;

}
