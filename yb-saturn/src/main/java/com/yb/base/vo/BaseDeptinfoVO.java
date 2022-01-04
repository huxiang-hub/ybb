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
package com.yb.base.vo;

import com.yb.base.entity.BaseDeptinfo;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.supervise.vo.DeptProductNumberVO;
import com.yb.supervise.vo.MaStatusNumberVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 部门结构_yb_ba_dept视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BaseDeptinfoVO对象", description = "部门结构_yb_ba_dept")
public class BaseDeptinfoVO extends BaseDeptinfo  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 一级部门   数据对应前端组件 字段
     */
    private String label;// dpName
    private Integer value; // id
    private Integer type; // 判断是部门还是设备 0部门 ,1设备
    /**
     * 部门对应车间，二级间
     */
//    private List<DeptModelVO> children;
    /**
     * 部门对应车间，二级间
     */
    private List<BaseDeptinfoVO> children;
//    /**
//     * 部门对应车间，二级间
//     */
//    private List<MachineMainfoVO> machineList;
    /*设备状态数量*/
    private List<MaStatusNumberVO> statusNumber;
    /*车间生产数量*/
    private DeptProductNumberVO deptProductNumberVO;
    /*设备总数量*/
    private Integer maCount;
    /*设备id*/
    private Integer maId;
}
