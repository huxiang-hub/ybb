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

import com.yb.machine.vo.MachineMainfoVO;
import com.yb.process.entity.ProcessClassify;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 工序分类表_yb_process_classify视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProcessClassifyVO对象", description = "工序分类表_yb_process_classify")
public class ProcessClassifyVO extends ProcessClassify implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 工序id
     */
    private Integer prId;
    /**
     * 部门Id
     */
    private Integer dpId;


    /**
     * 后台给前端组件的字段 对应
     */
    private Integer id;//id
    private Integer value; // pr_id
    private String label;// dpName
    /**
     * 四级设备
     */
    private List<MachineMainfoVO> children = new ArrayList<>();
}
