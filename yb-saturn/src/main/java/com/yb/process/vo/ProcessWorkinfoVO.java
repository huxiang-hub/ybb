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
package com.yb.process.vo;

import com.yb.machine.entity.MachineMainfo;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 工序表--租户的工序内容（可以依据行业模版同步）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProcessWorkinfoVO对象", description = "工序表--租户的工序内容（可以依据行业模版同步）")
public class ProcessWorkinfoVO extends ProcessWorkinfo {
    private static final long serialVersionUID = 1L;
    /**
     *工序名称Id
     */
    private Integer prId;
    /**
     *工序类型Id
     */
    private Integer pyId;
    /**
     *工序类型Id
     */
    private String pyName;
    /*工序难易程度*/
    private Double diffLevel;
    /*工序损耗率*/
    private Double wasteRate;
    /*工序下设备的生产状态*/
    private List<SuperviseBoxinfoVO> superviseBoxinfoVOS;

    private List<MachineMainfo> mainfos;

}
