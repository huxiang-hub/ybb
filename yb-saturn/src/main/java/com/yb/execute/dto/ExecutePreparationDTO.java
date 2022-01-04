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
package com.yb.execute.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yb.execute.entity.ExecutePreparation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 生产准备记录_yb_execute_preparation数据传输对象实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExecutePreparationDTO extends ExecutePreparation {
    private static final long serialVersionUID = 1L;

    private Integer maId;
    private String maName;
    private String odNo;
    private String event;   //
    private Integer dpId;
    private String dpName;
    private Integer count;   //保养换膜次数
    private Double duration;   //保养换膜总计时间
    //JsonFormat后台像前台取值，前台向后台取值
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createAt;

}
