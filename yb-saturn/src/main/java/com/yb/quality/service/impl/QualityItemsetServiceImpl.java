package com.yb.quality.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.quality.entity.QualityItemset;
import com.yb.quality.entity.QualityTacitly;
import com.yb.quality.mapper.QualityItemsetMapper;
import com.yb.quality.mapper.QualityTacitlyMapper;
import com.yb.quality.request.QualityRequest;
import com.yb.quality.service.QualityItemsetService;
import com.yb.quality.vo.*;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class QualityItemsetServiceImpl extends ServiceImpl<QualityItemsetMapper, QualityItemset> implements QualityItemsetService {

    @Autowired
    private QualityItemsetMapper qualityItemsetMapper;
    @Autowired
    private QualityTacitlyMapper qualityTacitlyMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean creatTable(CreatTableVO creatTableVO) {
        Integer userId = SaSecureUtil.getUserId();
        String tabPrefix = "yb_quality_";//默认前缀
        Integer pyId = creatTableVO.getPyId();//工序分类id
        String pyName = creatTableVO.getPyName();
        List<String> checkTypeList = new ArrayList<>();
        checkTypeList.add("first_check");//首检
        checkTypeList.add("round_check");//巡检
//        checkTypeList.add("self_check");//自检
        int i = 1;
        String defultCols =
                "wf_id|ma_id|us_id|img_id|production_num|report_status|quantity_declared|scrap_quantity|start_at|end_at|create_at|update_at";//默认字段
        QualityTacitly qualityTacitly;
        QualityItemset qualityItemset;
        for(String checkType : checkTypeList){
            String tableName = tabPrefix + checkType + "_" +pyId;//表名(前缀+检查类型+工序分类id)
            qualityItemsetMapper.dropTable(tableName);
            qualityItemsetMapper.creatTable(tableName, null);
            String insertTableName = qualityItemsetMapper.getInsertTableName(pyId, checkType);
            if(StringUtil.isEmpty(insertTableName)){
                qualityTacitly = new QualityTacitly();
                Date date = new Date();
                qualityTacitly.setCheckType(checkType);
                qualityTacitly.setTabPrefix(tabPrefix);
                qualityTacitly.setTabName(tableName);
                qualityTacitly.setStatus(1);
                qualityTacitly.setCreateAt(date);
                qualityTacitly.setPyId(pyId);
                qualityTacitly.setPyName(pyName);
                qualityTacitly.setUpdateAt(date);
                qualityTacitly.setUsId(userId);
                qualityTacitly.setColDefault(defultCols);
                i = qualityTacitlyMapper.insert(qualityTacitly);

                qualityItemset = new QualityItemset();
                qualityItemset.setQtId(qualityTacitly.getId());
                qualityItemset.setFirstCheck(0);
                qualityItemset.setRoundCheck(0);
                qualityItemset.setSelfCheck(0);
                switch (checkType){
                    case "first_check":{
                        qualityItemset.setFirstCheck(1);
                        break;
                    }
                    case "round_check":{
                        qualityItemset.setRoundCheck(1);
                        break;
                    }
                    case "self_check":{
                        qualityItemset.setSelfCheck(1);
                        break;
                    }
                }
                qualityItemset.setPyId(pyId);
                qualityItemset.setPyName(pyName);
                qualityItemset.setUsId(userId);
                /*插入默认的字段信息*/
                qualityItemsetMapper.insertQualityItemset(qualityItemset);
            }
        }
        return i > 0;
    }

    @Override
    public Boolean insertTable(Map<String, Object> insertTableMap) {
        if (insertTableMap == null) {
            log.error("--------------insertTableMap不能为null-----------------");
            return false;
        }
        Date date = new Date();
        insertTableMap.put("start_at", date);
        insertTableMap.put("report_status", 1);
        /*处理数据*/
        UpdateOrSaveVO updateOrSaveVO = getUpdateOrSaveVO(insertTableMap);
        if(updateOrSaveVO == null){
            return false;
        }
        List<String> fieldNameList = updateOrSaveVO.getFieldNameList();//字段名集合
        String tableName = updateOrSaveVO.getTableName();//表名
        List<Object> valList = updateOrSaveVO.getValList();//字段值
        Integer save = qualityItemsetMapper.insetTable(tableName, fieldNameList, valList);
        return save > 0;
    }

    /**
     * 处理新增和修改时的数据结构
     * @param insertTableMap
     * @return
     */
    private UpdateOrSaveVO getUpdateOrSaveVO(Map<String, Object> insertTableMap){
        UpdateOrSaveVO updateOrSaveVO = new UpdateOrSaveVO();
        Integer pyId = (Integer) insertTableMap.get("pyId");//工序分类id
        String checkType = (String) insertTableMap.get("checkType");//检查类型
        if (pyId == null || StringUtil.isEmpty(checkType)) {
            log.error("----------------工序分类id或检查类型为空----------------");
            return null;
        }
        /*首先根据工序分类id和检查类型查询表名*/
        String tableName = qualityItemsetMapper.getInsertTableName(pyId, checkType);
        if (StringUtil.isEmpty(tableName)) {
            log.error("-----------------没有找到对应的表名-----------------");
            return null;
        }
        /*新增表记录*/
        List<String> fieldNameList = new ArrayList<>();
        List<Object> valList = new ArrayList<>();
        for (String key : insertTableMap.keySet()) {
            if ("pyId".equals(key) || "checkType".equals(key)) {
                continue;
            }
            fieldNameList.add(key);
            valList.add(insertTableMap.get(key));
        }
        updateOrSaveVO.setFieldNameList(fieldNameList);
        updateOrSaveVO.setValList(valList);
        updateOrSaveVO.setTableName(tableName);
        return updateOrSaveVO;
    }


    @Override
    public IPage<QualityBaseVO> selectTable(QualityRequest qualityRequest, IPage<QualityBaseVO> page) {
        String checkType = qualityRequest.getCheckType();//检查类型
        List<Integer> pyIds = qualityRequest.getPyIds();
//        Integer pyId = qualityRequest.getPyId();//工序分类id
//
//        String startTime = qualityRequest.getStartTime();
//        String endTime = qualityRequest.getEndTime();
//        if(StringUtil.isEmpty(startTime)){
//            qualityRequest.setStartTime(DateUtil.refNowDay());
//        }
//        if(StringUtil.isEmpty(endTime)){
//            qualityRequest.setEndTime(DateUtil.refNowDay());
//        }
        //返回对应表格式信息内容
        List<String> tableNames = qualityItemsetMapper.getInsertTableNames(pyIds, checkType);
        List<QualityBaseVO> tableList = qualityItemsetMapper.getTableListByTables(tableNames, qualityRequest, page);
        /*首先根据工序分类id和检查类型查询表名*/
        return page.setRecords(tableList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addTableCols(AddColsVO addColsVO) {
        Integer userId = SaSecureUtil.getUserId();
        String checkType = addColsVO.getCheckType();//检查类型
        Integer pyId = addColsVO.getPyId();//工序分类id
        String colName = addColsVO.getColName();//字段名
        String colShow = addColsVO.getColShow();//显示名称
        /*查询表名*/
        String tableName = qualityItemsetMapper.getInsertTableName(pyId, checkType);
        if(StringUtil.isEmpty(tableName)){
            log.error("--------------表不存在----------------");
            return false;
        }
        /*验证存入的字段在表中是否存在*/
        Integer verifyTheFieldStatus = qualityItemsetMapper.verifyTheField(tableName, colName);
        if (verifyTheFieldStatus != 0) {
            log.error("--------------该字段已存在----------------");
            return false;
        }
        /*查询存储表信息的详情*/
        QualityTacitly qualityTacitly = qualityTacitlyMapper.getQualityTacitly(pyId, checkType);
        if(qualityTacitly != null){
            String colDefault = qualityTacitly.getColDefault();
            qualityTacitly.setColDefault(colDefault + "|" + colName);//修改默认字段
        }
        try {
            qualityItemsetMapper.addTableCols(tableName, colName, colShow);
            qualityTacitlyMapper.updateById(qualityTacitly);
            QualityItemset qualityItemset = new QualityItemset();
            Date date = new Date();
            qualityItemset.setPyId(pyId);
            qualityItemset.setPyName(addColsVO.getPyName());
            qualityItemset.setColShow(colShow);
            qualityItemset.setColName(colName);
            qualityItemset.setStatus(1);
            qualityItemset.setColDesc(addColsVO.getColDesc());
            qualityItemset.setColInfo(addColsVO.getColInfo());
            qualityItemset.setColType(addColsVO.getColType());
            qualityItemset.setColVal(addColsVO.getColVal());
            qualityItemset.setCreateAt(date);
            qualityItemset.setIsImg(addColsVO.getIsImg());
            qualityItemset.setUpdateAt(date);
            qualityItemset.setQtId(qualityTacitly.getId());
            qualityItemset.setUsId(userId);
            qualityItemset.setFirstCheck(0);
            qualityItemset.setRoundCheck(0);
            qualityItemset.setSelfCheck(0);
            switch (checkType){
                case "first_check":{
                    qualityItemset.setFirstCheck(1);
                    break;
                }
                case "round_check":{
                    qualityItemset.setRoundCheck(1);
                    break;
                }
                case "self_check":{
                    qualityItemset.setSelfCheck(1);
                    break;
                }
            }
            qualityItemsetMapper.insert(qualityItemset);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public List<OptionVO> getQualityItemsetList(Integer pyId, String checkType) {
        List<OptionVO> optionVOList = new ArrayList<>();
        OptionVO optionVO;
        List<TabControlVO> tabControlVOList;
        TabControlVO tabControlVO;
        List<QualityItemset> qualityItemsetList = qualityItemsetMapper.getQualityItemsetList(pyId, checkType);
        for(QualityItemset qualityItemset : qualityItemsetList){
            optionVO = new OptionVO();
            tabControlVOList = new ArrayList<>();
            String colName = qualityItemset.getColName();//数据字段名称
            String colShow = qualityItemset.getColShow();//显示名称
            String colDesc = qualityItemset.getColDesc();//备注说明；注意事项
            String colInfo = qualityItemset.getColInfo();//字段数据选择用竖线分隔；单选多选选项；默认值
            String colVal = qualityItemset.getColVal();//字段数据选择用竖线分隔；为选择value值信息
            Integer colType = qualityItemset.getColType();//字段类型：1单选、2多选、3文本
            optionVO.setColDesc(colDesc);
            optionVO.setColName(colName);
            optionVO.setColshow(colShow);
            optionVO.setColType(colType);
            if(colType != 3 && colInfo != null && colVal != null){
                String[] colInfos = colInfo.split("\\|");
                String[] colVals = colVal.split("\\|");
                for(int i = 0; i < colInfos.length; i++){
                    tabControlVO = new TabControlVO();
                    tabControlVO.setDisplayName(colInfos[i]);
                    tabControlVO.setSelectiveValue(colVals[i]);
                    tabControlVOList.add(tabControlVO);
                }
            }
            optionVO.setTabControlVOList(tabControlVOList);
            optionVOList.add(optionVO);
        }
        return optionVOList;
    }

    @Override
    public Boolean updateTable(Map<String, Object> insertTableMap) {
        Integer id = (Integer)insertTableMap.get("id");
        Integer pyId = (Integer) insertTableMap.get("pyId");//工序分类id
        String checkType = (String) insertTableMap.get("checkType");//检查类型
        if (pyId == null || StringUtil.isEmpty(checkType)) {
            log.error("------------------工序分类id或检查类型为空------------------");
            return false;
        }
        /*首先根据工序分类id和检查类型查询表名*/
        String tableName = qualityItemsetMapper.getInsertTableName(pyId, checkType);
        if (StringUtil.isEmpty(tableName)) {
            log.error("------------------没有找到对应的表名-----------------");
            return false;
        }
        /*移除不需要的map*/
        insertTableMap.remove("id");
        insertTableMap.remove("pyId");
        insertTableMap.remove("checkType");
        Integer update;
        try {
            update = qualityItemsetMapper.updateTable(tableName, insertTableMap, id);
        }catch (Exception e){
            return false;
        }
        return update > 0;
    }

    @Override
    public List<ProcessClassifyVo> processList() {
        return qualityItemsetMapper.processList();
    }

    @Override
    public List<MachineMainfoVO> getMachins(Integer pyId) {
        return qualityItemsetMapper.getMachins(pyId);
    }

    @Override
    public Map selectTableDetail(String tabName, Integer id) {
        Map map = qualityItemsetMapper.selectTableDetail(tabName, id);
        if(!StringUtil.isEmpty(tabName)){
            String s = tabName.substring(11);
            int lastIndexOf = s.lastIndexOf("_");
            map.put("checkType", s.substring(0, lastIndexOf));
            map.put("pyId", s.substring(lastIndexOf + 1));
        }
        return map;
    }


}
