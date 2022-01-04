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
package org.springblade.saturn.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.saturn.entity.SuperviseExecute;

import java.util.Date;


/**
 * 设备清零日志表视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseExecutVO对象", description = "设备实时订单状态")
public class SuperviseExecuteVO extends SuperviseExecute {
    private static final long serialVersionUID = 1L;
    /**
     * 补充关联表查询  限制弹窗时间
     */
    private Integer limitTime; //设置限制时间

    /**
     * 补充关联表查询 限制记录时间
     */
    private Integer syslimitTime;

    /*补充关联表人员名称*/
    private String userName;
    /*补充关联表设备名称*/
    private String equipmentName;
    /*补充关联表已生产数*/
    private Integer completeNum;
    /*补充关联表订单名称*/
    private String odName;
    /*补充关联表订单编号*/
    private String odNo;
    /*补充关联表生产总数*/
    private Integer planNum;
    /*补充关联表截止时间*/
    private Date closeTime;
    /*补充关联表实际结束时间*/
    private Date endTime;
    /*补充关联表超期时间*/
    private Long exceedDate;
    /*补充关联表订单状态*/
    private String odStatus;
    /*补充关联表订单状态*/
    private String status;
}
