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
package com.yb.mater.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yb.mater.entity.MaterMtinfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 物料列表_yb_mater_matinfo视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "MaterMtinfoVO对象", description = "物料列表_yb_mater_matinfo")
public class MaterMtinfoVO extends MaterMtinfo {
    private static final long serialVersionUID = 1L;

    /**
     * 物联关联id
     */
    private Integer materBatchlinkId;

    /**
     * 物料数量
     */
    private Integer mtNum;


    /**
     * 入库提交时间-请求
     */
    private String instorageTime;

    /**
     * 入库实际时间
     */
    private Date inTime;

    /**
     * 入库状态
     */
    private Integer status;

    @JsonIgnore
    private Integer sdId;
}
