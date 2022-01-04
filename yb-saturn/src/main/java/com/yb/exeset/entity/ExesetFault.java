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
package com.yb.exeset.entity;

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
@TableName("yb_exeset_fault")
@ApiModel(value = "ExesetFault对象", description = "故障停机设置_yb_exeset_fault")
public class ExesetFault implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Integer maId;
    /**
     * 超过系统设置时间就做记录，但并不展示；后台可见，前台不可见
     */
    @ApiModelProperty(value = "超过系统设置时间就做记录，但并不展示；后台可见，前台不可见")
    private Integer syslimitTime;
    /**
     * 弹出限制设定（分钟）
     */
    @ApiModelProperty(value = "弹出限制设定（分钟）")
    private Integer limitTime;
    /**
     * 窗口消失时间设定（分钟）
     */
    @ApiModelProperty(value = "窗口消失时间设定（分钟）")
    private Integer disappear;
    /**
     * 弹出窗口1弹出0不弹出（默认弹出）
     */
    @ApiModelProperty(value = "弹出窗口1弹出0不弹出（默认弹出）")
    private Integer popup;
    /**
     * 消息发送接收人id|竖线分隔
     */
    @ApiModelProperty(value = "消息发送接收人id|竖线分隔")
    private String sendto;
    /**
     * 设置时间
     */
    @ApiModelProperty(value = "设置时间")
    private Date createAt;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private Integer usId;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    /**
     * 间隔时间
     */
    @ApiModelProperty(value = "间隔时间")
    private Integer recedelayTime;


}
