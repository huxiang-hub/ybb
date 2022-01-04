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
package com.yb.order.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.actset.common.SaveCheckLogUnit;
import com.yb.actset.entity.ActsetCheckLog;
import com.yb.actset.service.IActsetCheckLogService;
import com.yb.actset.vo.CheckModelVO;
import com.yb.actset.vo.OrderCheckModelVO;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.order.entity.*;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderOrdinfoVO;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.order.wrapper.OrderOrdinfoWrapper;
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.entity.ProdPdmodel;
import com.yb.prod.service.IProdPartsinfoService;
import com.yb.prod.service.IProdPdinfoService;
import com.yb.prod.service.IProdPdmodelService;
import com.yb.prod.service.IProdProcelinkService;
import com.yb.prod.util.PartsinfoUtil;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.prod.vo.ProdPdmodelVO;
import com.yb.prod.vo.ProdProcelinkVO;
import com.yb.system.user.entity.SaUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rx.functions.FuncN;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.spring.web.json.Json;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 订单表_yb_order_ordinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/orderordinfo")
@Api(value = "订单表_yb_order_ordinfo", tags = "订单表_yb_order_ordinfo接口")
@Slf4j
public class OrderOrdinfoController extends BladeController {
//    private String uploadPath = "D:/temp/";
    private String uploadPath = "/home/img/";

//    private String url ="/img/";
    private String url = "/home/download/";

