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
package com.yb.workbatch.vo;

import com.yb.workbatch.entity.WorkbatchModulus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 版数转化_yb_workbatch_modulus视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@ApiModel(value = "WorkbatchOrdlinkVO对象", description = "版数转化_yb_workbatch_modulus")
public class WorkbatchModulusVO extends WorkbatchModulus {
    private static final long serialVersionUID = 1L;

    /**
     * 转换后的部件名称
     */
    private  String afterPtName;
    /**
     * 转换后的部件No
     */
    private String afterPtNo;

    /**
     *
     */
    private String ExcelModulus;

}
