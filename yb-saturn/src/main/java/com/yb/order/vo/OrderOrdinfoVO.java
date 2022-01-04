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
package com.yb.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.order.entity.OrderOrdinfo;
import com.yb.order.entity.OrderWorkbatch;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.prod.vo.ProdProcelinkVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 订单表_yb_order_ordinfo视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderOrdinfoVO对象", description = "订单表_yb_order_ordinfo")
public class OrderOrdinfoVO extends OrderOrdinfo {
    private static final long serialVersionUID = 1L;
    /**
     * 订单名称
     */
    @ApiModelProperty(value = "订单名称")
    private String odName;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String odNum;
    /**
     * 订单数量
     */
    @ApiModelProperty(value = "订单数量")
    private Integer odCount;
    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNum;
    /**
     * 合同名称
     */
    @ApiModelProperty(value = "合同名称")
    private String contractName;
    /**
     * 预计废品
     */
    @ApiModelProperty(value = "预计废品")
    private Integer wasteCount;
    /**
     * 订货id
     */
    @ApiModelProperty(value = "订货厂家id")
    private Integer cmId;
    /**
     * 订货厂家缩写
     */
    @ApiModelProperty(value = "订货厂家缩写")
    private String cmShortname;
    /**
     * 订货厂家名称
     */
    @ApiModelProperty(value = "订货厂家名称")
    private String cmName;
    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date limitDate;
    /**
     * 审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过
     */
    @ApiModelProperty(value = "审核状态 -1草稿  0提交至技术中心  1 提交至工艺审核  2 审核中  3审核通过  ")
    private Integer auditStatus;
    /**
     * 生产状态   0 未执行  1 正在执行 2 已完成
     */
    @ApiModelProperty(value = "生产状态   0 未执行  1 正在执行 2 已完成")
    private Integer productionState;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    private String excellimitDate;
//    产品id
    private Integer pdId;
//      产品名称
    private String pdName;
//    产品编号
    private String pdNo;
//    产品类型
    private Integer pcId;
//    图片地址
    private String imageUrl;
//    图片地址
    private String procePic;
//    模型对象的记录
    private String modelJson;
//    创建人
    private String realName;
//     模板id
    private Integer pmId;
//     模板名称（选择模板的产品名称）
    private String pmName;
//    当前步骤的审核状态
    private Integer status;
    //    当前订单的审核状态
    private Integer ordStatus;
//    审核结果
    private String result;
//        查询部件集合
    List<ProdPartsinfoVo> prodPartsinfoVOList;
//        查询所有部件的工序（也用于修改部件工序时进行保存）
    List<ProdProcelinkVO> prodProcelinkVOList;
//        查询所有部件的原料
    List<MaterProdlinkVO> materProdlinkVOList;
//    临时存储订单名称
//    用于页面显示
    private String batchNo;
//    批次信息集合
    List<OrderWorkbatchVO> batchList;

    private Integer flag;
    /**
     * 审核表中的Id
     */
    private Integer logId;
}
