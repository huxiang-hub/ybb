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

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备当前状态表boxinfo-视图视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@ApiModel(value = "DeptProductNumberVO对象", description = "DeptProductNumberVO对象")
public class DeptProductNumberVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*排产单总数量*/
    private Integer sdNum = 0;
    /*排产单生产完成数量*/
    private Integer sdNumber = 0;
    /*排产单未完成数量*/
    private Integer unSdNumber = 0;
    /*车间生产完成总数*/
    private Integer completeNums = 0;
    /*车间生产计划总数*/
    private Integer planNums = 0;
    /*车间未完成总数*/
    private Integer incompleteNums = 0;
}
