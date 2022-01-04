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
package com.yb.machine.vo;

import com.yb.machine.entity.MachineMainfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 设备_yb_mach_mainfo视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MachineMainfoVO对象", description = "设备_yb_mach_mainfo")
public class MachineMainfoVO extends MachineMainfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer maId;
    private String mno;
    private String maName;
    private String dictValue;
    private Integer maStatus;

    @ApiModelProperty(value = "多个id选择查询")
    private List<Integer> idList;
    @ApiModelProperty(value = "多个设备类型")
    private List<Integer> maTypeList;
    private String ids;

    /**
     * 后台给前端组件的字段 对应
     */
    private Integer value; // maId
    private String label;// maName

    /**
     * 部门信息
     */
    private String dpName;
    private Integer dpId;
     /* 设备品牌
     */
    private String  brand;
    /**
     * 设备规格
     */
    private String  specs;
    /**
     * 设备型号
     */
    private String  model;
    /**
     * 设备图片
     */
    private String  image;
    /**
     *速度
     */
    private Integer speed;//标准时速

    private Integer prId;//工序id
    /**
     * 工序分类名称
     */
    private String pyName;
    /**
     * 主要工序名称
     */
    private String prName;

    /**
     * 换膜时间(分)
     */
    @ApiModelProperty(value = "换膜时间(分)")
    private Integer prepareTime;
}
