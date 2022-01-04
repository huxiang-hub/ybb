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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.prod.entity.ProdPartsinfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tool.node.INode;

import java.util.ArrayList;
import java.util.List;


/**
 * 产品信息（product）视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProdPartsinfoVO对象", description = "产品部件信息（product）")
public class ProdPartsinfoVo extends ProdPartsinfo {
    private static final long serialVersionUID = 1L;
    public ProdPartsinfoVo(){
        super();
    }
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 父节点ID
     */
    private Integer parentId;

    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ProdPartsinfoVo> children;

    public List<ProdPartsinfoVo> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

    List<ProcessWorkinfoVO> prNames;

    /**
     * 上级部件
     */
    private String parentName;

    //    某一部件的工序集合
    List<ProdProcelinkVO> prodProcelinkVOList;

    //    某一部件的物料
    List<MaterProdlinkVO> materProdlinkVOList;

    //部件下部件+部件的ids对象集合
    List<ProdPartsinfo> ptIdsList;

//    批次id
    private Integer wbId;

    //    部件对应的工单的上报信息集合
    private List<ExecuteBriefer> executeBrieferList;
//工艺修改时接收参数
    private String ptUuid;

    //部件类型
    private String ptTy;
}
