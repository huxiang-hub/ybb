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
package com.yb.panel.vo;


import com.yb.panel.entity.PanelCustomize;
import com.yb.panel.entity.PanelMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 机台自定义_yb_panel_customize视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PanelCustomizeVO对象", description = "机台自定义_yb_panel_customize")
public class PanelCustomizeVO extends PanelCustomize implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设备Id")
    private int maId;

    @ApiModelProperty("设备名称")
    private String maName;

    @ApiModelProperty("菜单Id")
    private List<Integer> panelMenus;

}
