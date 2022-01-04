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
package com.yb.maintain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备故障表分类_yb_machine_faultclassify实体类
 *
 * @author Blade
 * @since 2020-03-15
 */
@Data
@TableName("yb_maintain_faultclassify")
@ApiModel(value = "MaintainFaultclassify对象", description = "设备故障表分类_yb_machine_faultclassify")
public class MaintainFaultclassify implements Serializable {

    private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
    /**
     * 父节点
     */
    @ApiModelProperty(value = "父节点")
    private Integer pid;
    /**
     * 停机分类
     */
    @ApiModelProperty(value = "停机分类")
    private String fname;
    /**
     * 分类编码
     */
    @ApiModelProperty(value = "分类编码")
    private String fvalue;
//    /**
//     * 限制时间(分钟)
//     */
//    @ApiModelProperty(value = "限制时间(分钟)")
//    private Integer overTime;
//    /**
//     * 废品数量
//     */
//    @ApiModelProperty(value = "废品数量")
//    private Integer waste;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
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

    @TableField(exist = false)
    List<MaintainFaultclassify> childNodes = new ArrayList<>();  // 子菜单list

}
