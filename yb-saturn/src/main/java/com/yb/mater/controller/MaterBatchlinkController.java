package com.yb.mater.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.auth.secure.util.SaSecureUtil;
import com.yb.mater.entity.MaterBatchlink;
import com.yb.mater.entity.MaterInstore;
import com.yb.mater.service.MaterBatchlinkService;
import com.yb.mater.service.MaterInstoreService;
import com.yb.mater.vo.BePutInStorageVO;
import com.yb.mater.vo.MaterBatchlinkVO;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.mapper.SaUserMapper;
import io.swagger.annotations.ApiParam;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/MaterBatchlink")
public class MaterBatchlinkController {

    @Autowired
    private MaterBatchlinkService materBatchlinkService;
    @Autowired
    private MaterInstoreService materInstoreService;
    @Autowired
    private SaUserMapper saUserMapper;


    @RequestMapping("/update")
    public R update(@RequestBody MaterBatchlink materBatchlink){
        return R.status(materBatchlinkService.updateById(materBatchlink));
    }

    @RequestMapping("/page")
    public R<IPage<MaterBatchlinkVO>> page(MaterBatchlinkVO materBatchlinkVO, Query query) {
        IPage<MaterBatchlinkVO> page = materBatchlinkService.getpage(materBatchlinkVO, Condition.getPage(query));
        return R.data(page);
    }
    @RequestMapping("/waitPage")
    public R<IPage<MaterBatchlinkVO>> waitPage(MaterBatchlinkVO materBatchlinkVO, Query query) {
        IPage<MaterBatchlinkVO> page = materBatchlinkService.waitPage(materBatchlinkVO, Condition.getPage(query));
        return R.data(page);
    }
    @RequestMapping("/getStatus")
    public R getStatus() {
        return R.data(materBatchlinkService.getStatus());
    }

    /**
     * 手动关闭
     * @param ids
     * @return
     */
    @RequestMapping("/close")
    public R close(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        if(StringUtil.isEmpty(ids)){
            return R.fail("请选择需要关闭的物料");
        }
        MaterBatchlink materBatchlink;
        List<Integer> toIntList = Func.toIntList(ids);
        List<MaterBatchlink> materBatchlinkList = new ArrayList<>();
        for(Integer id : toIntList){
            materBatchlink = new MaterBatchlink();
            materBatchlink.setId(id);
            materBatchlink.setStatus(4);
            materBatchlinkList.add(materBatchlink);
        }
        return R.status(materBatchlinkService.updateBatchById(materBatchlinkList));
    }

    /**
     * 入库
     * @param bePutInStorageVOList
     * @return
     */
    @RequestMapping("/bePutInStorage")
    public R bePutInStorage(@RequestBody List<BePutInStorageVO> bePutInStorageVOList){
        Integer userId = SaSecureUtil.getUserId();
        String userName = null;
        if (userId != null) {
            SaUser saUser = saUserMapper.selectById(userId);
            if(saUser != null){
                userName = saUser.getName();
            }
        }
        List<Integer> ids = new ArrayList<>();
        if(bePutInStorageVOList.isEmpty()){
            return R.fail("请选择需要入库的物料");
        }
        for(BePutInStorageVO bePutInStorageVO : bePutInStorageVOList){
            Integer id = bePutInStorageVO.getId();
            ids.add(id);
        }
        List<MaterBatchlink> materBatchlinkList = materBatchlinkService.getBaseMapper().selectBatchIds(ids);
        MaterInstore materInstore = null;
        for(MaterBatchlink materBatchlink : materBatchlinkList){
            Integer processNum = materBatchlink.getProcessNum();//工序数量
            Integer status = 2;
            Integer realacceptNum = materBatchlink.getRealacceptNum();//实收数量
            processNum = processNum == null ? 0 : processNum;
            realacceptNum = realacceptNum == null ? 0 : realacceptNum;
            if(processNum > realacceptNum){
                status = 1;//部分入库
            }
            materBatchlink.setContacterName(userName);
            materBatchlink.setReceiveUsid(userId);
            materBatchlink.setInTime(new Date());//实际入库时间
            materBatchlink.setStatus(status);
            materInstore = new MaterInstore();
            materInstore.setReceiveUsid(userId);
            materInstore.setContacterName(userName);
            materInstore.setMbId(materBatchlink.getId());
            materInstore.setLocation(materBatchlink.getLocation());//摆放位置
            materInstore.setPlateNum(materBatchlink.getPlateNum());//板数（可选）
            materInstore.setRemarks(materBatchlink.getRemarks());//备注
            materInstore.setRealacceptNum(realacceptNum);//实收数量
            materInstore.setInTime(new Date());
            materInstore.setCreateAt(new Date());
            materInstoreService.save(materInstore);//保存详情信息
        }
        return R.status(materBatchlinkService.updateBatchById(materBatchlinkList));
    }

}
