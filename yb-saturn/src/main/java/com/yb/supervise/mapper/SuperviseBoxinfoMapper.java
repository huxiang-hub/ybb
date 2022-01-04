package com.yb.supervise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.process.vo.ProcessWorkinfoVO;
import com.yb.supervise.entity.SuperviseBoxinfo;
import com.yb.supervise.entity.SuperviseExecute;
import com.yb.supervise.vo.MaStatusNumberVO;
import com.yb.supervise.vo.MachineAtPresentStatusVO;
import com.yb.supervise.vo.SuperviseBoxinfoVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.yilong.request.BoxInfoRequest;
import com.yb.yilong.response.BoxInfoNumberVO;
import com.yb.yilong.response.BoxInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备当前状态表boxinfo-视图 Mapper 接口
 *
 * @author Blade
 * @since 2020-03-10
 */
@Mapper
public interface SuperviseBoxinfoMapper extends BaseMapper<SuperviseBoxinfo> {

    /**
     * 自定义分页
     *
     * @param page
     * @param superviseBoxinfo
     * @return
     */
    List<SuperviseBoxinfoVO> selectSuperviseBoxinfoPage(IPage page, SuperviseBoxinfoVO superviseBoxinfo);

    int getBoxNum(String mId);

    String getMacUser(String mId);

    SuperviseBoxinfo getBoxInfoByBno(String uuid_s);

    SuperviseBoxinfo getBoxInfoByMid(Integer maId);


    List<SuperviseBoxinfoVO> selectSuperviseBoxinfoVO();

    Integer pageCount(String status);

    List<SuperviseBoxinfoVO> getBoxListNotStop();

    List<MaStatusNumberVO> selectDeptBoxinfoStatusList(Integer dpId);

    List<WorkbatchOrdlink> selectNumberList(@Param("dpId") Integer dpId);

    Integer selectSdNumber(Integer dpId);

    /**
     * 查询车间下每个设备的生产状态
     *
     * @param prId
     * @param dpId
     * @return
     */
    List<SuperviseBoxinfoVO> selectDeptBoxinfoSdList(Integer prId, Integer dpId);

    /**
     * 查询车间所
     *
     * @param dpId
     * @return
     */
    List<ProcessWorkinfoVO> selectDpProcess(Integer dpId);

    Integer removerListByMaid(@Param("list") List<Integer> maIds);

    /**
     * 动态查询不同部门,不同工序的设备实施状态
     *
     * @param dpId
     * @param prId
     * @return
     */
    List<SuperviseBoxinfoVO> selectBoxinfoList(Integer dpId, Integer prId);

    List<SuperviseBoxinfo> findByStatus();

    /**
     * 根据设备id查询设备当前状态
     *
     * @param maId
     * @return
     */
    MachineAtPresentStatusVO getBoxinfoStatusByMaId(Integer maId);

    SuperviseExecute getByMaId(@Param("maId") Integer maId);

    BoxInfoVO boxInfo(@Param("request") BoxInfoRequest request);

    BoxInfoNumberVO boxNumber(Integer maId);

}

