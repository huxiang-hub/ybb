package com.yb.panelapi.fault;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.execute.dto.ExecuteFaultDTO;
import com.yb.execute.entity.ExecuteFault;
import com.yb.execute.service.IExecuteFaultService;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.vo.ExecuteFaultVO;
import com.yb.execute.wrapper.ExecuteFaultWrapper;
import com.yb.maintain.service.IMaintainFaultclassifyService;
import com.yb.maintain.vo.MaintainFaultclassifyVO;
import com.yb.order.service.IOrderWorkbatchService;
import com.yb.order.vo.OrderWorkbatchVO;
import com.yb.panelapi.common.TempEntity;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 停机类型
 *
 * @author by SUMMER
 * @date 2020/3/15.
 */
@RestController
@RequestMapping("/plapi/fault")
public class PApiFaultController {

    @Autowired
    private IMaintainFaultclassifyService faultclassifyService;
    @Autowired
    private IExecuteStateService stateService;
    @Autowired
    private IExecuteFaultService faultService;
    @Autowired
    private IWorkbatchOrdlinkService ordlinkService;
    @Autowired
    private IOrderWorkbatchService workbatchService;

    @RequestMapping("/list")
    List<MaintainFaultclassifyVO> getFaultList() {
        return faultclassifyService.getFaultList();
    }

    /**
     * 获取停机的类型  树状菜单
     *
     * @return
     */
    /*@RequestMapping("/list")
    List<MaintainFaultclassify> getFaultList() {
        List<MaintainFaultclassify> list = faultclassifyService.list();
        //父菜单
        List<MaintainFaultclassify> parentList = new ArrayList<>();
        for (MaintainFaultclassify faultclassify : list) {
            //当pid=0 表示为父菜单
            if (faultclassify.getPid() == 0) {
                parentList.add(faultclassify);
            } else {
                //如果不为0 说明是子菜单就需要根据pid找到父菜单
                MaintainFaultclassify parent = getParentByPid(list, faultclassify.getPid());
                parent.getChildNodes().add(faultclassify);
            }
        }
        return parentList;
    }

    private MaintainFaultclassify getParentByPid(List<MaintainFaultclassify> list, Integer pid) {
        for (MaintainFaultclassify faultclass : list) {
            if (faultclass.getId().equals(pid)) {
                return faultclass;
            }
        }
        return null;
    }*/

    /**
     * 保存设备停机记录
     * 设备停机故障记录表_yb_execute_fault
     */

    @PostMapping("/saveOrUpdate")
    public R save(@RequestBody ExecuteFaultDTO executeFault) {
        executeFault.getPid();
        executeFault.setHandleTime(new Date());  //处理时间
        executeFault.setHandle(1);  //已处理
        if (executeFault.getId() == null) {
            if (faultService.save(executeFault)) {
                return R.success("保存更新停机故障表成功");
            }
        } else {
            if (faultService.updateById(executeFault)) {
                return R.success("更新停机故障表成功");
            }
        }
        return R.fail("保存失败");
    }

    /**
     * 获取每个订单的停机列表
     */
    @GetMapping("/downList")
    public R getDownFaultList(Integer maId) {
        List<Map<String,Object>> result = new ArrayList<>();
        //通过设备id查询当前设备的工单的停机列表
        String event = "C2";   //体积
        List<TempEntity> temps = ordlinkService.getFaultAndQuality(maId, event);
        for (TempEntity temp : temps) {
            Map<String ,Object> map = new HashMap();
            List<ExecuteFaultVO> faultList = faultService.getDownFaultList(temp.getSdId());
            WorkbatchOrdlink ordlink = ordlinkService.getById(temp.getSdId());
            OrderWorkbatchVO workBatch = workbatchService.getWorkBatchByWbId(temp.getWbId());
            map.put("orderLink", ordlink);
            map.put("faultList",faultList);
            map.put("workBatch",workBatch);
            result.add(map);
        }
        return R.data(result);
    }

    /**
     * 统计未处理的停机信息条数
     *
     * @return
     */
    @RequestMapping("/count")
    R count(Integer mId) {
        int num = faultService.getCount(mId);
        return R.data(num);
    }

    /**
     * 查询停机故障表的信息
     *
     * @param fault
     * @param query
     * @return
     */
    @GetMapping("/FaultList")
    public R<IPage<ExecuteFaultVO>> list(@RequestParam Map<String, Object> fault, Query query) {
        IPage<ExecuteFault> pages = faultService.page(Condition.getPage(query),
                Condition.getQueryWrapper(fault, ExecuteFault.class));
        return R.data(ExecuteFaultWrapper.build().pageVO(pages));
    }

}
