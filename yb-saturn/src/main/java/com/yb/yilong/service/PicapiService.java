package com.yb.yilong.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.xunyue.request.XueYueOpenShiftRequest;
import com.yb.yilong.request.*;
import com.yb.yilong.response.BoxInfoNumberVO;
import com.yb.yilong.response.BoxInfoVO;
import com.yb.yilong.response.MachineDownPageVO;
import com.yb.yilong.response.WbNoInfoVO;
import org.springblade.core.tool.api.R;

import java.util.List;


/**
 * @Description:
 * @Author my
 * @Date Created in 2021/1/6 14:17
 */
public interface PicapiService {

    /**
     * 工单流程标记
     *
     * @param request
     * @return
     */
    R opShift(OpenShiftRequest request);



    /**
     * 工单上报
     *
     * @param request
     */
    void opBriefer(OpBrieferRequest request);

    /**
     * 获取工单详情
     *
     * @param request
     * @return
     */
    List<WbNoInfoVO> wbNoInfo(WbNoInfoRequest request);

    BoxInfoVO boxInfo(BoxInfoRequest request);


    BoxInfoNumberVO boxNumber(Integer maId);

    IPage<MachineDownPageVO> downPage(IPage<MachineDownPageVO> page, MachineDownPageRequest request);

    R xunYueOpShift(XueYueOpenShiftRequest request);

    /***
     * 工单执行过程的状态切换
     * @param maId
     * @param status
     * @return
     */
    R xunYueOpStatus(Integer maId, Integer status);



    /***
     * 工单执行的时候需要换班操作；工单号不要做更换
     * @param request
     * @return
     */
    R xunYueOpClasses(XueYueOpenShiftRequest request);

}
