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
package com.yb.execute.vo;

import com.yb.execute.entity.ExecuteWaste;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 质量检查废品表_yb_execute_waste视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteWasteVO对象", description = "质量检查废品表_yb_execute_waste")
public class ExecuteWasteVO extends ExecuteWaste {
    private static final long serialVersionUID = 1L;

    private Date startAt;
    private Date endAt;
    /**
     * 按数量质检
     */
    private Integer limitNum;
    /**
     * 按时间质检
     */
    private Integer limitTime;
    /**
     * 质检类型
     */
    private Integer model;
    /**
     * 质检人员名称
     */
    private String userName;
    /**
     * 订单编号
     */
    private String odNo;
    /**
     * 设备Id
     */
    private Integer maId;
    /**
     * 设备名称
     */
    private String macName;
}
