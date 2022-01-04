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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 上报审核表_yb_execute_examine实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_execute_examine")
@ApiModel(value = "ExecuteExamine对象", description = "修改确认表_yb_execute_examine")
public class ExecuteExamine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 日报id
     */
    @ApiModelProperty(value = "日报id")
    private Integer bfId;
    /**
     * 审核人id
     */
    @ApiModelProperty(value = "审核人id")
    private Integer exUserid;
    /**
     * 上报人id
     */
    @ApiModelProperty(value = "上报人id")
    private Integer rptUserid;
    /**
     * 上报时间
     */
    @ApiModelProperty(value = "上报时间")
    private Date rptTime;

    /**
     * 上报备注
     */
    @ApiModelProperty(value = "审核前数据，用竖线分隔|作业数|正品数|次品数|")
    private String dataBefore;

    @ApiModelProperty(value = "审核后数据，用竖线分隔|作业数|正品数|次品数|")
    private String dataAfter;
    /**
     * 上报备注
     */
    @ApiModelProperty(value = "上报备注；编写理由；图片信息")
    private String reprotMark;
    /**
     * 审核状态 0 未审核 1 通过
     */
    @ApiModelProperty(value = "1 通过")
    private Integer exStatus;

    @ApiModelProperty(value = "途径1、修改审核2、正式审核界面、3手机审核")
    private Integer exWay;

    @ApiModelProperty(value = "是否审核托盘（可选）")
    private Integer tyId;

//    @ApiModelProperty(value = "审核状态 0 未审核 1 通过 ")
//    private Integer examineStatus;

    /**
     * 历史数据
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "审核前库位编码")
    private String storeBefore;

    @ApiModelProperty(value = "审核后库位编码")
    private String storeAfter;

    /**
     * 2021-03-07手机端报废审核增加字段
     */
    @ApiModelProperty(value = "执行单id")
    private Integer exId;
    @ApiModelProperty(value = "修改类型1、盘点 2、报废、3机长")
    private String exMold;
    @ApiModelProperty(value = "审核图片上传（多图片）")
    private String exPics;
    @ApiModelProperty(value = "更新时间")
    private String updateAt;


}
