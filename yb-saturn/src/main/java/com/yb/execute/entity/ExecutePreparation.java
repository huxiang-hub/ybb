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
package com.yb.execute.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 生产准备记录_yb_execute_preparation实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_preparation")
@ApiModel(value = "ExecutePreparation对象", description = "生产准备记录_yb_execute_preparation")
public class ExecutePreparation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 执行状态ID
     */
    @ApiModelProperty(value = "执行状态ID")
    private Integer exId;
    /**
     * 1、装版 2、调试3、保养
     */
    @ApiModelProperty(value = "1、装版 2、调试3、保养")
    private String readyType;
    /**
     * 状态1正常0异常
     */
    @ApiModelProperty(value = "状态1正常0异常")
    private Integer status;
    /**
     * 备注说明
     */
    @ApiModelProperty(value = "备注说明")
    private String remark;
    /**
     * 测试张数
     */
    @ApiModelProperty(value = "测试张数")
    private Integer testPaper;
    /**
     * 测试纸张修正值
     */
    @ApiModelProperty(value = "测试纸张修正值")
    private Integer usePaper;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date startAt;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date finishAt;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;
    /**
     * 开始数
     */
    @ApiModelProperty(value = "开始数")
    private Integer endNum;
    /**
     *结束
     */
    @ApiModelProperty(value = "结束")
    private Integer startNum;

}
