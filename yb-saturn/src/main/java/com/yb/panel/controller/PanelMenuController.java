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
package com.yb.panel.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.panel.entity.PanelMenu;
import com.yb.panel.service.PanelMenuService;
import com.yb.panel.vo.PanelMenuVO;
import com.yb.panel.wrapper.PanelMenuWrapper;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.service.IProcessClassifyService;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.vo.PyModelVO;
import com.yb.process.wrapper.ProcessClassifyWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机台菜单_yb_panel_menu 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/plapi/panelmenu")
@Api(value = "机台菜单_yb_panel_menu", tags = "机台菜单_yb_panel_menu接口")
public class PanelMenuController extends BladeController {

    @Autowired
    private PanelMenuService panelMenuService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processClassify")
    public R<PanelMenu> detail(PanelMenu panelMenu) {
        PanelMenu detail = panelMenuService.getOne(Condition.getQueryWrapper(panelMenu));
        return R.data(PanelMenuWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序分类表_yb_process_classify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<IPage<PanelMenuVO>> list(PanelMenu panelMenu, Query query) {
        IPage<PanelMenu> pages = panelMenuService.page(Condition.getPage(query), Condition.getQueryWrapper(panelMenu));
        return R.data(PanelMenuWrapper.build().pageVO(pages));
    }

    /**
     * 新增 工序分类表_yb_process_classify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processClassify")
    public R save(@Valid @RequestBody PanelMenu panelMenu) {
        return R.status(panelMenuService.save(panelMenu));
    }

    /**
     * 修改 工序分类表_yb_process_classify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processClassify")
    public R update(@Valid @RequestBody PanelMenu panelMenu) {
        return R.status(panelMenuService.updateById(panelMenu));
    }

    /**
     * 新增或修改 工序分类表_yb_process_classify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processClassify")
    public R submit(@Valid @RequestBody PanelMenu panelMenu) {
        return R.status(panelMenuService.saveOrUpdate(panelMenu));
    }

    /**物理删除（删除就不可找回）**/
    /**
     * 删除 工序分类表_yb_process_classify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        boolean result = panelMenuService.removeByIds(Func.toIntList(ids));
        return R.status(result);
    }

    /**
     * 设备所有菜单 表yb_panel_menu
     * */
    @PostMapping("/panelMenuAll")
    @ApiOperation(value = "所有菜单")
    public R panelMenuAll(){
        List<PanelMenuVO> plist= new ArrayList<>();
        List<PanelMenuVO> panelMenuAlls=panelMenuService.getPanelMenuAll();
        if(panelMenuAlls!=null && panelMenuAlls.size()>0){
            for (int i=0;i<panelMenuAlls.size();i++){
                if(panelMenuAlls.get(i).getStatus()==0){
                    panelMenuAlls.get(i).setDisabled(true);
                }else{
                    panelMenuAlls.get(i).setDisabled(false);
                }
                plist.add(panelMenuAlls.get(i));
            }
        }
       return R.data(plist);
    }
}
