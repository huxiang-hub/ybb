package com.yb.actset.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yb.actset.common.StaticFinaly;
import com.yb.actset.entity.ActsetCkflow;
import com.yb.actset.vo.*;
import com.yb.mater.service.IMaterProdlinkService;
import com.yb.mater.vo.MaterProdlinkVO;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.service.IWorkbatchOrdlinkService;
import org.springblade.common.resubmit.annotion.ReSubmit;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import com.yb.actset.service.IActsetCheckLogService;
import com.yb.actset.service.IActsetCkflowService;
import com.yb.actset.service.IActsetCksetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * yb_actset_checklog controller
 *
 * @author
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/actset")
@Api(value = "yb_actset_checklog", tags = "yb_actset_checklog")
public class ActsetCheckLogController extends BladeController {
    /**
     * 审核记录
     */
    private IActsetCheckLogService iActsetCheckLogService;
    /**
     * 审核层级设置
     */
    private IActsetCkflowService iActsetCkflowService;
    /**
     * 审核流程设置
     */
    private IActsetCksetService iActsetCksetService;
    /**
     * 物料查询
     */
    private IMaterProdlinkService materProdlinkService;
    /**
     * 排产
     */
    private IWorkbatchOrdlinkService ordlinkService;

    /**
     * 订单和产品的审核接口  POSTMAN已经测试
     *
     * @param modelVO
     * @return R
     * 测试数据
     * {
     * "asType":"A",
     * "awType":"produce",
     * "result":"同意",
     * "status":1 2 3 4
     * "orderId":109
     * }
     */
    @PostMapping("/orderAndProductCheck")
    @ApiOperation(value = "订单和产品的审核接口", notes = "传入订单id，或者产品id，工艺类型和 细分类型")
    @ReSubmit
    public R orderAndProductCheck(@RequestBody CheckModelVO modelVO) {
     /*   // TODO 传审核主键Id
        Map<String,Object> map = new HashMap<>();
        map.put("id",modelVO.getLogId());//审核记录主键
        map.put("db_id",modelVO.getOrderId());//待审核主键
        //找到本条记录并检查审核情况 记录没得紊乱情况只会找到一条待审核记录
        ActsetCheckLog checkLog1 =
                iActsetCheckLogService.getBaseMapper().selectByMap(map).get(0);*/
        //TODO 优化  传参需要优化前端尽量传审核主键Id
        CheckModelVO model = new CheckModelVO();
        model.setAwType(modelVO.getAwType());
        model.setAsType(modelVO.getAsType());
        model.setOrderId(modelVO.getOrderId());
        model.setStatus(StaticFinaly.CHECK_ING);//待审核
        model.setLogId(modelVO.getLogId());
        //找到本条记录并检查审核情况 记录没得紊乱情况只会找到一条待审核记录
//        List<ActsetCheckLogVO> checkLogList =
//                iActsetCheckLogService.getActsetCheckLog(model);
//        if (!Func.isEmpty(checkLogList)){
//            ActsetCheckLogVO checkLog = checkLogList.get(0);
        ActsetCheckLogVO checkLog =
                iActsetCheckLogService.getActsetCheckLog(model);
        if (checkLog != null) {
            //查看当前产品或者订单设置了有几级审核人
            Integer leave =
                    iActsetCksetService.getById(checkLog.getAsId()).getLeve();
            if (modelVO.getStatus() == StaticFinaly.CHECK_AGREE) {//同意
                checkLog.setResult(modelVO.getResult());//同意
                checkLog.setStatus(StaticFinaly.CHECK_AGREE);//状态为同意
                checkLog.setCheckTime(new Date());//设置设个时间
                iActsetCheckLogService.updateById(checkLog);//更新审核结果
                //获取下一个流程的审核信息
                ActsetCkflow ckflow =
                        iActsetCkflowService.getNextActsetCkflow(checkLog.getAwId(), leave, checkLog.getSort());
                //判断本级审核通过后是否还有下一级审核，如果有抛下级继续审核
                if (ckflow != null) {  //审核记录大于审核设置的级数 说明已经没有下一个审核流程了
                    checkLog.setResult(null); //设置下一个审核记录
                    checkLog.setStatus(StaticFinaly.CHECK_ING);//待审核
                    checkLog.setAwId(ckflow.getId());//重置下一个审核流程id
                    checkLog.setUsId(ckflow.getUsId());//设置审核人
                    checkLog.setCreateAt(new Date());//设置创建时间
                    checkLog.setResult(modelVO.getResult());//新的记录放入拒绝的理由
                    iActsetCheckLogService.save(checkLog);//添加一条下一级的待审核审核记录
                    return R.success("审核成功！");
                }
                //确认审核到最后一步了，下发到机台
                if (modelVO.getAsType().equals(StaticFinaly.AS_TYPE_ORDER)) { //订单审核到最后一步
                    WorkbatchOrdlink ordlink = ordlinkService.getById(modelVO.getOrderId());
                    ordlink.setStatus(StaticFinaly.SEND_TO_MACHINE);//下发机台
                    ordlinkService.updateById(ordlink);//更新至下发机台状态
                } else if (modelVO.getAsType().equals(StaticFinaly.AS_TYPE_PRODUCT)) {//产品审核到最后一步
                    //todo 暂时无产品审核流程
                }
                return R.success("审核成功！");
            } else if (modelVO.getStatus() == StaticFinaly.CHECK_DISAGREE) { //拒绝
                /**
                 * 5/20增加判断如果是最后一步则不跳转上一条记录批次审核，直接设置为 排产拒绝，重新提交排产
                 */
                //获取下一个流程的审核信息
                ActsetCkflow ckflowNext =
                        iActsetCkflowService.getNextActsetCkflow(checkLog.getAwId(), leave, checkLog.getSort());
                if (ckflowNext == null) { //最后一个流程做特殊处理
                    //确认审核到最后一步了，驳回到排产提交
                    if (modelVO.getAsType().equals(StaticFinaly.AS_TYPE_ORDER)) { //订单审核到最后一步
                        WorkbatchOrdlink ordlink = ordlinkService.getById(modelVO.getOrderId());
                        ordlink.setStatus(StaticFinaly.SEND_TO_MACHINE_FAI);//驳回给排产划分 并不是驳回上个流程
                        ordlink.setRemarks(modelVO.getResult());//驳回的备注
                        ordlinkService.updateById(ordlink);//更新至下发机台状
                        /**
                         * 撤销此条待审核记录记录
                         */
                        iActsetCheckLogService.removeById(checkLog.getId());//更新审核结果
                        return R.success("审核成功！");
                    } else if (modelVO.getAsType().equals(StaticFinaly.AS_TYPE_PRODUCT)) {//产品审核到最后一步
                        //todo 暂时无产品审核流程
                        return R.success("审核成功！");
                    }
                }

                checkLog.setResult(modelVO.getResult());//拒绝原因
                checkLog.setStatus(StaticFinaly.CHECK_DISAGREE);//拒绝
                checkLog.setCheckTime(new Date());//设置设个时间
                iActsetCheckLogService.updateById(checkLog);//更新审核结果
                //获取上一个流程的审核信息
                ActsetCkflow ckflow =
                        iActsetCkflowService.getUpLevelActsetCkflow(checkLog.getAwId(), leave, checkLog.getSort());
                if (ckflow != null) { //给上级添加待审核记录
                    checkLog.setResult(null); //设置下一个审核记录
                    checkLog.setStatus(StaticFinaly.CHECK_ING);//待审核
                    checkLog.setAwId(ckflow.getId());//重置上一个审核流程id
                    checkLog.setUsId(ckflow.getUsId());//设置审核人
                    checkLog.setCreateAt(new Date());//设置创建时间
                    checkLog.setResult(modelVO.getResult());//新的记录放入拒绝的理由
                    iActsetCheckLogService.save(checkLog);//添加一条下一级的待审核审核记录
                }
                return R.success("审核成功！");
            }

        }
        return R.fail("没有此条审核记录！");
    }

