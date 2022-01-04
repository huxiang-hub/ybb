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
package com.yb.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.base.entity.BaseDeptinfo;
import com.yb.base.mapper.BaseDeptinfoMapper;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.base.vo.BaseDeptinfoVO;
import com.yb.base.vo.DeptModelVO;
import com.yb.base.vo.DeptNameModel;
import com.yb.base.wrapper.BaseDeptinfoWrapper;
import com.yb.exeset.wrapper.ExesetNetworkWrapper;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.machine.wrapper.MachineMainfoWrapper;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.mapper.ProcessWorkinfoMapper;
import com.yb.process.vo.ProcessClassifyVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门结构_yb_ba_dept 服务实现类
 *
 * @author Blade
 * @since 2020-03-10
 */
@Service
public class BaseDeptinfoServiceImpl extends ServiceImpl<BaseDeptinfoMapper, BaseDeptinfo> implements IBaseDeptinfoService {
    /**
     * 注入的servce 可能存在问题
     */
    @Autowired
    private IMachineMainfoService machineMainfoMapper;
    @Autowired
    private BaseDeptinfoMapper baseDeptinfoMapper;
    @Autowired
    private MachineMainfoMapper machineMapper;
    @Autowired
    private ProcessWorkinfoMapper processWorkinfoMapper;

    @Override
    public IPage<BaseDeptinfoVO> selectBaseDeptinfoPage(IPage<BaseDeptinfoVO> page, BaseDeptinfoVO baseDeptinfo) {
        return page.setRecords(baseMapper.selectBaseDeptinfoPage(page, baseDeptinfo));
    }

    @Override
    public List<BaseDeptinfoVO> baseDeptinfos() {
        //TODO 代码需要重写
        List<BaseDeptinfoVO> depts =new ArrayList<>();//存储一级部门
        List<BaseDeptinfoVO> items =baseMapper.baseDeptinfos();
        for (BaseDeptinfoVO item1 : items) {
            BaseDeptinfoVO deptinfo = new BaseDeptinfoVO();
            if (item1.getPId() == 0) {
                item1.setLabel(item1.getDpName());//部门名字
                item1.setValue(item1.getId()); // 部门的ID
                depts.add(item1);
                for (BaseDeptinfoVO item2 : items) {
                    if (item1.getId() == item2.getPId()) { //添加二级部门名字给一级部门
                        DeptModelVO modelVO = new DeptModelVO();
                        modelVO.setId(item2.getId());//前端显是Id
                        modelVO.setDpId(item2.getId()); //部门ID
                        modelVO.setLabel(item2.getDpName()); //部门名字
                        modelVO.setValue(item2.getId());//部门ID
                        //List<ProcessClassifyVO> pcList = getAllProcess(item2.getId()); //工序对应的设备
                        List<MachineMainfoVO> maList = machineMainfoMapper.getMachineByDpId(item2.getId());
                        modelVO.setChildren(maList);//通过部门ID去找对应的工序
                      /*  item1.getChildren().add(modelVO); //个上级部门添加子部门节点*/
                    }
                }
            }
        }
        return depts; // 几个一级部门几条item
    }

    @Override
    public List<DeptNameModel> getPdNameByClassify(Integer classify) {

        return baseMapper.getPdNameByClassify(classify);
    }

    /**
     * 前端组件不太好接收值  封装成前端需要展示的字段
     *
     * @param dpId
     * @return
     */
    @Override
    public List<ProcessClassifyVO> getAllProcess(Integer dpId) {
        List<ProcessClassifyVO> oldList = baseMapper.getAllProcess(dpId);
        List<ProcessClassifyVO> newList = new ArrayList<>();
        for (ProcessClassifyVO pro : oldList) {
            pro.setId(pro.getPrId());
            pro.setValue(pro.getPrId());
            pro.setLabel(pro.getPyName());
            //获取指定车间下 指定工序的设备 添加给工序节点
            List<MachineMainfoVO> listma = machineMainfoMapper.getMachinsByDpIdPrId(null, dpId, pro.getPrId(), null);
            pro.setChildren(listma);
            newList.add(pro);
        }
        return newList;
    }

