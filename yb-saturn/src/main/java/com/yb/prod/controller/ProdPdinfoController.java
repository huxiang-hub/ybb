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
package com.yb.prod.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.mater.entity.MaterMtinfo;
import com.yb.mater.entity.MaterProdlink;
import com.yb.mater.service.IMaterClassfiyService;
import com.yb.mater.service.IMaterMtinfoService;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.order.entity.Node;
import com.yb.order.service.IOrderOrdinfoService;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.entity.ProcessClasslink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessClassifyService;
import com.yb.process.service.IProcessClasslinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdPartsinfo;
import com.yb.prod.entity.ProdPdinfo;
import com.yb.prod.entity.ProdProcelink;
import com.yb.prod.entity.json.JsonsRootBean;
import com.yb.prod.entity.json.Leftstyle;
import com.yb.prod.entity.json.Linelist;
import com.yb.prod.entity.json.Nodelist;
import com.yb.prod.service.*;
import com.yb.prod.util.PartsinfoUtil;
import com.yb.prod.vo.ProdPartsinfoVo;
import com.yb.prod.vo.ProdPdinfoVO;
import com.yb.prod.vo.ProdPdmodelVO;
import com.yb.prod.vo.ProdProcelinkVO;
import com.yb.prod.wrapper.ProdPdinfoWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 产品信息（product） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/prodpdinfo")
@Api(value = "产品信息（product）", tags = "产品信息（product）接口")
public class ProdPdinfoController extends BladeController {
    @Autowired
    private IProdPdinfoService prodPdinfoService;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IMaterProdlinkService materProdlinkService;
    @Autowired
    private IMaterMtinfoService materMtinfoService;
    @Autowired
    private IMaterClassfiyService materClassfiyService;
    @Autowired
    private IProdPdmodelService prodPdmodelService;
    @Autowired
    private IProdClassifyService prodClassifyService;
    @Autowired
    private IOrderOrdinfoService orderOrdinfoService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;
    @Autowired
    private IProcessClasslinkService processClasslinkService;
    @Autowired
    private IProcessClassifyService processClassifyService;
    //    部件编号增加的尾号
    private Integer ptNoTail = 1;

    //部件坐标
    private Integer ptLeft = 180;
    private Integer ptTop = 10;
    private Integer mtLeft = 30;
    private Integer mtTop = 10;
    private Integer prLeft = 330;
    private Integer prTop = 10;
    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入prodPdinfo")
    public R<ProdPdinfoVO> detail(ProdPdinfo prodPdinfo) {
        /*ProdPdinfo detail = prodPdinfoService.getOne(Condition.getQueryWrapper(prodPdinfo));*/
        ProdPdinfoVO detail = prodPdinfoService.selectPdInFoOne(prodPdinfo.getId());
        //查询相关信息
        selectAll(detail);
        if (detail.getModelJson() == null || detail.getModelJson() == ""){
            JsonsRootBean jsonsRootBean = new JsonsRootBean();
            jsonsRootBean.setName("");
            JsonsRootBean jsons = getPdinfoJSON(detail.getProdPartsinfoVOList(), jsonsRootBean);
            String modelJson =  JSONObject.fromObject(jsons).toString();
            System.out.println(modelJson);
            prodPdinfo.setModelJson(modelJson);
            detail.setModelJson(modelJson);
            prodPdinfoService.updateById(prodPdinfo);
            ptLeft = 180;
            ptTop = 10;
            mtLeft = 30;
            mtTop = 10;
            prLeft = 330;
            prTop = 10;
        }
        return R.data(detail);
    }

