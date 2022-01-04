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

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.order.entity.Node;
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
import com.yb.prod.wrapper.ProdPartsinfoWrapper;
import com.yb.prod.wrapper.ProdPdmodelWrapper;
import com.yb.system.menu.wrapper.MenuWrapper;
import io.protostuff.Request;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.node.INode;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 产品模版信息（productmodel） 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/prodpdmodel")
@Api(value = "产品模版信息（productmodel）", tags = "产品模版信息（productmodel）接口")
public class ProdPdmodelController extends BladeController {

    @Autowired
    private IProdPdmodelService prodPdmodelService;
    @Autowired
    private IProdPdinfoService prodPdinfoService;
    @Autowired
    private IProdPartsinfoService prodPartsinfoService;
    @Autowired
    private IProdProcelinkService prodProcelinkService;
    @Autowired
    private IMaterProdlinkService materProdlinkService;
    //    部件编号增加的尾号
    private Integer ptNoTail = 1;
    /**
     * 详情(选择模板后查询相关产品信息，用于订单新增)
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入prodPdmodel")
    public R<ProdPdmodelVO> detail(ProdPdmodelVO prodPdmodel) {
//        选择某一模板后查询查询对应产品信息
        ProdPdmodelVO detail = prodPdmodelService.getOneProdPdmodelById(prodPdmodel.getId());
        JSONObject jsonObject = JSONObject.fromObject(detail.getModelJson());
        // 使用JSONObject.toBean(jsonObject, beanClass, classMap)
        List<Node> nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
//        查询对应工序展示图与对应工序集合与工序集合的参数
//        工序展示图与产品图片与产品id
//        ProdPdinfoVO prodPdinfoVO = prodPdinfoService.getProdPdinfoVoBypdNo(detail.getPdNo());
//        detail.setPdId(prodPdinfoVO.getId());
//        detail.setImageUrl(prodPdinfoVO.getImageUrl());
//        detail.setProcePic(prodPdinfoVO.getProcePic());
//        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(prodPdmodel.getId(), 2);
//        查询所有部件的工序
        List<ProdProcelinkVO> prodProcelinkVOList = new ArrayList<>();
        //        物料集合
        List<MaterProdlinkVO> materProdlinkVOList = new ArrayList<>();
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
//            查询每个部件对应的工序集合
            List<ProdProcelinkVO> procelinkVOS = prodProcelinkService.list(partsinfoVo.getId());
            prodProcelinkVOList.addAll(procelinkVOS);
            partsinfoVo.setProdProcelinkVOList(procelinkVOS);
            //        查询部件对应的物料集合
            List<MaterProdlinkVO> materProdlinkVO = materProdlinkService.selectMaterProdlinkVOListById(partsinfoVo.getId());
            materProdlinkVOList.addAll(materProdlinkVO);
            //            查询部件对应的pt_classify与pt_ids
            partsinfoVo.setPtIdsList(ptIdsList(partsinfoVo));
            //            查询部件的ptUuid
            Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
            if (!Func.isEmpty(node)){
                partsinfoVo.setPtUuid(node.get().getPtUuid());
            }
        }

        detail.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
        detail.setProdProcelinkVOList(prodProcelinkVOList);
        detail.setMaterProdlinkVOList(materProdlinkVOList);

        return R.data(ProdPdmodelWrapper.build().entityVO(detail));
    }
    //    List<ProdPartsinfoVo>转树结构
    private static List<ProdPartsinfoVo> listGetStree(List<ProdPartsinfoVo> list) {
        List<ProdPartsinfoVo> treeList = new ArrayList<ProdPartsinfoVo>();
        for (ProdPartsinfoVo tree : list) {
            //找到根
            if (tree.getPid() == null) {
                treeList.add(tree);
            }
            //找到子
            for (ProdPartsinfoVo treeNode : list) {
                if (treeNode.getPid() == tree.getId()) {
                    if (tree.getChildren() == null) {
                        tree.setChildren(new ArrayList<ProdPartsinfoVo>());
                    }
                    tree.getChildren().add(treeNode);
                }
            }
        }
        return treeList;
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
     * 分页 产品模版信息（productmodel）
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入prodPdmodel")
    public R<IPage<ProdPdmodelVO>> list(ProdPdmodel prodPdmodel, Query query) {
        IPage<ProdPdmodel> pages = prodPdmodelService.page(Condition.getPage(query), Condition.getQueryWrapper(prodPdmodel));
        return R.data(ProdPdmodelWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 产品模版信息（productmodel）
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入prodPdmodel")
    public R<IPage<ProdPdmodelVO>> page(ProdPdmodelVO prodPdmodel, Query query) {
        IPage<ProdPdmodelVO> pages = prodPdmodelService.selectProdPdmodelPage(Condition.getPage(query), prodPdmodel);
        return R.data(pages);
    }

    /**
     * 新增 产品模版信息（productmodel）
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入prodPdmodel")
    @Transactional
    public R save(@Valid @RequestBody ProdPdmodelVO prodPdmodel) {
        System.out.println(prodPdmodel);
//        先查询是否产品编号重复
        try {
            if (!Func.isEmpty(prodPdinfoService.getProdPdinfoVoBypdNo(prodPdmodel.getPdNo()))){
                return R.fail("产品编号重复");
            }
            ProdPdinfo prodPdinfo = new ProdPdinfo();
            prodPdinfo.setPcId(prodPdmodel.getPcId());//产品类型
            prodPdinfo.setImageUrl(prodPdmodel.getImageUrl());//产品图
            prodPdinfo.setProcePic(prodPdmodel.getProcePic());//工序图
            prodPdinfo.setModelJson(prodPdmodel.getModelJson());//工序json
            prodPdinfo.setPdName(prodPdmodel.getPdName());//产品名
            prodPdinfo.setPdNo(prodPdmodel.getPdNo());//产品编号
            prodPdinfo.setCreateAt(new Date());//增加时间
            prodPdinfo.setIsUsed(1);//启用
            prodPdinfoService.save(prodPdinfo);
//            生成了对应pdId
//            保存对应部件的工序
            savePartsinfo(prodPdmodel.getProdPartsinfoVOList());
//            保存对应部件的物料
            for(MaterProdlinkVO materProdlinkVO : prodPdmodel.getMaterProdlinkVOList()){
                materProdlinkService.save(materProdlinkVO);
            }
            return R.status(prodPdmodelService.save(prodPdmodel));
        }catch (Exception e){
            return R.fail("增加异常，网络连接波动");
        }
    }
    //递归序
    private boolean savePartsinfo(List<ProdPartsinfoVo> prodPartsinfoVoList) throws Exception{
        for (ProdPartsinfoVo partsinfoVo : prodPartsinfoVoList){
            if (partsinfoVo.getProdProcelinkVOList().size() != 0){
                for(ProdProcelinkVO prodProcelinkVO : partsinfoVo.getProdProcelinkVOList()){
//                    存入对应的部件Id
                    prodProcelinkVO.setPtId(partsinfoVo.getId());
                    prodProcelinkService.save(prodProcelinkVO);
                }
            }
            if (partsinfoVo.getChildren().size() == 0){
                return true;
            }else{
                savePartsinfo(partsinfoVo.getChildren());
            }
        }
        return true;
    }
    /**
     * 修改 产品模版信息（productmodel）
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入prodPdmodel")
    public R update(@Valid @RequestBody ProdPdmodelVO prodPdmodelVO) {
        if(prodPdmodelVO.getPdName().matches("^\\+?[1-9][0-9]*$")){
            ProdPdinfo prodPdinfo = prodPdinfoService.getById(Integer.valueOf(prodPdmodelVO.getPdName()));
            prodPdmodelVO.setPdName(prodPdinfo.getPdName());
        }

        return R.status(prodPdmodelService.updateById(prodPdmodelVO));
    }

    /**
     * 新增或修改 产品模版信息（productmodel）
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @Transactional
    @ApiOperation(value = "新增或修改", notes = "传入prodPdmodel")
    public R submit(@Valid @RequestBody ProdPdmodelVO prodPdmodelVO) {
        ProdPdinfoVO prodPdinfo = prodPdinfoService.getProdPdinfoVoBypdId(prodPdmodelVO.getId());
        String pdNo = prodPdinfo.getPdNo();
        ProdPdmodel pdmodel = prodPdmodelService.getOne(new QueryWrapper<ProdPdmodel>().eq("pd_no", pdNo));
        if(pdmodel != null){
            return R.fail("该产品模板已存在");
        }
        JSONObject jsonObject = JSONObject.fromObject(prodPdinfo.getModelJson());
        List<Node> nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(prodPdinfo.getId(), 1);

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
            Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
            if (node.isPresent()){
                partsinfoVo.setPtUuid(node.get().getPtUuid());
            }
        }
        prodPdinfo.setProdProcelinkVOList(prodProcelinkVOList);
        prodPdinfo.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
        prodPdinfo.setMaterProdlinkVOList(materProdlinkVOList);

        try {
            //        保存为模板
            ProdPdmodelVO prodPdmodel = new ProdPdmodelVO();
            prodPdmodel.setIsUsed(1);
            prodPdmodel.setPdNo(prodPdinfo.getPdNo());
            prodPdmodel.setPdName(prodPdinfo.getPdName());
            prodPdmodel.setImageUrl(prodPdinfo.getImageUrl());
            prodPdmodel.setModelJson(prodPdinfo.getModelJson());
            prodPdmodel.setPcId(prodPdinfo.getPcId());
            prodPdmodel.setProcePic(prodPdinfo.getProcePic());
            prodPdmodelService.save(prodPdmodel);

            prodPdmodel.setMaterProdlinkVOList(prodPdinfo.getMaterProdlinkVOList());
            prodPdmodel.setProdProcelinkVOList(prodPdinfo.getProdProcelinkVOList());
            prodPdmodel.setProdPartsinfoVOList(prodPdinfo.getProdPartsinfoVOList());
            JSONObject jsonObjectPdModel = savePdinfoBeToProdPdmodelVO(prodPdmodel, 2);

            prodPdmodel.setModelJson(jsonObjectPdModel.toString());

            return R.status(prodPdmodelService.saveOrUpdate(prodPdmodel));
        }catch (Exception e){
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("转换失败");
        }
    }

    /**
     * 保存为模板
     * @param pdmodelVO
     * @param pdType
     * @return
     * @throws Exception
     */
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
     * 新增或修改 产品模版信息（productmodel）
     */
    @PostMapping("/inster")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入prodPdmodel")
    public R inster(@Valid @RequestBody ProdPdmodelVO prodPdmodelVO) {
        ProdPdinfoVO prodPdinfo = prodPdinfoService.getProdPdinfoVoBypdId(Integer.valueOf(prodPdmodelVO.getPdName()));
        String pdNo = prodPdinfo.getPdNo();
        ProdPdmodel pdmodel = prodPdmodelService.getOne(new QueryWrapper<ProdPdmodel>().eq("pd_no", pdNo));
        if(pdmodel != null){
            return R.fail("该产品模板已存在");
        }
        JSONObject jsonObject = JSONObject.fromObject(prodPdinfo.getModelJson());
        List<Node> nodeList = JSONArray.parseArray(jsonObject.getString("nodeList"), Node.class);
        //        查询部件集合
        List<ProdPartsinfoVo> prodPartsinfoVoList = prodPartsinfoService.listByPdId(prodPdinfo.getId(), 1);

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
            Optional<Node> node = nodeList.stream().filter(node1 -> (node1.getType() == 2 && node1.getId()!= null && node1.getId().intValue() == partsinfoVo.getId().intValue())).findFirst();
            if (node.isPresent()){
                partsinfoVo.setPtUuid(node.get().getPtUuid());
            }
        }
        prodPdinfo.setProdProcelinkVOList(prodProcelinkVOList);
        prodPdinfo.setProdPartsinfoVOList(PartsinfoUtil.listGetStree(prodPartsinfoVoList));
        prodPdinfo.setMaterProdlinkVOList(materProdlinkVOList);