    @Override
    public Integer getDpIdByDpName(String dpName) {
        return baseMapper.getDpIdByDpName(dpName);
    }

    @Override
    public List<BaseDeptinfo> getDpNameByPrId(Integer prId) {
        return baseMapper.getDpNameByPrId(prId);
    }

    @Override
    public List<BaseDeptinfoVO> getHierarchyDeptList() {
        /*查询所有部门*/
        List<BaseDeptinfo> baseDeptinfoList = baseDeptinfoMapper.selectList(new QueryWrapper<BaseDeptinfo>().eq("is_deleted", 0));
        List<BaseDeptinfoVO> baseDeptinfoVOList = new ArrayList<>();
        for(BaseDeptinfo baseDeptinfo : baseDeptinfoList){
            BaseDeptinfoVO baseDeptinfoVO = BaseDeptinfoWrapper.build().entityVO(baseDeptinfo);
            baseDeptinfoVOList.add(baseDeptinfoVO);
        }
        return getDeptTree(baseDeptinfoVOList);
    }

    /**
     * 部门树
     * @param baseDeptinfoList
     * @return
     */
    private List<BaseDeptinfoVO> getDeptTree(List<BaseDeptinfoVO> baseDeptinfoList){
        List<MachineMainfo> machineMainfoList = machineMapper.selectList(new QueryWrapper<>());
        List<ProcessWorkinfo> processWorkinfoList = processWorkinfoMapper.selectList(new QueryWrapper<>());
        List<MachineMainfoVO> machineMainfoVOList = new ArrayList<>();

        for(MachineMainfo machineMainfo : machineMainfoList){//转化为VO
            MachineMainfoVO machineMainfoVO = MachineMainfoWrapper.build().entityVO(machineMainfo);
            //ProcessWorkinfo processWorkinfo = processWorkinfoMapper.selectById(machineMainfo.getProId());//根据设备绑定的主工序id查询工序信息
            for(ProcessWorkinfo processWorkinfo : processWorkinfoList){
                if(processWorkinfo != null){//判断设备是否已绑定主工序
                    if(processWorkinfo.getId().equals(machineMainfoVO.getProId())){
                        machineMainfoVO.setPrName(processWorkinfo.getPrName());
                        machineMainfoVO.setPrId(processWorkinfo.getId());
                        break;
                    }
                }
            }
            machineMainfoVOList.add(machineMainfoVO);
        }

        List<MachineMainfoVO> machineListVO;
        List<BaseDeptinfoVO> treeList = new ArrayList<>();
        for(BaseDeptinfoVO tree : baseDeptinfoList){
            //找到跟部门
            if(tree.getPId() == null || tree.getPId() == 0){
                tree.setLabel(tree.getDpName());//部门名字
                tree.setValue(tree.getId()); // 部门的ID
                tree.setType(0);//标识部门
                if(tree.getClassify() == 2){//如果是生产部门,则查询部门下的设备
                    machineListVO = new ArrayList<>();
                    for(MachineMainfoVO machineMainfoVO : machineMainfoVOList){
                        Integer dpId = machineMainfoVO.getDpId();
                        if(dpId == null){
                            dpId = 0;
                        }
                        if(dpId.equals(tree.getId())){
                            machineListVO.add(machineMainfoVO);
                        }
                    }
                    if(!machineListVO.isEmpty()){
                        if(tree.getChildren() == null){
                            tree.setChildren(new ArrayList<>());
                        }
                        BaseDeptinfoVO modelVO = null;
                        for(MachineMainfoVO machineMainfoVO : machineListVO){
                            modelVO = new BaseDeptinfoVO();
                            String prName = machineMainfoVO.getPrName();
                            if(StringUtil.isEmpty(prName)){
                                prName = "暂无";
                            }
                            modelVO.setType(1);//标识设备
                            modelVO.setId(machineMainfoVO.getId());//前端显是Id
                            modelVO.setLabel(machineMainfoVO.getName() + "【"+ prName +"】"); //设备名字
                            modelVO.setValue(machineMainfoVO.getId());//设备ID
                            modelVO.setMaId(machineMainfoVO.getId());//设备id
                            tree.getChildren().add(modelVO);
                        }
                    }
                }
                treeList.add(tree);
            }
            for(BaseDeptinfoVO childrenDept : baseDeptinfoList){
                if(childrenDept.getPId() != null && childrenDept.getPId() != 0 && childrenDept.getPId().equals(tree.getId())){
                    if(tree.getChildren() == null){
                        tree.setChildren(new ArrayList<>());
                    }
                    childrenDept.setType(0);//标识部门
                    childrenDept.setId(childrenDept.getId());//前端显是Id
                    childrenDept.setLabel(childrenDept.getDpName()); //部门名字
                    childrenDept.setValue(childrenDept.getId());//部门ID
                    if(childrenDept.getClassify() == 2){//如果是生产部门,则查询部门下的设备
                        machineListVO = new ArrayList<>();
                        for(MachineMainfoVO machineMainfoVO : machineMainfoVOList){
                            Integer dpId = machineMainfoVO.getDpId();
                            if(childrenDept.getId().equals(dpId)){
                                machineListVO.add(machineMainfoVO);
                            }
                        }
                       // List<MachineMainfoVO> machineListVO = getMachineListVO(childrenDept.getId());
                        if(!machineListVO.isEmpty()){
                            if(childrenDept.getChildren() == null) {
                                childrenDept.setChildren(new ArrayList<>());
                            }
                            BaseDeptinfoVO modelVO = null;
                            for(MachineMainfoVO machineMainfoVO : machineListVO){
                                modelVO = new BaseDeptinfoVO();
                                String prName = machineMainfoVO.getPrName();
                                if(StringUtil.isEmpty(prName)){
                                    prName = "暂无";
                                }
                                modelVO.setType(1);//标识设备
                                modelVO.setId(machineMainfoVO.getId());//前端显是Id
                                modelVO.setLabel(machineMainfoVO.getName() + "【"+ prName +"】"); //设备名字
                                modelVO.setValue(machineMainfoVO.getId());//设备ID
                                modelVO.setMaId(machineMainfoVO.getId());//设备id
                                childrenDept.getChildren().add(modelVO);
                            }
                        }
                    }
                    tree.getChildren().add(childrenDept);//符合条件则为子部门
                }
            }
        }
        return treeList;
    }

    /**
     * 获取部门下的设备和设备绑定的(主)工序
     * @param dpId
     * @return
     */
    private List<MachineMainfoVO> getMachineListVO(Integer dpId){
        /*查询部门下所有设备信息*/
        List<MachineMainfo> machineMainfoList = machineMapper.selectList(new QueryWrapper<MachineMainfo>().eq("dp_id", dpId));
        List<MachineMainfoVO> machineMainfoVOList = new ArrayList<>();
        for(MachineMainfo machineMainfo : machineMainfoList){//转化为VO
            MachineMainfoVO machineMainfoVO = MachineMainfoWrapper.build().entityVO(machineMainfo);
            ProcessWorkinfo processWorkinfo = processWorkinfoMapper.selectById(machineMainfo.getProId());//根据设备绑定的主工序id查询工序信息
            if(processWorkinfo != null){//判断设备是否已绑定主工序
                machineMainfoVO.setPrName(processWorkinfo.getPrName());
                machineMainfoVO.setPrId(processWorkinfo.getId());
            }
            machineMainfoVOList.add(machineMainfoVO);
        }
        return machineMainfoVOList;
    }

}
