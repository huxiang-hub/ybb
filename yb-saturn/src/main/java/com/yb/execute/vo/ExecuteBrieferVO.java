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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yb.execute.entity.ExecuteBriefer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生产执行上报信息_yb_execute_briefer视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExecuteBrieferVO对象", description = "生产执行上报信息_yb_execute_briefer")
public class ExecuteBrieferVO extends ExecuteBriefer {
    private static final long serialVersionUID = 1L;
    private Integer odId;
    private String odName;
    private Integer examineStatus;
    private Integer odCount;
    private Integer completeNum;
    private String putTime;
    private Integer sdId;
    private String prName;
    private Integer sort;
    private String sdStatus;
    private Integer ebId;
    private Integer maId;
    private String machineName;
    private Integer usId;
    private String usrName;
    private Integer planNum;
    private Integer bfeId;
}
