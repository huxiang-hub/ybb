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
package com.anaysis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工序表--租户的工序内容（可以依据行业模版同步）实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_process_workinfo")
@ApiModel(value = "ProcessWorkinfo对象", description = "工序表--租户的工序内容（可以依据行业模版同步）")
public class ProcessWorkinfoVO extends ProcessWorkinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工序分类编号
     */
    private String pyNum;
    /**
     * 工序分类名称
     */
    private String pyName;
}
