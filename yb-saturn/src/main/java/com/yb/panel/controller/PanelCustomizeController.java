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
import com.yb.machine.service.IMachineMainfoService;
import com.yb.panel.common.panelUtils;
import com.yb.panel.entity.PanelCustomize;
import com.yb.panel.entity.PanelMenu;
import com.yb.panel.mapper.PanelCustomizeMapper;
import com.yb.panel.request.PanelCustomizeRequest;
import com.yb.panel.service.PanelCustomizeService;
import com.yb.panel.service.PanelMenuService;
import com.yb.panel.vo.PanelCustomizeVO;
import com.yb.panel.vo.PanelMenuVO;
import com.yb.panel.wrapper.PanelCustomizeWrapper;
import com.yb.panel.wrapper.PanelMenuWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 机台自定义_yb_panel_customize 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/plapi/panelcustomize")
@Api(value = "机台自定义_yb_panel_customize", tags = "机台自定义_yb_panel_customize接口")
public class PanelCustomizeController extends BladeController {

    @Autowired
    private PanelCustomizeService panelCustomizeService;
    @Autowired
    private PanelMenuService panelMenuService;
    @Autowired
    PanelCustomizeMapper panelCustomizeMapper;
    @Autowired
    private IMachineMainfoService iMachineMainfoService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processClassify")
    public R<PanelCustomizeVO> detail(PanelCustomize panelCustomize) {
        PanelCustomize detail = panelCustomizeService.getOne(Condition.getQueryWrapper(panelCustomize));
        return R.data(PanelCustomizeWrapper.build().entityVO(detail));
    }

    /**
     * 分页 工序分类表_yb_process_classify
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入processClassify")
    public R<IPage<PanelCustomizeVO>> list(PanelCustomize panelCustomize, Query query) {
        IPage<PanelCustomize> pages = panelCustomizeService.page(Condition.getPage(query), Condition.getQueryWrapper(panelCustomize));
        return R.data(PanelCustomizeWrapper.build().pageVO(pages));
    }


    /**
     * 新增 工序分类表_yb_process_classify
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入processClassify")
    public R save(@Valid @RequestBody PanelCustomize panelCustomize) {
        return R.status(panelCustomizeService.save(panelCustomize));
    }

    /**
     * 修改 工序分类表_yb_process_classify
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processClassify")
    public R update(@Valid @RequestBody PanelCustomize panelCustomize) {

        return R.status(panelCustomizeService.updateById(panelCustomize));
    }

    /**
     * 新增或修改 工序分类表_yb_process_classify
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入processClassify")
    public R submit(@Valid @RequestBody PanelCustomize panelCustomize) {
        return R.status(panelCustomizeService.saveOrUpdate(panelCustomize));
    }

    /**物理删除（删除就不可找回）**/
    /**
     * 删除 工序分类表_yb_process_classify
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        boolean result = panelCustomizeService.removeByIds(Func.toIntList(ids));
        return R.status(result);
    }

    /**
     * 通过设备查询对应的权限
     */
    @GetMapping("/getListByMaId")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "设备查询权限", notes = "传入maId")
    @ResponseBody
    public R getListByMaId(Integer maId){
        Map<String, Object> map = new HashMap<>();
        map.put("ma_id", maId);
        map.put("status", 1);
        List<PanelCustomize> list = panelCustomizeService.listByMap(map);
        List<PanelMenuVO> menuList = new ArrayList<>();
        if (!Func.isEmpty(list)){
            list.stream().forEach( panelCustomize -> {
                //            查询菜单信息
                Map<String, Object> menuMap = new HashMap<>();
                menuMap.put("id", panelCustomize.getMuId());
                menuMap.put("status", 1);
                List<PanelMenu> menu = panelMenuService.listByMap(menuMap);
                menuMap.clear();
                if (!Func.isEmpty(menu)) {
                    menu.stream().forEach(me -> {
                        PanelMenuVO menuVO = PanelMenuWrapper.build().entityVO(me);
                        menuVO.setPanelId(panelCustomize.getId());
                        menuVO.setPanelPId(panelCustomize.getPId());
                        menuList.add(menuVO);
                    });
                }
            });
        }
        return R.data(panelUtils.listGetStree(menuList));
    }

    /**
     *
     * */
    @PostMapping("/getPanelMenu")
    @ApiOperation(value = "菜单")
    public R getPanelMenu(@RequestBody PanelCustomizeRequest request){
        List<PanelCustomizeVO> panelCustomizeVOList=new ArrayList<>();
        if(request!=null) {
            for (int i = 0; i < request.getMaList().size(); i++) {
                    PanelCustomizeVO panelCustomizeVO = new PanelCustomizeVO();
                    List<Integer> muIdList = panelCustomizeService.getMuId(request.getMaList().get(i).getValue());
                    panelCustomizeVO.setMaId(request.getMaList().get(i).getValue());
                    panelCustomizeVO.setMaName(request.getMaList().get(i).getLabel());
                    panelCustomizeVO.setPanelMenus(muIdList);
                    panelCustomizeVOList.add(panelCustomizeVO);
            }
        }
        return R.data(panelCustomizeVOList);
    }

    /**
     * 修改设备菜单
     * */
    @PostMapping("/updatePanelCustomize")
    @ApiOperation(value = "修改设备菜单")
    public R updatePanelCustomize(@RequestBody PanelCustomizeRequest request){
        String maName=null;
        if(request.getMaName().contains("(")) {
            maName = request.getMaName().substring(0, request.getMaName().indexOf("("));
        }else{
            maName=request.getMaName();
        }
        int maId=iMachineMainfoService.getMachins(maName).get(0).getId();
        List<PanelCustomize> pList=new ArrayList<>();
        List<Integer> menus=panelCustomizeService.getMuId(maId);
        if(menus.size()<request.getSelectMenu().size()){
            List<Integer> muIds=new ArrayList(request.getSelectMenu());
            muIds.removeAll(menus);
            for (int i=0;i<muIds.size();i++){
                PanelCustomize panelCustomize=new PanelCustomize();
                panelCustomize.setMaId(maId);
                panelCustomize.setMuId(muIds.get(i));
                panelCustomize.setStatus(1);
                pList.add(panelCustomize);
            }
            boolean flag=panelCustomizeService.panelMenuAdd(pList);
            if(flag){
                return R.success("修改/保存成功!");
            }
        }else {
            List<Integer> muIds=new ArrayList(menus);
            muIds.removeAll(request.getSelectMenu());
            if(muIds!=null && muIds.size()>0){
                for (int j=0;j<muIds.size();j++){
                    PanelCustomize panelCustomize=new PanelCustomize();
                    panelCustomize.setMaId(maId);
                    panelCustomize.setMuId(muIds.get(j));
                    pList.add(panelCustomize);
                }
            }
            boolean flag=panelCustomizeService.deteleMenu(pList);
            if(flag){
                return R.success("修改/保存成功!");
            }
        }
        return R.fail("修改/保存失败！");
    }

    /**
     * 批量修改
     * */
    @PostMapping("/batchEdit")
    @ApiOperation(value = "批量修改设备菜单")
    public R batchEdit(@RequestBody PanelCustomizeRequest request){
        boolean flag=panelCustomizeService.batchEdit(request);
        if(flag){
           return R.success("批量修改成功！");
        }
        return R.fail("批量修改失败！");
    }
}