        try {
            //        保存为模板
            ProdPdmodelVO prodPdmodel = new ProdPdmodelVO();
            prodPdmodel.setIsUsed(prodPdmodelVO.getIsUsed());
            prodPdmodel.setPdNo(prodPdinfo.getPdNo());
            prodPdmodel.setPdName(prodPdinfo.getPdName());
            prodPdmodel.setImageUrl(prodPdinfo.getImageUrl());
            prodPdmodel.setModelJson(prodPdinfo.getModelJson());
            prodPdmodel.setPcId(prodPdinfo.getPcId());
            prodPdmodel.setProcePic(prodPdinfo.getProcePic());
            prodPdmodelService.save(prodPdmodel);

            prodPdmodel.setMaterProdlinkVOList(prodPdinfo.getMaterProdlinkVOList());
            prodPdmodel.setProdProcelinkVOList(prodPdinfo.getProdProcelinkVOList());
            prodPdmodel.setProdPartsinfoVOList(prodPdinfo.getProdPartsinfoVOList());
            JSONObject jsonObjectPdModel = savePdinfoBeToProdPdmodelVO(prodPdmodel, 2);

            prodPdmodel.setModelJson(jsonObjectPdModel.toString());

            return R.status(prodPdmodelService.saveOrUpdate(prodPdmodel));
        }catch (Exception e){
            e.printStackTrace();
            //设置手动回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("转换失败");
        }
    }


    /**
     * 删除 产品模版信息（productmodel）
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(prodPdmodelService.removeByIds(Func.toIntList(ids)));
    }
    /**
     * 所有模板
     */
    @GetMapping("/tree")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入prodPdmodel")
    public List<ProdPdmodelVO> tree() {
        List<ProdPdmodelVO> list = prodPdmodelService.tree();
        return list;
    }
    @RequestMapping("/selectePdinfoModelList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入prodPdmodel")
    public R<IPage<ProdPdmodelVO>> selectePdinfoModelList(ProdPdmodelVO prodPdmodel, Query query) {
        return R.data(prodPdmodelService.selectePdinfoModelList(Condition.getPage(query), prodPdmodel));
    }
    @RequestMapping("/selectePdinfoModel")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "id")
    public R<ProdPdmodelVO> selectePdinfoModel(Integer id) {
        return R.data(prodPdmodelService.selectePdinfoModel(id));
    }
    @RequestMapping("/selectePdinfoModelOne")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "id")
    public R<ProdPdmodel> selectePdinfoModelOne(Integer id) {
        return R.data(prodPdmodelService.getById(id));
    }

}
