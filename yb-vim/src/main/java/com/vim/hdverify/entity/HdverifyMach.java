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
package com.vim.hdverify.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 故障停机设置_yb_exeset_fault实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_hdverify_mach")
@ApiModel(value = "yb_hdverify_mach", description = "yb_hdverify_mach")
public class HdverifyMach implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    @ApiModelProperty(value = "设备名字")
    private String maName;
    @ApiModelProperty(value = "ma_startnum")
    private Integer maStartnum;
    @ApiModelProperty(value = "设备结束计数")
    private Integer maEndnum;
    @ApiModelProperty(value = "计数差值")
    private Integer maDiff;
    @ApiModelProperty(value = "盒子流水号")
    private Integer bxId;
    @ApiModelProperty(value = "盒子的uuid")
    private String bxUuid;
    @ApiModelProperty(value = "盒子开始计数")
    private Integer bxStartnum;
    @ApiModelProperty(value = "盒子结束计数")
    private Integer bxEndnum;
    @ApiModelProperty(value = "计数差值")
    private Integer bxDiff;
    @ApiModelProperty(value = "计时开始")
    private Date startTime;
    @ApiModelProperty(value = "计时结束")
    private Date endTime;
    @ApiModelProperty(value = "间隔时间（秒单位）")
    private Integer stayTime;
    @ApiModelProperty(value = "误差率%")
    private Double diffRate;
    @ApiModelProperty(value = "操作人姓名")
    private String operator;
    @ApiModelProperty(value = "验证状态：1未开始、2进行、3结束")
    private Integer status;
    @ApiModelProperty(value = "创建时间")
    private Date createAt;










}