    /**
     * 分页 产品信息（product）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入prodPdinfo")
    public R<IPage<ProdPdinfoVO>> list(ProdPdinfo prodPdinfo, Query query) {
        IPage<ProdPdinfo> pages = prodPdinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(prodPdinfo));
        return R.data(ProdPdinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 产品信息（product）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入prodPdinfo")
    public R<IPage<ProdPdinfoVO>> page(ProdPdinfoVO prodPdinfo, Query query) {
        IPage<ProdPdinfoVO> pages = prodPdinfoService.selectProdPdinfoPage(Condition.getPage(query), prodPdinfo);
        return R.data(pages);
    }

    /**
     * 新增 产品信息（product）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @Transactional
    @ApiOperation(value = "新增", notes = "传入prodPdinfo")
    public R save(@Valid @RequestBody ProdPdinfoVO prodPdinfo) {
        try {
//            查询类别
            if (prodPdinfo.getPcId() == null){
                return R.fail("请选择产品类型");
            }
//            验证是否为修改
            if (prodPdinfo.getId() != null){
                //            查询类型编号
//                删除对应的所有工序等
                //            删除产品对应部件对应的物料
                materProdlinkService.removeByPdIdAndPdType(prodPdinfo.getId(), 1);
                //            删除产品对应部件对应的工序
                prodProcelinkService.removeByPdIdAndPdType(prodPdinfo.getId(), 1);
                //            删除产品对应部件
                prodPartsinfoService.removeByPdIdAndPdType(prodPdinfo.getId(), 1);
            }else{
                ProdClassify prodClassify = prodClassifyService.getById(prodPdinfo.getPcId());
                prodPdinfo.setPdNo(prodPdinfoService.getPdNo((prodClassify.getClassify()+"%")));//生成产品编号
            }
            //            保存模板相关存为产品
            prodPdinfoService.saveOrUpdate(prodPdinfo);
            JSONObject jsonObject = savePdinfoBeToProdPdinfoVO(prodPdinfo, 1);
            prodPdinfo.setModelJson(jsonObject.toString());
            prodPdinfoService.updateById(prodPdinfo);
            return R.status(prodPdinfoService.updateById(prodPdinfo));
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("产品保存失败");
        }
    }
    //产品的
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
                Stream<Node> nodeStream = nodeList.stream().filter(nodes -> (nodes.getType() == 1)).filter(nodes -> nodes.getPtUuid().equals(nd.get().getUuid()));
//                对应部件的工序
                Stream<Node>  nodeStreamPr = nodeList.stream().filter(nodes -> (nodes.getType() == 3)).filter(nodes -> nodes.getPtUuid().equals(nd.get().getUuid()));
//                循环保存
                    //                    有物料参与的部件
                    nodeStream.filter(par-> par != null).forEach(par -> saveProcelink(par, partsinfoVo, pdId, pid, pdNo));
//                循环保存
                    //                对应部件的工序
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
                        nodeStreamPr.filter(par-> par != null).forEach(par -> saveProdProcelink(par, partsinfoVo));
                    continue;
                }
            }else{
                saveProd(allProdPartsinfoVoList, partsinfoVo.getChildren(),prodPartsinfoVo, nodeList, pdId, pdNo, partsinfoVo.getId(), pdType);
            }
        }
    }
    /**
     * 修改 产品信息（product）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入prodPdinfo")
    public R update(@Valid @RequestBody ProdPdinfo prodPdinfo) {
        return R.status(prodPdinfoService.updateById(prodPdinfo));
    }

    /**
     * 转换为模板
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入prodPdinfo")
    public R submit(@Valid @RequestBody ProdPdinfoVO prodPdinfo) {
//        查询出此产品的相关信息
        ProdPdinfoVO pdinfoVO = prodPdinfoService.getProdPdinfoVoBypdId(prodPdinfo.getId());
        //查询相关信息
        selectAll(pdinfoVO);
        try {
            ProdPdmodelVO prodPdmodel = new ProdPdmodelVO();
            prodPdmodel.setIsUsed(1);
            prodPdmodel.setPdNo(pdinfoVO.getPdNo());
            prodPdmodel.setPdName(pdinfoVO.getPdName());
            prodPdmodel.setImageUrl(pdinfoVO.getImageUrl());
            prodPdmodel.setModelJson(pdinfoVO.getModelJson());
            prodPdmodel.setPcId(pdinfoVO.getPcId());
            prodPdmodel.setProcePic(pdinfoVO.getProcePic());
            prodPdmodelService.save(prodPdmodel);

            prodPdmodel.setMaterProdlinkVOList(pdinfoVO.getMaterProdlinkVOList());
            prodPdmodel.setProdProcelinkVOList(pdinfoVO.getProdProcelinkVOList());
            prodPdmodel.setProdPartsinfoVOList(pdinfoVO.getProdPartsinfoVOList());
//            保存模板相关信息
            JSONObject jsonObjectPdModel = savePdinfoBeToProdPdmodelVO(prodPdmodel, 2);

            prodPdmodel.setModelJson(jsonObjectPdModel.toString());
//            更新模板相关信息
            return R.status(prodPdmodelService.saveOrUpdate(prodPdmodel));
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("产品删除失败");
        }
    }

    /**
     * 查询产品相关信息
     * @param pdinfoVO
     */
    public void selectAll(ProdPdinfoVO pdinfoVO){
        List<Node> nodeList = new ArrayList<>();
        if (pdinfoVO.getModelJson() != null && pdinfoVO.getModelJson() != ""){
            JSONObject jsonObject = JSONObject.fromObject(pdinfoVO.getModelJson());
            // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
            nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
        }
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(pdinfoVO.getId(), 1);

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
            partsinfoVo.setMaterProdlinkVOList(materProdlinkVO);
//            查询部件的ptUuid
            if (pdinfoVO.getModelJson() != null && pdinfoVO.getModelJson() != ""){
                Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
                if (node.isPresent()){
                    partsinfoVo.setPtUuid(node.get().getPtUuid());
                }
            }
        }
        pdinfoVO.setMaterProdlinkVOList(materProdlinkVOList);
        pdinfoVO.setProdProcelinkVOList(prodProcelinkVOList);
        pdinfoVO.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
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
                if (nd.isPresent()) {
                    jsonArray.getJSONObject(i).put("id", nd.get().getId());
                }
            }
        }
        jsonObject.put("nodeList",jsonArray);
        return jsonObject;
    }

    /**
     * 查询对应部件+部件的部件集合
     * @param prodPartsinfoVo
     * @return
     */
    public List<ProdPartsinfo> ptIdsList(ProdPartsinfoVo prodPartsinfoVo){
        List<ProdPartsinfo> list = new ArrayList<>();
        String[] ids = new String[0];
        if(prodPartsinfoVo.getPtClassify() != null && prodPartsinfoVo.getPtClassify() == 2){
//                部件+部件（查询对应部件的集合）
            String ptIds = prodPartsinfoVo.getPtIds();
            if (StrUtil.isBlank(ptIds)) {
                return list;
            } else {
                ids = ptIds.split("\\|");
            }
            for (String id : ids){
                list.add(prodPartsinfoService.getBaseMapper().selectById(Integer.parseInt(id)));
            }
        }
        return list;
    }

    /**
     * 删除 产品信息（product）
     * @param ids
     * @return
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @Transactional
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        try {
            List<Integer> idList = Func.toIntList(ids);
            for (Integer id : idList) {
//                查询是否使用
                ProdPdinfo pdinfo = prodPdinfoService.getById(id);
                Map<String,Object> map = new HashMap<>();
                map.put("pd_id",id);
                if (!Func.isEmpty(orderOrdinfoService.listByMap(map))) {
                    return R.fail("编号"+pdinfo.getPdNo()+"产品已使用不可删除");
                }
    //            删除产品对应部件对应的物料
                materProdlinkService.removeByPdIdAndPdType(id, 1);
    //            删除产品对应部件对应的工序
                prodProcelinkService.removeByPdIdAndPdType(id, 1);
    //            删除产品对应部件
                prodPartsinfoService.removeByPdIdAndPdType(id, 1);
            }
            return R.status(prodPdinfoService.removeByIds(Func.toIntList(ids)));
        }catch (Exception e){
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("产品删除失败");
        }
    }
    /**
     * 删除 产品信息（product）
     */
    @RequestMapping("/selectePdinfoList")
    public R<IPage<ProdPdinfoVO>> selectePdinfoList(ProdPdinfoVO prodPdinfo, Query query) {
        Integer current = (query.getCurrent()-1) * query.getSize();
        IPage<ProdPdinfoVO> pages = prodPdinfoService.selectePdinfoList(prodPdinfo, current, query.getSize());
        return R.data(pages);
    }

    /**
     * 查询产品分类名
     * @return
     */
    @RequestMapping("/clName")
    public List<ProdClassify> clName() {
        return prodPdinfoService.selectClName();
    }

    /**
     * 查询所有产品信息
     * @return
     */
    @RequestMapping("/pdNames")
    public List<ProdPdinfo> pdNames() {

        return prodPdinfoService.list();
    }
    /**
     * 查询所有产品信息
     * @return
     */
    @RequestMapping("/pdNames/{pcId}")
    public List<ProdPdinfo> pcPdNames(@PathVariable Integer pcId) {

        return prodPdinfoService.list(new QueryWrapper<ProdPdinfo>().eq("pc_id", pcId).eq("is_used", 1));
    }
