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
package com.yb.workbatch.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yb.workbatch.entity.*;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 生产排产表yb_workbatch_ordlink视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WorkbatchOrdlinkVO对象", description = "生产排产表yb_workbatch_ordlink")
public class WorkbatchOrdlinkShiftVO extends WorkbatchOrdlink {
    private static final long serialVersionUID = 1L;

    private Integer wfId;//排产
    private Integer sdId;//排产的id对象信息
    private Integer planTotalTime;//计划总时间
    private Integer mouldStay;//设备换型时间
    private Integer speed;
    private Integer wsId;//班次id信息
    private Date proBeginTime;//计划开始时间
    private Date proFinishTime;//计划完成时间
    private String ckName;//班次名称
    private Integer stayTime; //班次间隔分钟数量
}
