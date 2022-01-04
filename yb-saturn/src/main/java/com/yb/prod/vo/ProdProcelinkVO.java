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

import com.yb.prod.entity.ProdProcelink;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品对应工序关联表视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ProdProcelinkVO对象", description = "产品对应工序关联表")
public class ProdProcelinkVO extends ProdProcelink {
    private static final long serialVersionUID = 1L;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String prName;
    /*部件名称*/
    private String ptName;
    /*工序分类名称*/
    private String pyName;
}