//    转化json串
    private JsonsRootBean getPdinfoJSON(List<ProdPartsinfoVo> list, JsonsRootBean jsonsRootBean){
        for (ProdPartsinfoVo prodPartsinfoVO : list) {
            if (!Func.isEmpty(prodPartsinfoVO.getChildren())){
//              只有子类部件在流程中
                getPdinfoJSON(prodPartsinfoVO.getChildren(), jsonsRootBean);
            }else {
//                生产部件的json对象
//
                Nodelist node = new Nodelist();
                node.setUuid(UUID.randomUUID().toString());
                node.setLeft(ptLeft+"px");
                node.setTop(ptTop+"px");
                node.setShow(true);
                node.setId(prodPartsinfoVO.getId());
                node.setPtName(prodPartsinfoVO.getPtName());
                node.setPtUuid(UUID.randomUUID().toString());
                node.setPtType(prodPartsinfoVO.getPtType());
                node.setType(2);//确定类型为部件
                node.setIco("el-icon-plus");
                node.setName(prodPartsinfoVO.getPtName());
                node.setPtClassify(prodPartsinfoVO.getPtClassify());//todo 金世纪暂无其它情况
                node.setReUuid(node.getUuid());
                if(jsonsRootBean.getNodeList() == null || Func.isEmpty(jsonsRootBean.getNodeList())){
                    jsonsRootBean.setNodeList(new ArrayList<>());
                }
                jsonsRootBean.getNodeList().add(node);
                mtTop = ptTop;
                if (!Func.isEmpty(prodPartsinfoVO.getMaterProdlinkVOList())){
                    for(MaterProdlink materProdlink : prodPartsinfoVO.getMaterProdlinkVOList()) {
                        //                    转换对应部件的物料的json串对象与连线的json对象
                        Nodelist nodeMater = new Nodelist();
                        nodeMater.setUuid(UUID.randomUUID().toString());
                        nodeMater.setShow(true);
                        nodeMater.setId(materProdlink.getMlId());
//                        查询物料
                        nodeMater.setLeft(mtLeft+"px");
                        nodeMater.setTop(mtTop+"px");
                        mtTop = mtTop + 55;
                        MaterMtinfo materMtinfo = materMtinfoService.getById(materProdlink.getMlId());
                        nodeMater.setMlNo(materMtinfo.getMlNo()==null ? "":materMtinfo.getMlNo());
                        nodeMater.setMlName(materMtinfo.getMlName());
                        nodeMater.setMcId(materMtinfo.getMcId());
                        nodeMater.setMaterial(materMtinfo.getMaterial());
                        nodeMater.setModel(materMtinfo.getModel());
                        nodeMater.setSpecification(materMtinfo.getSpecification());
                        nodeMater.setSize(materMtinfo.getSize());
                        nodeMater.setBrand(materMtinfo.getBrand());
                        nodeMater.setManufactor(materMtinfo.getManufactor());
                        nodeMater.setIslocal(materMtinfo.getIslocal());
                        nodeMater.setIsdel(materMtinfo.getIsdel());
                        nodeMater.setCreateAt(materMtinfo.getCreateAt());
                        nodeMater.setUpdateAt(materMtinfo.getUpdateAt());
                        nodeMater.setIco("el-icon-plus");
                        nodeMater.setName(materMtinfo.getMlName());
                        nodeMater.setType(1);//类型为物料
                        nodeMater.setMcName(materMtinfo.getMcId() != null ? materClassfiyService.getById(materMtinfo.getMcId()).getMcName() : null);
                        Leftstyle leftstyle = new Leftstyle();
                        leftstyle.setBackgroundColor("#409EFF");
                        nodeMater.setLeftStyle(leftstyle);
                        nodeMater.setPtUuid(node.getUuid());//等于部件的uuid
                        nodeMater.setMlNum(materProdlink.getMtNum()+"");
                        jsonsRootBean.getNodeList().add(nodeMater);
//                        添加连线
                        Linelist line = new Linelist();
                        line.setFrom(nodeMater.getUuid());
                        line.setTo(node.getUuid());
                        if(jsonsRootBean.getLineList() == null || Func.isEmpty(jsonsRootBean.getLineList())){
                            jsonsRootBean.setLineList(new ArrayList<>());
                        }
                        jsonsRootBean.getLineList().add(line);
                    };
                };
                prTop = ptTop;
                if (!Func.isEmpty(prodPartsinfoVO.getProdProcelinkVOList())){
                    String from = node.getUuid();
                    for (ProdProcelink procelink : prodPartsinfoVO.getProdProcelinkVOList()){
                        Nodelist nodeProce = new Nodelist();
                        nodeProce.setUuid(UUID.randomUUID().toString());
                        nodeProce.setLeft(prLeft+"px");
                        nodeProce.setTop(prTop+"px");
                        prTop = prTop + 55;

                        nodeProce.setShow(true);
                        ProcessWorkinfo processWorkinfo = processWorkinfoService.getById(procelink.getPrId());//查询工序信息
                        nodeProce.setId(processWorkinfo.getId());
                        nodeProce.setPrName(processWorkinfo.getPrName());
                        nodeProce.setPrNo(processWorkinfo.getPrNo());
                        nodeProce.setSort(procelink.getSortNum());
                        nodeProce.setStatus(processWorkinfo.getStatus());
                        nodeProce.setIslocal(processWorkinfo.getIslocal());
                        nodeProce.setIsdel(processWorkinfo.getIsdel());
                        nodeProce.setCreateAt(processWorkinfo.getCreateAt());
                        nodeProce.setUpdateAt(processWorkinfo.getUpdateAt());
//                            查询工序分类名称
                        Map map = new HashMap<>();
                        map.put("pr_id",processWorkinfo.getId());
                        List<ProcessClasslink> link = processClasslinkService.listByMap(map);
                        if (!Func.isEmpty(link)) {
                            ProcessClassify classify = processClassifyService.getById(link.get(0).getPyId());
                            if (!Func.isEmpty(classify)) {
                                nodeProce.setPyName(classify.getPyName());
                            }
                        }
                        nodeProce.setDiffLevel(procelink.getDiffLevel());
                        nodeProce.setWasteRate(procelink.getWasteRate());
                        nodeProce.setIco("el-icon-plus");
                        nodeProce.setName(processWorkinfo.getPrName());
                        nodeProce.setType(3);//类型为工序
                        Leftstyle leftStyle = new Leftstyle();
                        leftStyle.setBackgroundColor("#67C23A");
                        nodeProce.setLeftStyle(leftStyle);
                        nodeProce.setPrParam(procelink.getPrParam());
                        nodeProce.setRemarks(procelink.getRemarks());
                        nodeProce.setDifficulty("0");
                        nodeProce.setLossRate("0");
                        nodeProce.setPtUuid(node.getUuid());
                        jsonsRootBean.getNodeList().add(nodeProce);
//                            添加连线
                        Linelist line = new Linelist();
                        line.setFrom(from);
                        line.setTo(nodeProce.getUuid());
                        if(jsonsRootBean.getLineList() == null || Func.isEmpty(jsonsRootBean.getLineList())){
                            jsonsRootBean.setLineList(new ArrayList<>());
                        }
                        jsonsRootBean.getLineList().add(line);
                        from = nodeProce.getUuid();
                    };
                }
                if ((!Func.isEmpty(prodPartsinfoVO.getProdProcelinkVOList()) ? prodPartsinfoVO.getProdProcelinkVOList().size() : 0) > (!Func.isEmpty(prodPartsinfoVO.getMaterProdlinkVOList()) ? prodPartsinfoVO.getMaterProdlinkVOList().size() : 0)) {
                    ptTop = prTop + 40;
                }else if ((!Func.isEmpty(prodPartsinfoVO.getProdProcelinkVOList()) ? prodPartsinfoVO.getProdProcelinkVOList().size() : 0) < (!Func.isEmpty(prodPartsinfoVO.getMaterProdlinkVOList()) ? prodPartsinfoVO.getMaterProdlinkVOList().size() : 0)) {
                    ptTop = mtTop + 40;
                }else{
                    ptTop = ptTop + 150;
                }
                continue;
            }
        };
        return jsonsRootBean;
    }
}
