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
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * I.1	 产品部件表_yb_prod_partsinfo
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@TableName("yb_prod_partsinfo")
@ApiModel(value = "ProdPartsinfo对象", description = " 产品部件表（product）")
public class ProdPartsinfoVO extends ProdPartsinfo implements Serializable {

    private static final long serialVersionUID = 1L;
//父节点
    private Integer IDAPComp;
//子节点
    private Integer IDAComp;
//物料集合
    private List<MaterProdlinkVO> materProdlinks;
//工序集合
    private List<ProdProcelinkVO> prodProcelinks;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProdPartsinfoVO> children;

    public List<ProdPartsinfoVO> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

    private String ptSize;
}