    @Autowired
    private IOrderOrdinfoService orderOrdinfoService;
    @Autowired
    private IBaseStaffinfoService baseStaffinfoService;
    @Autowired
    private IProdPdinfoService prodPdinfoService;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IOrderWorkbatchService orderWorkbatchService;
    @Autowired
    private IActsetCheckLogService actsetCheckLogService;
    @Autowired
    private IProdPdmodelService prodPdmodelService;
    @Autowired
    private IMaterProdlinkService materProdlinkService;
    //    部件编号增加的尾号
    private Integer ptNoTail = 1;
    /**
     * 详情(点击"查询模板"时)
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入orderOrdinfo")
    public R<OrderOrdinfoVO> detail(OrderOrdinfo orderOrdinfo) {
        OrderOrdinfoVO detail = orderOrdinfoService.getOneById(orderOrdinfo.getId());
        if (!Func.isEmpty(detail)){
            //        查询对应产品信息
            ProdPdinfoVO pdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
            if (!Func.isEmpty(pdinfoVO)) {
                detail.setPdId(pdinfoVO.getId());
                detail.setPdNo(pdinfoVO.getPdNo());
                detail.setPdName(pdinfoVO.getPdName());
            }
////        查询是否对应模板（通过产品编号）
//        ProdPdmodelVO pdmodelVO = prodPdmodelService.getOneProdPdmodelByPdNo(detail.getPdNo());
//        if (!Func.isEmpty(pdmodelVO)) {
//            detail.setPmId(pdmodelVO.getId());
//            detail.setPmName(pdmodelVO.getPdName());
//            detail.setPdNo(pdinfoVO.getPdNo());
//        }
//        查询其对应的批次和工序部件参数等
            ProdPdinfoVO prodPdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
            detail.setImageUrl(prodPdinfoVO.getImageUrl());
            detail.setProcePic(prodPdinfoVO.getProcePic());
            detail.setModelJson(prodPdinfoVO.getModelJson());
            //        查询部件集合
            List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(detail.getPdId(), 1);
//        查询所有部件的工序
            List<ProdProcelinkVO> prodProcelinkVOList = new ArrayList<>();
            //        物料集合
            List<MaterProdlinkVO> materProdlinkVOList = new ArrayList<>();
            for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
//            查询每个部件对应的工序集合
                List<ProdProcelinkVO> procelinkVOS = prodProcelinkService.list(partsinfoVo.getId());
                prodProcelinkVOList.addAll(procelinkVOS);
                partsinfoVo.setProdProcelinkVOList(procelinkVOS);
                //            查询部件对应的pt_classify与pt_ids
                partsinfoVo.setPtIdsList(ptIdsList(partsinfoVo));
                //                    查詢对应的部件物料
                List<MaterProdlinkVO> materProdlinkVO = materProdlinkService.selectMaterProdlinkVOListById(partsinfoVo.getId());
                materProdlinkVOList.addAll(materProdlinkVO);
            }
            detail.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
            detail.setProdProcelinkVOList(prodProcelinkVOList);
            detail.setMaterProdlinkVOList(materProdlinkVOList);
            return R.data(OrderOrdinfoWrapper.build().entityVO(detail));
        }else {
            return R.data(null);
        }

    }

    /**
     * 分页 订单表_yb_order_ordinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入orderOrdinfo")
    public R<IPage<OrderOrdinfoVO>> list(@RequestParam Map<String, Object> orderOrdinfo, Query query) {
        IPage<OrderOrdinfo> pages = orderOrdinfoService.page(Condition.getPage(query),
                Condition.getQueryWrapper(orderOrdinfo, OrderOrdinfo.class).orderByDesc("ORDER BY create_at DESC"));
        return R.data(OrderOrdinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 订单表_yb_order_ordinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入orderOrdinfo")
    public R<IPage<OrderOrdinfoVO>> page(OrderOrdinfoVO orderOrdinfo, Query query) {
        IPage<OrderOrdinfoVO> pages = orderOrdinfoService.selectOrderOrdinfoPage(Condition.getPage(query), orderOrdinfo);
        return R.data(pages);
    }

    /**
     * 新增 订单表_yb_order_ordinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @Transactional()
    @ApiOperation(value = "新增", notes = "传入orderOrdinfo")
    public R save(@Valid @RequestBody OrderOrdinfoVO orderOrdinfoVO) {
        try {
            //        获取创建人
            SaUser saUser = SaSecureUtil.getUser();//获取登录着的信息
            if (saUser == null){
                return R.fail("请登陆");
            }
            orderOrdinfoVO.setUsId(saUser.getUserId());
//        查询该订单编号是否已经存在
            Integer obnoIsExist = orderOrdinfoService.getObnoExist(orderOrdinfoVO.getOdNo());
            if (obnoIsExist != null){
                return R.fail("订单编号已存在");
            }
//            绑定选择模板的产品id
//            新增一个模板对应的产品
            ProdPdmodelVO prodPdmodel = prodPdmodelService.getOneProdPdmodelById(orderOrdinfoVO.getPmId());
//            查询模板相关
            selectAll(prodPdmodel);
//            保存产品
            ProdPdinfoVO pdinfoVO = new ProdPdinfoVO();
            pdinfoVO.setModelJson(prodPdmodel.getModelJson());
            pdinfoVO.setPdNo(prodPdinfoService.getPdNo(prodPdmodel.getPdNo().substring(0,2)+"%"));//生成
            pdinfoVO.setProcePic(prodPdmodel.getProcePic());
            pdinfoVO.setImageUrl(prodPdmodel.getImageUrl());
            pdinfoVO.setPcId(prodPdmodel.getPcId());
            pdinfoVO.setIsUsed(1);
            pdinfoVO.setPdName(prodPdmodel.getPdName());
            pdinfoVO.setModelJson(prodPdmodel.getModelJson());
            pdinfoVO.setMaterProdlinkVOList(prodPdmodel.getMaterProdlinkVOList());
            pdinfoVO.setProdPartsinfoVOList(prodPdmodel.getProdPartsinfoVOList());
            pdinfoVO.setProdProcelinkVOList(prodPdmodel.getProdProcelinkVOList());
            prodPdinfoService.saveOrUpdate(pdinfoVO);
//            保存模板相关存为产品
            JSONObject jsonObject = savePdinfoBeToProdPdinfoVO(pdinfoVO, 1);

            orderOrdinfoVO.setPdId(pdinfoVO.getId());
            orderOrdinfoVO.setProductionState(0);//默认0未执行
            orderOrdinfoVO.setCreateAt(new Date());
//            存入订单表
            orderOrdinfoService.save(orderOrdinfoVO);
            pdinfoVO.setModelJson(jsonObject.toString());
            prodPdinfoService.saveOrUpdate(pdinfoVO);
//            存入审核表
            return R.status(SaveCheckLogUnit.saveCheckLog("A","sale",orderOrdinfoVO.getId()));
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("订单新增失败");
        }

    }

    //    查询模板对应的信息
    public void selectAll(ProdPdmodelVO pdmodelVO){
        List<Node> nodeList = new ArrayList<>();
        if (pdmodelVO.getModelJson() != null && pdmodelVO.getModelJson() != ""){
            JSONObject jsonObject = JSONObject.fromObject(pdmodelVO.getModelJson());
            // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
            nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
        }

        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(pdmodelVO.getId(), 2);

        //        查询所有部件的工序
        List<ProdProcelinkVO> prodProcelinkVOList = new ArrayList<>();
//        物料集合
        List<MaterProdlinkVO> materProdlinkVOList = new ArrayList<>();
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            //            查询每个部件对应的工序集合
            List<ProdProcelinkVO> procelinkVOS = prodProcelinkService.list(partsinfoVo.getId());
            prodProcelinkVOList.addAll(procelinkVOS);
            partsinfoVo.setProdProcelinkVOList(procelinkVOS);
//            查询部件对应的pt_classify与pt_ids
            partsinfoVo.setPtIdsList(ptIdsList(partsinfoVo));
//                    查詢对应的部件物料
            List<MaterProdlinkVO> materProdlinkVO = materProdlinkService.selectMaterProdlinkVOListById(partsinfoVo.getId());
            materProdlinkVOList.addAll(materProdlinkVO);
//            查询部件的ptUuid
            if (pdmodelVO.getModelJson() != null && pdmodelVO.getModelJson() != ""){
                Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
                if (node.isPresent()){
                    partsinfoVo.setPtUuid(node.get().getPtUuid());
                }
            }
        }
        pdmodelVO.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
        pdmodelVO.setProdProcelinkVOList(prodProcelinkVOList);
        pdmodelVO.setMaterProdlinkVOList(materProdlinkVOList);
    }

    /**
     * 修改 订单表_yb_order_ordinfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入orderOrdinfo")
    public R update(@Valid @RequestBody OrderOrdinfo orderOrdinfo) {
        return R.status(orderOrdinfoService.updateById(orderOrdinfo));
    }

    /**
     * 修改 订单表_yb_order_ordinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入orderOrdinfo")
    public R submit(@Valid @RequestBody OrderOrdinfo orderOrdinfo) {
        return R.status(orderOrdinfoService.saveOrUpdate(orderOrdinfo));
    }


    /**
     * 删除 订单表_yb_order_ordinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//        先查询有没有进入审核流程
        for (Integer id : Func.toIntList(ids)){
            Map<String,Object> map = new HashMap<>();
            map.put("db_id", id);
            if (!Func.isEmpty(actsetCheckLogService.getBaseMapper().selectByMap(map))){
                return R.fail("删除失败，不可包含已审核订单");
            }
        }
//        如果没有可以删除，如果进入则不可删除
        return R.status(orderOrdinfoService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 根据订单的状态去查询生产状态   0 未执行  1 正在执行 2 已完成
     */
    @PostMapping("/getOrderByStatu")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "根据状态去查询", notes = "productionState")
    public R getOrderByStatu(@ApiParam(value = "通过状态查询", required = true) @RequestParam Integer productionState) {
        return R.data(orderOrdinfoService.getOrderByStatus(productionState));
    }


    @PostMapping("/seletOrderByCondition")
    @ApiOperationSupport(order = 9)
    @ApiOperation(value = "根据状态去查询", notes = "productionState")
    public R seletOrderByCondition(OrderOrdinfoVO orderOrdinfo) {

        return R.data(orderOrdinfoService.seletOrderByCondition(orderOrdinfo));
    }

    /**
     * 自定义分页 订单表_yb_order_ordinfo
     */
    @GetMapping("/pages")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入orderOrdinfo")
    public R<IPage<OrderOrdinfoVO>> pages(Query query,@ApiIgnore @RequestParam Map<String, Object> orderOrdinfoVO) {
        OrderOrdinfoVO orderOrdinfoVO1 = new OrderOrdinfoVO();
        if(!orderOrdinfoVO.isEmpty()){
            for (Map.Entry<String,Object> entry: orderOrdinfoVO.entrySet()){
                if(entry.getKey().equals("cmName")){
                    orderOrdinfoVO1.setCmName(""+entry.getValue());
                }
                if(entry.getKey().equals("odNo")){
                    orderOrdinfoVO1.setOdNo(""+entry.getValue());
                }
            }
        }
//        查询个人权限（是否为负责人）
        SaUser user = SaSecureUtil.getUser();//获取登录着的信息
//        查询个人信息(通过userId)是否为负责人
        BaseStaffinfoVO baseStaffinfoVO = baseStaffinfoService.getBaseStaffinfoByUsId(user.getUserId());
        IPage<OrderOrdinfoVO> pages = null;
        if (baseStaffinfoVO.getIsManage() != null &&baseStaffinfoVO.getIsManage() == 1){
            pages = orderOrdinfoService.selectOrderOrdinfoPages(Condition.getPage(query), orderOrdinfoVO1);
        }else{
            orderOrdinfoVO1.setUsId(baseStaffinfoVO.getUserId());
            pages = orderOrdinfoService.selectOrderOrdinfoPagesByUserId(Condition.getPage(query), orderOrdinfoVO1);
        }
//        查询整个流程审核结果
        if(pages.getRecords() != null){
            for(OrderOrdinfoVO ordinfoVO : pages.getRecords()){
                ActsetCheckLog actsetCheckLog = actsetCheckLogService.getMoreNewCheckLog(ordinfoVO.getId());
                if (actsetCheckLog != null){
                    ordinfoVO.setOrdStatus(actsetCheckLog.getStatus());
                    /**秦博加的,冲突别删除*/
                    ordinfoVO.setLogId(actsetCheckLog.getId());//审核审核记录表的Id
                    /**#####################################*/
                }
            }
        }
        return R.data(pages);
    }

    /**
     * 详情(点击查询批次时)
     */
    @GetMapping("/detailBatch")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入orderOrdinfo")
    public R<OrderOrdinfoVO> detailBatch(OrderOrdinfo orderOrdinfo) {
        OrderOrdinfoVO detail = orderOrdinfoService.getOneById(orderOrdinfo.getId());
        //        查询对应产品信息
        ProdPdinfoVO pdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
        if (!Func.isEmpty(pdinfoVO)) {
            detail.setPdId(pdinfoVO.getId());
            detail.setPdName(pdinfoVO.getPdName());
            detail.setPdNo(pdinfoVO.getPdNo());
        }
//        //        查询是否对应模板（通过产品编号）
        ProdPdmodelVO pdmodelVO = prodPdmodelService.getOneProdPdmodelByPdNo(detail.getPdNo());
        if (!Func.isEmpty(pdmodelVO)) {
            detail.setPmId(pdmodelVO.getId());
            detail.setPmName(pdmodelVO.getPdName());
        }
        /**秦博加的，冲突别删除**/
        //查找出对应的审核日志表Id
        ActsetCheckLog actsetCheckLog = actsetCheckLogService.getMoreNewCheckLog(detail.getId());
        if(!Func.isEmpty(actsetCheckLog)) {
            detail.setLogId(actsetCheckLog.getId());
            /**#####################################################*/
        }
//        查询其对应的批次

        List<OrderWorkbatchVO> list =  orderWorkbatchService.selectOrderWorkbatchListByOdno(detail.getOdNo());
        detail.setBatchList(list);
        return R.data(OrderOrdinfoWrapper.build().entityVO(detail));
    }

    /**
     * 详情("工艺修改"时)
     */
    @GetMapping("/detailCraft")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入orderOrdinfo")
    public R<OrderOrdinfoVO> detailCraft(OrderOrdinfo orderOrdinfo) {
        OrderOrdinfoVO detail = orderOrdinfoService.getOneById(orderOrdinfo.getId());
        //        查询对应产品信息
        ProdPdinfoVO pdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
        detail.setPdId(pdinfoVO.getId());
        detail.setPdNo(pdinfoVO.getPdNo());
        detail.setPdName(pdinfoVO.getPdName());
        List<Node> nodeList = new ArrayList<>();
        if (pdinfoVO.getModelJson() != null && pdinfoVO.getModelJson() != ""){
            JSONObject jsonObject = JSONObject.fromObject(pdinfoVO.getModelJson());
//         使用JSONObject.toBean(jsonObject, beanClass, classMap)
            nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
        }
        //        查询是否对应模板（通过产品编号）
        ProdPdmodelVO pdmodelVO = prodPdmodelService.getOneProdPdmodelByPdNo(detail.getPdNo());
        if (!Func.isEmpty(pdmodelVO)) {
            detail.setPmId(pdmodelVO.getId());
            detail.setPmName(pdmodelVO.getPdName());
        }
        //        查询其对应的批次和工序部件参数等
        ProdPdinfoVO prodPdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(detail.getPdId());
        detail.setImageUrl(prodPdinfoVO.getImageUrl());
        detail.setProcePic(prodPdinfoVO.getProcePic());
        detail.setModelJson(prodPdinfoVO.getModelJson());
//        //        查询对应的模板id
//        ProdPdmodel prodPdmodel = prodPdmodelService.getOneProdPdmodelByPdNo(prodPdinfoVO.getPdNo());
//        if (!Func.isEmpty(prodPdmodel)){
//            detail.setPmId(prodPdmodel.getId());
//        }
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(detail.getPdId(), 1);

        //        查询所有部件的工序
        List<ProdProcelinkVO> prodProcelinkVOList = new ArrayList<>();
//        物料集合
        List<MaterProdlinkVO> materProdlinkVOList = new ArrayList<>();
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            //            查询每个部件对应的工序集合
            List<ProdProcelinkVO> procelinkVOS = prodProcelinkService.list(partsinfoVo.getId());
            prodProcelinkVOList.addAll(procelinkVOS);
            partsinfoVo.setProdProcelinkVOList(procelinkVOS);
//            查询部件对应的pt_classify与pt_ids
            partsinfoVo.setPtIdsList(ptIdsList(partsinfoVo));
//                    查詢对应的部件物料
            List<MaterProdlinkVO> materProdlinkVO = materProdlinkService.selectMaterProdlinkVOListById(partsinfoVo.getId());
            materProdlinkVOList.addAll(materProdlinkVO);
//            查询部件的ptUuid
            if (pdinfoVO.getModelJson() != null && pdinfoVO.getModelJson() != ""){
                Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
                if (node.isPresent()){
                    partsinfoVo.setPtUuid(node.get().getPtUuid());
                }
            }
        }
        /**秦博加的，冲突别删除**/
        //查找出对应的审核日志表Id
        ActsetCheckLog actsetCheckLog = actsetCheckLogService.getMoreNewCheckLog(detail.getId());
        if (!Func.isEmpty(actsetCheckLog)) {
            detail.setLogId(actsetCheckLog.getId());
            /**#####################################################*/
        }
        detail.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
        detail.setProdProcelinkVOList(prodProcelinkVOList);
        detail.setMaterProdlinkVOList(materProdlinkVOList);

        return R.data(OrderOrdinfoWrapper.build().entityVO(detail));
    }
    /**
     * 修改订单（工艺）
     * 读取前端的json获取部件相关工序物料，通过产品编号判断是否修改，是否需要保存
     */
    @PostMapping("/updateCraft")
    @ApiOperationSupport(order = 5)
    @Transactional
    @ApiOperation(value = "修改", notes = "传入orderOrdinfo与flag")
    public R updateCraft( @Valid @RequestBody OrderOrdinfoVO orderOrdinfoVO) {
//        查询所选模板
        try {
            if (orderOrdinfoVO.getFlag() != null && orderOrdinfoVO.getFlag() == 1){
//                保存模板时，验证此模板是否已经存在
                ProdPdmodelVO prodPdmodelVO = prodPdmodelService.getOneProdPdmodelByPdNo(orderOrdinfoVO.getPdNo());
                if (Func.isEmpty(prodPdmodelVO)){
                    return R.fail("此编号模板已存在！");
                }
            }
//            if(orderOrdinfoVO.getPmId() != null){
////                如果选择了模板
//                ProdPdmodelVO prodPdmodelVO = prodPdmodelService.getOneProdPdmodelById(orderOrdinfoVO.getPmId());
//                if (prodPdmodelVO.getPdNo().equals(orderOrdinfoVO.getPdNo())){
////            如果产品编号没有进行修改，承认使用的就是模板中的产品编号，修改订单中的产品id
//                    return R.data(orderOrdinfoService.saveOrUpdate(orderOrdinfoVO));
//                }
//            }
//            如果不相同，则默认增加了一个产品，验证产品编号是否有重复
//                ProdPdinfoVO prodPdinfoVO = prodPdinfoService.getProdPdinfoVoBypdNo(orderOrdinfoVO.getPdNo());
//                if (!Func.isEmpty(prodPdinfoVO)){
//                    return R.fail("修改失败，产品编号重复");
//                }
//            查询产品编号是否有修改
            ProdPdinfo pd = prodPdinfoService.getById(orderOrdinfoVO.getPdId());
            if (!pd.getPdNo().equals(orderOrdinfoVO.getPdNo())){
//                        验证是否重复
                ProdPdinfoVO VO = prodPdinfoService.getProdPdinfoVoBypdNo(orderOrdinfoVO.getPdNo());
                if (!Func.isEmpty(VO)){
                    return R.fail("修改失败，产品编号重复");
                }
            }
//                产品信息表中增加一个产品
            ProdPdinfoVO pdinfoVO = new ProdPdinfoVO();
//                获取修改后的名字与编号
            pdinfoVO.setId(orderOrdinfoVO.getPdId());
            pdinfoVO.setPdName(orderOrdinfoVO.getPdName());
            pdinfoVO.setPdNo(orderOrdinfoVO.getPdNo());
            pdinfoVO.setIsUsed(1);
            pdinfoVO.setImageUrl(orderOrdinfoVO.getImageUrl());
            pdinfoVO.setProcePic(orderOrdinfoVO.getProcePic());
            pdinfoVO.setModelJson(orderOrdinfoVO.getModelJson());
//                    存入产品类型
            pdinfoVO.setPcId(Integer.parseInt(orderOrdinfoVO.getPdNo().substring(0,2)));
////                修改产品信息表，获取产品id（pdId）
            prodPdinfoService.saveOrUpdate(pdinfoVO);
//                    orderOrdinfoVO.setPdId(pdinfoVO.getId());
//            删除产品对应部件对应的物料
            materProdlinkService.removeByPdIdAndPdType(pdinfoVO.getId(), 1);
//            删除产品对应部件对应的工序
            prodProcelinkService.removeByPdIdAndPdType(pdinfoVO.getId(), 1);
//            删除产品对应部件
            prodPartsinfoService.removeByPdIdAndPdType(pdinfoVO.getId(), 1);
////                存入产品相关
            JSONObject jsonObject = savePdinfoBeToOrderOrd(orderOrdinfoVO, pdinfoVO);

            pdinfoVO.setModelJson(jsonObject.toString());
            prodPdinfoService.saveOrUpdate(pdinfoVO);
//                    判断是否保存模板
            if (orderOrdinfoVO.getFlag() != null && orderOrdinfoVO.getFlag() == 1){
                ProdPdmodelVO prodPdmodel = new ProdPdmodelVO();
                prodPdmodel.setIsUsed(1);
                prodPdmodel.setPdNo(pdinfoVO.getPdNo());
                prodPdmodel.setPdName(pdinfoVO.getPdName());
                prodPdmodel.setImageUrl(pdinfoVO.getImageUrl());
                prodPdmodel.setModelJson(pdinfoVO.getModelJson());
                prodPdmodel.setPcId(pdinfoVO.getPcId());
                prodPdmodel.setProcePic(pdinfoVO.getProcePic());
                prodPdmodelService.save(prodPdmodel);

                prodPdmodel.setMaterProdlinkVOList(orderOrdinfoVO.getMaterProdlinkVOList());
                prodPdmodel.setProdProcelinkVOList(orderOrdinfoVO.getProdProcelinkVOList());
                prodPdmodel.setProdPartsinfoVOList(orderOrdinfoVO.getProdPartsinfoVOList());
                JSONObject jsonObjectPdModel = savePdinfoBeToProdPdmodelVO(prodPdmodel, 2);

                prodPdmodel.setModelJson(jsonObjectPdModel.toString());

                prodPdmodelService.saveOrUpdate(prodPdmodel);
            }
            return R.data("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("修改失败");
        }
    }
    //保存订单的产品信息
    public JSONObject savePdinfoBeToOrderOrd(OrderOrdinfoVO orderOrdinfoVO, ProdPdinfoVO pdinfoVO) throws Exception{
        //                    orderOrdinfoService.saveOrUpdate(orderOrdinfoVO);
        JSONObject jsonObject = JSONObject.fromObject(orderOrdinfoVO.getModelJson());
        net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray("nodeList");
        // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
        List<Node> nodes = (List<Node>) JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
//                    保存父部件
        savePprodProce(orderOrdinfoVO.getProdPartsinfoVOList(),pdinfoVO.getId(),orderOrdinfoVO.getPdNo(),null, 1);
//                      保存子部件以及对应的原料工序信息
        saveProdProce(orderOrdinfoVO.getProdPartsinfoVOList(), orderOrdinfoVO.getProdPartsinfoVOList(), nodes, pdinfoVO.getId(), orderOrdinfoVO.getPdNo(),null, 1);
        for(int i = 0 ; i < jsonArray.size() - 1; i++){
            if(jsonArray.getJSONObject(i).get("type").equals(2) && jsonArray.getJSONObject(i).get("uuid").equals(jsonArray.getJSONObject(i).get("reUuid"))){
                int finalI = i;
                Optional<Node> nd = nodes.stream().filter(node -> node.getUuid().equals(jsonArray.getJSONObject(finalI).get("uuid"))).findFirst();
                if (nd.isPresent()){
                    jsonArray.getJSONObject(i).put("id",nd.get().getId());
                }
            }
        }
        jsonObject.put("nodeList",jsonArray);
        return jsonObject;
    }
    //保存订单下的产品的模板
    public JSONObject savePdinfoBeToProdPdmodelVO(ProdPdmodelVO pdmodelVO, Integer pdType) throws Exception{
        //                    orderOrdinfoService.saveOrUpdate(orderOrdinfoVO);
        JSONObject jsonObject = JSONObject.fromObject(pdmodelVO.getModelJson());
        net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray("nodeList");
        // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
        List<Node> nodes = (List<Node>) JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
//                    保存父部件
        savePprodProce(pdmodelVO.getProdPartsinfoVOList(),pdmodelVO.getId(),pdmodelVO.getPdNo(),null, pdType);
//                      保存子部件以及对应的原料工序信息
        saveProdProce(pdmodelVO.getProdPartsinfoVOList(), pdmodelVO.getProdPartsinfoVOList(), nodes, pdmodelVO.getId(), pdmodelVO.getPdNo(),null, pdType);
        for(int i = 0 ; i < jsonArray.size() - 1; i++){
            if(jsonArray.getJSONObject(i).get("type").equals(2) && jsonArray.getJSONObject(i).get("uuid").equals(jsonArray.getJSONObject(i).get("reUuid"))){
                int finalI = i;
                Optional<Node> nd = nodes.stream().filter(node -> node.getUuid().equals(jsonArray.getJSONObject(finalI).get("uuid"))).findFirst();
                if (nd.isPresent()){
                    jsonArray.getJSONObject(i).put("id",nd.get().getId());
                }
            }
        }
        jsonObject.put("nodeList",jsonArray);
        return jsonObject;
    }
    //保存订单下的产品的模板
    public JSONObject savePdinfoBeToProdPdinfoVO(ProdPdinfoVO pdinfoVO, Integer pdType) throws Exception{
        //                    orderOrdinfoService.saveOrUpdate(orderOrdinfoVO);
        JSONObject jsonObject = JSONObject.fromObject(pdinfoVO.getModelJson());
        net.sf.json.JSONArray jsonArray = jsonObject.getJSONArray("nodeList");
        // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
        List<Node> nodes = (List<Node>) JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
//                    保存父部件
        savePprodProce(pdinfoVO.getProdPartsinfoVOList(),pdinfoVO.getId(),pdinfoVO.getPdNo(),null, pdType);
//                      保存子部件以及对应的原料工序信息
        saveProdProce(pdinfoVO.getProdPartsinfoVOList(), pdinfoVO.getProdPartsinfoVOList(), nodes, pdinfoVO.getId(), pdinfoVO.getPdNo(),null, pdType);
        for(int i = 0 ; i < jsonArray.size() - 1; i++){
            if(jsonArray.getJSONObject(i).get("type").equals(2) && jsonArray.getJSONObject(i).get("uuid").equals(jsonArray.getJSONObject(i).get("reUuid"))){
                int finalI = i;
                Optional<Node> nd = nodes.stream().filter(node -> node.getUuid().equals(jsonArray.getJSONObject(finalI).get("uuid"))).findFirst();
                if (nd.isPresent()){
                    jsonArray.getJSONObject(i).put("id",nd.get().getId());
                }
            }
        }
        jsonObject.put("nodeList",jsonArray);
        return jsonObject;
    }
    /**
     * 递归保存所有父部件（不保存子部件）
     * @param prodPartsinfoVoList
     * @param pdId
     * @param pdNo
     * @param pid
     * @throws Exception
     */
    private void savePprodProce(List<ProdPartsinfoVo> prodPartsinfoVoList, Integer pdId, String pdNo , Integer pid , Integer pdType)  throws Exception{
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            if(partsinfoVo.getChildren()!= null && partsinfoVo.getChildren().size() > 0){
                //        保存父部件
                partsinfoVo.setId(null);
                partsinfoVo.setPid(pid);
                partsinfoVo.setPdId(pdId);
                partsinfoVo.setPdType(pdType);
                partsinfoVo.setPtNo(pdNo + "_" + ptNoTail++);
                prodPartsinfoService.save(partsinfoVo);
                savePprodProce(partsinfoVo.getChildren(),  pdId, pdNo, partsinfoVo.getId(), pdType);
            }else{
                continue;
            }
        }
    }

    /**
     * 保存部件以及对应的工序和原料
     * @param prodPartsinfoVoList
     * @param nodeList
     * @param pdId
     * @param pdNo
     * @param pid
     * @throws Exception
     */
    private void saveProdProce(List<ProdPartsinfoVo> allProdPartsinfoVoList, List<ProdPartsinfoVo> prodPartsinfoVoList, List<Node> nodeList, Integer pdId, String pdNo , Integer pid, Integer pdType) throws Exception{
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            if(partsinfoVo.getChildren() != null && partsinfoVo.getChildren().size() > 0){
//                继续递归出最底层子部件
                saveProdProce(allProdPartsinfoVoList,partsinfoVo.getChildren(), nodeList, pdId, pdNo, partsinfoVo.getId(), pdType);
            }else{
//                为最底层子部件时有物料和工序，
//                找到在nodeList中对应的部件（取reUuid）
                Optional<Node> node = nodeList.stream().filter(nodes -> (nodes.getType() == 2)).filter(nodes -> nodes.getPtUuid().equals(partsinfoVo.getPtUuid())).findFirst();
//                找出对应ptUuin的有原料和工序的部件
                Optional<Node> nd = nodeList.stream().filter(no -> no.getUuid().equals(node.get().getReUuid())).findFirst();
//                判断部件组成类型
                partsinfoVo.setPtIds(null);
                if(nd.get().getPtClassify() != 1){
//                    查询ids对应的部件是否已经保存
                    String [] idUuid = nd.get().getPtIds().split("\\|");
                    for (String Uuid : idUuid){
//                        通过ids中的uuid查询到组成部件
                        Optional<Node> uuidNode = nodeList.stream().filter(no -> no.getUuid().equals(Uuid)).findFirst();
//                        是否保存
                        ProdPartsinfoVo prodPartsinfoVo = selectAllbyPtUuid(allProdPartsinfoVoList, uuidNode.get());
                        if (prodPartsinfoVo.getId() == null){
//                            保存组成部件
                            saveProd(allProdPartsinfoVoList, prodPartsinfoVoList, prodPartsinfoVo, nodeList, pdId, pdNo, null, 1);
                        }
                        if (partsinfoVo.getPtIds() == null || partsinfoVo.getPtIds() == null){
                            partsinfoVo.setPtIds(prodPartsinfoVo.getId()+"");
                        }else {
                            partsinfoVo.setPtIds(partsinfoVo.getPtIds()+"|"+prodPartsinfoVo.getId());
                        }
                    }
                }
//                查询对应部件的原料和工序节点
//                部件和对应的原料
                //        保存部件
                partsinfoVo.setPid(pid);
                partsinfoVo.setPdId(pdId);
                partsinfoVo.setPdType(pdType);
                partsinfoVo.setPtClassify(nd.get().getPtClassify());
                partsinfoVo.setPtNo(pdNo + "_" + ptNoTail++);
                prodPartsinfoService.save(partsinfoVo);
                nd.get().setId(partsinfoVo.getId());
//                    有物料参与的部件
                Stream<Node>  nodeStream = nodeList.stream().filter(nodes -> (nodes.getType() == 1)).filter(nodes -> nodes.getPtUuid().equals(nd.get().getUuid()));
//                对应部件的工序
                Stream<Node>  nodeStreamPr = nodeList.stream().filter(nodes -> (nodes.getType() == 3)).filter(nodes -> nodes.getPtUuid().equals(nd.get().getUuid()));
//                循环保存
                    nodeStream.filter(par-> par != null).forEach(par -> saveProcelink(par, partsinfoVo, pdId, pid, pdNo));
//                循环保存
                    nodeStreamPr.filter(par-> par != null).forEach(part -> saveProdProcelink(part, partsinfoVo));
//                保存完成后进行下次循环
                continue;
            }
        }
    }
    /**
     * 验证是否可保存
     * @param allProdPartsinfoVoList
     * @param node
     * @return
     */
    private ProdPartsinfoVo selectAllbyPtUuid(List<ProdPartsinfoVo> allProdPartsinfoVoList, Node node){
        for (ProdPartsinfoVo partsinfoVo : allProdPartsinfoVoList){
            if (partsinfoVo.getChildren().size() > 0){
                ProdPartsinfoVo prodPartsinfoVo = selectAllbyPtUuid(partsinfoVo.getChildren(), node);
                if (prodPartsinfoVo != null){
                    return prodPartsinfoVo;
                }
            }
            if (node.getPtUuid().equals(partsinfoVo.getPtUuid())){
                return partsinfoVo;
            }else{
                continue;
            }
        }
        return null;
    }

    /**
     * 保存子部件和部件对应的原料
     * @param node
     * @param prodPartsinfoVo
     */
    private void saveProcelink(Node node, ProdPartsinfoVo prodPartsinfoVo, Integer pdId, Integer pid, String pdNo){
//        保存部件对应的原料
        MaterProdlinkVO materProdlinkVO = new MaterProdlinkVO();
        materProdlinkVO.setPdId(prodPartsinfoVo.getId());
        materProdlinkVO.setMlId(node.getId());
        materProdlinkVO.setMtNum(node.getMlNum());
//        materProdlinkVO.setPdNum();
        materProdlinkVO.setMaterial(node.getMaterial());
        materProdlinkVO.setModel(node.getModel());
        materProdlinkVO.setSpecification(node.getSpecification());
        materProdlinkVO.setSize(node.getSize());
        materProdlinkService.save(materProdlinkVO);
    }

    /**
     * 保存部件对应的工序
     * @param node
     * @param prodPartsinfoVo
     */
    private void saveProdProcelink(Node node, ProdPartsinfoVo prodPartsinfoVo){
        ProdProcelinkVO prodProcelinkVO = new ProdProcelinkVO();
        prodProcelinkVO.setPtId(prodPartsinfoVo.getId());
        prodProcelinkVO.setPrId(node.getId());
        prodProcelinkVO.setDiffLevel(Double.parseDouble(node.getDifficulty()));
        prodProcelinkVO.setWasteRate(Double.parseDouble(node.getLossRate()));
        prodProcelinkVO.setRemarks(node.getRemarks());
        prodProcelinkVO.setPrParam(node.getPrParam());
        prodProcelinkVO.setSortNum(node.getSort());
//        prodProcelinkVO.setPoint();
        prodProcelinkService.save(prodProcelinkVO);
    }
    /**
     * 保存某一组成部件（子部件保存）
     * @param prodPartsinfoVoList
     * @param prodPartsinfoVo
     * @param nodeList
     * @param pdId
     * @param pdNo
     * @return
     */
    private void saveProd(List<ProdPartsinfoVo> allProdPartsinfoVoList, List<ProdPartsinfoVo> prodPartsinfoVoList, ProdPartsinfoVo prodPartsinfoVo, List<Node> nodeList, Integer pdId, String pdNo,Integer pid, Integer pdType) throws Exception{
        for(ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            if (partsinfoVo.getChildren().size() == 0){
                if (partsinfoVo.getPtUuid().equals(prodPartsinfoVo.getPtUuid())){
                    //                找到在nodeList中对应的部件（取reUuid）
                    Optional<Node> node = nodeList.stream().filter(nodes -> (nodes.getType() == 2)).filter(nodes -> nodes.getPtUuid().equals(partsinfoVo.getPtUuid())).findFirst();
                    //                找出对应ptUuin的有原料和工序的部件
                    Optional<Node> nd = nodeList.stream().filter(no -> no.getUuid().equals(node.get().getReUuid())).findFirst();
//                判断部件组成类型
                    if(nd.get().getPtClassify() != 1){
//                    查询ids对应的部件是否已经保存
                        String [] idUuid = nd.get().getPtIds().split("\\|");
                        for (String Uuid : idUuid){
//                        通过ids中的uuid查询到组成部件
                            Optional<Node> uuidNode = nodeList.stream().filter(no -> no.getUuid().equals(Uuid)).findFirst();
//                        是否保存
                            ProdPartsinfoVo prodPartsinfo = selectAllbyPtUuid(allProdPartsinfoVoList, uuidNode.get());
                            if (prodPartsinfo.getId() == null){
//                            保存组成部件
                                saveProd(allProdPartsinfoVoList, prodPartsinfoVoList, prodPartsinfo, nodeList, pdId, pdNo, null, pdType);
                            }
                        }
                    }
//        保存部件
                    partsinfoVo.setPid(pid);
                    partsinfoVo.setPdId(pdId);
                    partsinfoVo.setPtClassify(nd.get().getPtClassify());
                    partsinfoVo.setPtNo(pdNo + "_" + ptNoTail++);
                    prodPartsinfoService.save(partsinfoVo);
                    nd.get().setId(partsinfoVo.getId());
//                对应部件的原料
                    Stream<Node>  nodeStream = nodeList.stream().filter(nodes -> nodes.getType() == 1).filter(nodes -> nodes.getPtUuid().equals(prodPartsinfoVo.getPtUuid()));
//                对应部件的工序
                    Stream<Node>  nodeStreamPr = nodeList.stream().filter(nodes -> nodes.getType() == 1).filter(nodes -> nodes.getPtUuid().equals(prodPartsinfoVo.getPtUuid()));

//                循环保存
                        //                对应部件的原料
                        nodeStream.filter(par-> par != null).forEach(par -> saveProcelink(par, partsinfoVo, pdId, pid, pdNo));
//                循环保存
                        //                对应部件的工序
                        nodeStreamPr.filter(par-> par != null).forEach(part -> saveProdProcelink(part, partsinfoVo));

                    continue;
                }
            }else{
                saveProd(allProdPartsinfoVoList, partsinfoVo.getChildren(),prodPartsinfoVo, nodeList, pdId, pdNo, partsinfoVo.getId(), pdType);
            }
        }
    }


    /**
     * 查询对应部件+部件的部件集合
     */
    public List<ProdPartsinfo> ptIdsList(ProdPartsinfoVo prodPartsinfoVo){
        List<ProdPartsinfo> list = new ArrayList<>();
        if(prodPartsinfoVo.getPtClassify() != null && prodPartsinfoVo.getPtClassify() == 2){
//                部件+部件（查询对应部件的集合）
            String[] ids = prodPartsinfoVo.getPtIds().split("\\|");
            for (String id : ids){
                list.add(prodPartsinfoService.getBaseMapper().selectById(Integer.parseInt(id)));
            }
        }
        return list;
    }

    /**
     * 查询所有订单对应的所有批次（有生产数据的）
     */
    @GetMapping("/listsAllOrder")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "询所有订单对应的所有批次", notes = "传入orderOrdinfo")
    public R<List<OrderOrdinfoVO>> listsAllOrder(@ApiIgnore @RequestParam Map<String, Object> map){
        OrderOrdinfoVO orderOrdinfoVO = new OrderOrdinfoVO();
        OrderWorkbatchVO orderWorkbatchVO = new OrderWorkbatchVO();
        if(!map.isEmpty()){
            for (Map.Entry<String,Object> entry: map.entrySet()){
                if(entry.getKey().equals("odName")){
                    orderOrdinfoVO.setOdName(""+entry.getValue());
                }
                if(entry.getKey().equals("batchNo")) {
//                    如果批次编号不为空
                    orderWorkbatchVO.setBatchNo(""+entry.getValue());
                }
            }
        }
//        查询正在执行中或者已经完成的订单
        List<OrderOrdinfoVO> list = orderOrdinfoService.listsAllOrder(orderOrdinfoVO);
//        查询其对应的批次
        for (OrderOrdinfoVO detail:list){
            detail.setBatchNo(detail.getOdName());
            orderWorkbatchVO.setOdId(detail.getId());
            List<OrderWorkbatchVO> workbatchVOS =  orderWorkbatchService.selectOrderWorkbatchList(orderWorkbatchVO);
            detail.setBatchList(workbatchVOS);
        }
        return R.data(list);
    }

