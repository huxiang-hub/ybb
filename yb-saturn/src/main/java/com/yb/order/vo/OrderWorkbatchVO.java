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
package com.yb.order.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.order.entity.OrderWorkbatch;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 作业批次_yb_order_workbatch视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonSerialize(nullsUsing = NullSerializer.class)
@ApiModel(value = "OrderWorkbatchVO对象", description = "作业批次_yb_order_workbatch")
public class OrderWorkbatchVO extends OrderWorkbatch {
    private static final long serialVersionUID = 1L;

    private Integer wbNum;
    private Integer wbWaste;
    private Integer wbExtraNum;
    private String indentor;
    private String odName;
    private String remark;//订单备注
    private String odNo;
    private Integer planNum;
    private String excelCloseTime;
    private Integer wasteTotal;
    /*已排产工序数量*/
    private Integer prNumber;
    /*排产id*/
    private Integer sdId;
}