    /**
     * http://192.168.0.106:2020/satapi/actset/getOrderCheckLogs
     * {
     * "asType":"A",
     * "awType":"sale"
     * <p>
     * }
     * 查找待审核记录
     *
     * @param
     * @return R
     * 需要参数 asType,awType,status=1 待审核 status = 2同意的 status = 3拒绝的 status = 4 超时的
     */
    @GetMapping("/getOrderCheckLogs")
    @ApiOperation(value = "查找待审核记录", notes = "工艺类型和 细分类型")
    public R<IPage<OrderCheckModelVO>> getOrderCheckLogs(CheckModelVO modelVO, Query query) {
        /***
         *订单的审核
         */
        IPage<OrderCheckModelVO> pages =
                iActsetCheckLogService.getOrderCheckLogsAndPdName(modelVO, Condition.getPage(query));
        //查询物料
        for (OrderCheckModelVO order : pages.getRecords()) {
            List<MaterProdlinkVO> list = materProdlinkService.selectMaterProdlinkVOListByPdId(order.getPdId(), 1);
            Iterator<MaterProdlinkVO> iterator = list.iterator();
            String mlNames = "";
            String mtNums = "";
            while (iterator.hasNext()) {
                MaterProdlinkVO materProdlinkVO = iterator.next();
                mlNames = mlNames.equals("") ? materProdlinkVO.getMlName() : mlNames + ";" + materProdlinkVO.getMlName(); //输出的是wang，而不是tom
                mtNums = mtNums.equals("") ? materProdlinkVO.getMtNum() + "" : mtNums + ";" + materProdlinkVO.getMtNum(); //输出的是wang，而不是tom
            }
            order.setMlNames(mlNames);
            order.setMtNums(mtNums);
        }
        return R.data(pages);
    }

    /**
     * http://192.168.0.106:2020/satapi/actset/getProdCheckLogs   POSTMAN
     * {
     * "asType":"B",
     * "awType":"sale"
     * <p>
     * }
     * 查找待审核记录
     *
     * @param
     * @return R
     * 需要参数 asType,awType,status
     */
    @PostMapping("/getProdCheckLogs")
    @ApiOperation(value = "查找待审核记录", notes = "工艺类型和 细分类型")
    public R<IPage<ProductCheckModelVO>> getProdCheckLogs(@RequestBody CheckModelVO modelVO, Query query) {
        /**
         * 产品的审核
         */
        IPage<ProductCheckModelVO> pages =
                iActsetCheckLogService.getProdCheckLogs(modelVO, Condition.getPage(query));

        return R.data(pages);
    }

    /**
     * http://192.168.0.106:2020/satapi/actset/getProdCheckLogs POSTMAN
     * 通用 查找待审核记录
     *
     * @param
     * @return R
     * 需要参数 asType,awType,status
     */
    @PostMapping("/getStoregeCheckLogs")
    @ApiOperation(value = "查找待审核记录", notes = "工艺类型和 细分类型")
    public R<IPage<StoregeCheckModelVO>> getStoregeCheckLogs(@RequestBody CheckModelVO modelVO, Query query) {
        /**
         * 仓促的审核 todo 还不知道有没得这个审核
         */
        IPage<StoregeCheckModelVO> pages =
                iActsetCheckLogService.getStoregeCheckLogs(modelVO, Condition.getPage(query));
        return R.data(pages);
    }


}
