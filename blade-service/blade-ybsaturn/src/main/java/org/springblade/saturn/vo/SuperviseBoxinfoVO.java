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
package org.springblade.saturn.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.saturn.entity.SuperviseBoxinfo;

import java.util.Date;
import java.util.List;

/**
 * 设备当前状态表boxinfo-视图视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SuperviseBoxinfoVO对象", description = "设备当前状态表boxinfo-视图")
public class SuperviseBoxinfoVO extends SuperviseBoxinfo {
    private static final long serialVersionUID = 1L;
    /*订单id*/
    private Integer odId;
    /*设备名*/
    private String name;
    /*订单名称*/
    private String odName;
    /*批次编号*/
    private String batchNo;
    /*班次名称*/
    private String ckName;
    /*排产计划数量*/
    private Integer planNum;
    /*当前工单已生产数量*/
    private Integer currNum;
    /*'执行状态：人事A 生产准备B 正式生产C 结束生产D'*/
    private String exeStatus;
    /*当前订单生产人员*/
    List<String> usNames;
    /*当前订单生产人员ids*/
    String usIds;
    private Integer odCount;
    private Integer completeNum;
    private Integer unfinished;
    private Date limitDate;
    private Integer wasteNum;
    private Integer countNum;
    private List<Integer> maNums;

}
