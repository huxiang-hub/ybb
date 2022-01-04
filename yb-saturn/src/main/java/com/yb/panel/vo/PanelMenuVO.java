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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.panel.entity.PanelMenu;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.prod.vo.ProdPartsinfoVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 机台菜单_yb_panel_menu视图实体类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PanelMenuVO对象", description = "机台菜单_yb_panel_menu")
public class PanelMenuVO extends PanelMenu implements Serializable {
    private static final long serialVersionUID = 1L;

//    权限关联表的id
    private Integer panelId;
//    权限关联表的父id
    private Integer panelPId;
    /**
     * 子孙节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<PanelMenuVO> children;

    public List<PanelMenuVO> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        return this.children;
    }

    private boolean disabled;//是否禁用
}
