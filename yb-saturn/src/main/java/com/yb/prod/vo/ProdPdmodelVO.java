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
package com.yb.prod.vo;

import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.prod.entity.ProdPdmodel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 产品模版信息（productmodel）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProdPdmodelVO对象", description = "产品模版信息（productmodel）")
public class ProdPdmodelVO extends ProdPdmodel {
    private static final long serialVersionUID = 1L;
    //        查询部件集合
    List<ProdPartsinfoVo> prodPartsinfoVOList;
    //        查询所有部件的工序
    List<ProdProcelinkVO> prodProcelinkVOList;
    //    查询对应的物料集合
    List<MaterProdlinkVO> materProdlinkVOList;
    /*产品分类名称*/
    private String clName;
    /*产品模板名称*/
    private String pdModelName;
}
