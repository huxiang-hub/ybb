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
package org.springblade.saturn.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础信息表_yb_base_staffext实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_base_staffext")
@ApiModel(value = "BaseStaffext对象", description = "基础信息表_yb_base_staffext")
public class BaseStaffext implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID")
    private Integer id;
    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private Integer sfId;
    /**
     * 性别 1男0女
     */
    @ApiModelProperty(value = "性别 1男0女")
    private Integer sex;
    /**
     * 学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6
     */
    @ApiModelProperty(value = "学历：初中及以下1、高中2、大专3、本科4、硕士5、博士及以上6")
    private Integer education;
    /**
     * 出生年月
     */
    @ApiModelProperty(value = "出生年月")
    private Date birthday;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idcard;
    /**
     * 身份证地址
     */
    @ApiModelProperty(value = "身份证地址")
    private String idaddr;
    /**
     * 籍贯（出生地）
     */
    @ApiModelProperty(value = "籍贯（出生地）")
    private String hometown;
    /**
     * 现居住址
     */
    @ApiModelProperty(value = "现居住址")
    private String curraddr;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;


}
