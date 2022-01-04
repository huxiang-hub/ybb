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
package com.yb.supervise.vo;

import com.yb.supervise.entity.SuperviseSchedule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单进度表（进度表-执行）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseScheduleVO对象", description = "订单进度表（进度表-执行）")
public class SuperviseScheduleVO extends SuperviseSchedule {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private String odId;
    @ApiModelProperty("订单名称")
    private String odName;
    @ApiModelProperty("订单编号")
    private String odNo;
    @ApiModelProperty("批次编号")
    private String batchNo;
    @ApiModelProperty("部件名称")
    private String ptName;
    @ApiModelProperty("工序名称")
    private String prName;
    @ApiModelProperty("上报成本数量")
    private Integer orderNum;
    @ApiModelProperty("废品数量")
    private Integer wasteNum;
    @ApiModelProperty("合计(百分比)")
    private BigDecimal total;
    @ApiModelProperty("实时统计数量")
    private Integer nowNum;

    private String uuid;

    private List<SuperviseScheduleVO> children = new ArrayList<>();
}
