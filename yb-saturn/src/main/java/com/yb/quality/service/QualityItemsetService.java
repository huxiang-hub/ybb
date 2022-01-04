package com.yb.quality.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.quality.entity.QualityItemset;
import com.yb.quality.request.QualityRequest;
import com.yb.quality.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface QualityItemsetService extends IService<QualityItemset> {
    /**
     * 创建表
     * @param creatTableVO
     * @return
     */
    Boolean creatTable(CreatTableVO creatTableVO);

    /**
     * 新增表数据
     * @param insertTableMap
     * @return
     */
    Boolean insertTable(Map<String, Object> insertTableMap);

    /**
     * 查询表记录
     * @param qualityRequest
     * @return
     */
    IPage<QualityBaseVO> selectTable(QualityRequest qualityRequest, IPage<QualityBaseVO> page);

    /**
     * 添加字段
     * @param addColsVO
     * @return
     */
    boolean addTableCols(AddColsVO addColsVO);

    /**
     * 根据检查类型和工序分类查询所需的检查项
     * @param pyId 工序分类id
     * @param checkType 检查类型
     * @return
     */
    List<OptionVO> getQualityItemsetList(Integer pyId, String checkType);

    /**
     * 修改自定义质量巡检表
     * @param insertTableMap
     * @return
     */
    Boolean updateTable(Map<String, Object> insertTableMap);

    /**
     * 查询工序类型表获取信息及关联设备的ID
     * */
    List<ProcessClassifyVo> processList();

    /**
     * 通过py_id查询设备集合
     * */
    List<MachineMainfoVO> getMachins(@Param("pyId") Integer pyId);

    /**
     * 查询自定义新增表数据详情
     * @param tabName
     * @param id
     * @return
     */
    Map selectTableDetail(String tabName, Integer id);
}