//    /**
//    * 查询所有订单对应的所有批次（有生产数据的）
//    */
//    @PostMapping("/saveProcePic")
//    @ResponseBody
//    @ApiOperationSupport(order = 3)
//    @ApiOperation(value = "询所有订单对应的所有批次", notes = "传入orderOrdinfo")
//    public R saveProcePic(@RequestParam("file") MultipartFile file, HttpServletRequest request){
//        String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        //重新给文件取名字
//        String fileName = UUID.randomUUID()+"_"+System.currentTimeMillis()+"."+file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
//        File targetFile = new File(uploadPath);
//        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
//        if (!targetFile.exists()) {
//            if (!targetFile.mkdirs()) {
//                json.put("msg", "error");
//                return R.fail(json.toJSONString());
//            }
//        }
//        json.put("msg", "success");
//        File tempFile = new File(uploadPath, fileName);
//        //保存
//        try {
//            file.transferTo(tempFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//            json.put("msg", "error");
//            return R.fail(json.toJSONString());
//        }
//        json.put("filePath", host +url + fileName);
//        return R.data(json.getString("filePath"),"头像上传成功！");
//    }


    /**
     * 上传接口
     *
     * @param file    文件
     * @param request 请求
     * @return json
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String host = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        //重新给文件取名字
        String fileName = UUID.randomUUID()+""+(new Date()).getTime()+ "." + file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".") + 1);
        File targetFile = new File(uploadPath);
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        if (!targetFile.exists()) {
            if (!targetFile.mkdirs()) {
                json.put("msg", "error");
                return R.fail(json.toJSONString());
            }
        }
        json.put("msg", "success");
        File tempFile = new File(uploadPath, fileName);
        //保存
        try {
            file.transferTo(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "error");
            return R.fail(json.toJSONString());
        }
        json.put("filePath", host +"/satapi"+url + fileName);
        return R.data(json.getString("filePath"),"图片保存成功！");
    }
}
