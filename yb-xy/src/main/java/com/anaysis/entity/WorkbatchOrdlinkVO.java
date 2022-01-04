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
 * 生产排产表yb_workbatch_ordlink实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_workbatch_ordlink")
@ApiModel(value = "WorkbatchOrdlink对象", description = "生产排产表yb_workbatch_ordlink")
public class WorkbatchOrdlinkVO extends WorkbatchOrdlink implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 工单NO
     */
    private String wbNo;
    /**
     * 设备UUID
     */
    private String maErp;
    /**
     * 工序UUID
     */
    private String prErp;
    /**
     * 产品UUID
     */
    private String pdErp;
    /**
     * 产品UUID(查部件)
     */
    private String pdErp1;
    /**
     * 产品类型UUID
     */
    private String typeErp;

    /**
     * 订货厂家UUID
     */
    private String cmErp;

    /**
     * 订单数量
     */
    private Integer odCount;
    /**
     * 用户UUID
     */
    private String usErp;
    /**
     * 部件UUID
     */
    private String partErp;
//    ==========================================扩展表START
    /**
     * 刀版编号
     */
    private String cutNo;


    /**
     * 版类
     */
    private String versionClass;
    /**
     * 总色数
     */
    private Integer colorNum;
    /**
     * 印色
     */
    private String paintColour;
    /**
     * 印刷文件编号
     */
    private String ctpNo;
    /**
     * 下资料袋时间CTP
     */
    private Date ctpTime;
    /**
     * 是否校板0不校板1校板
     */
    private Integer isCompare;
    /**
     * 看色描述
     */
    private String colorDesc;
    /**
     * 同印工艺1是0否
     */
    private String craftSame;
    /**
     * 推送日期
     */
    private Date pushDate;
    /**
     * 不装水油底纸1是0否
     */
    private Integer basePaper;
    /**
     * 同系列生产1是0否
     */
    private Integer sameSeries;
    /**
     * 含同印生产否1是0否
     */
    private Integer includePrint;
//    ==========================================扩展表END
    /**
     * ordoee的计划用时
     */
    private Integer planTime;
    /**
     * erp的小时产能，速度
     */
    private Integer erpSpeed;

    private float ptCD;
    private float ptKD;

    private String crmOdNo;
    //印刷机台
    private String printStation;
}
