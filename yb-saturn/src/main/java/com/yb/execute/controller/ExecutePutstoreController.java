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
package com.yb.execute.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.execute.entity.ExecutePutStore;
import com.yb.execute.entity.ExecuteState;
import com.yb.execute.service.ExecutePutstoreService;
import com.yb.execute.service.IExecuteStateService;
import com.yb.execute.vo.ExecuteBrieferVO;
import com.yb.execute.vo.ExecutePutStoreVO;
import com.yb.panelapi.common.UpdateStateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * 生产执行上报信息_yb_execute_briefer 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/executeputstore")
@Api(value = "生产执行上报信息_yb_execute_briefer", tags = "生产执行上报信息_yb_execute_briefer接口")
public class ExecutePutstoreController extends BladeController {

    private ExecutePutstoreService executePutstoreService;
    private IExecuteStateService stateService;

    /**
     * 分页 生产执行上报信息_yb_execute_briefer
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入executeBriefer")
    public R<IPage<ExecuteBrieferVO>> list(Query query, ExecuteBrieferVO executeBrieferVO) {
        Integer courrent = (query.getCurrent() - 1) * query.getSize();
        IPage<ExecuteBrieferVO> page = executePutstoreService.pagePutstoreList(courrent, query.getSize(), executeBrieferVO);

        return R.data(page);

    }
/***********************************QIBO***********************************************/

    /**
     * 自定义分页 质量检查废品表_yb_execute_putstore
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "executePutStore")
    public R<IPage<ExecutePutStoreVO>> page(ExecutePutStoreVO executePutStore, Query query) {
        IPage<ExecutePutStoreVO> pages = executePutstoreService.selectExecutePutStorePage(Condition.getPage(query), executePutStore);
        return R.data(pages);
    }

    /**
     * 修改 质量检查废品表_yb_execute_putstore
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "executePutStore")
    public R update(@Valid @RequestBody ExecutePutStoreVO executePutStore) {
        if (executePutStore == null) return R.fail("入库失败！");
        ExecutePutStore putStore =
                executePutstoreService.getById(executePutStore.getId());
        /**
         * 情况一:正常入库
         * 情况二：签收部分入库
         * 情况三：拒绝入库
         */
        /****
         *
         */
        Date time = new Date();
        //正常入库
        if (executePutStore.getStatus() == 2) {
            if (putStore.getPutNum() == executePutStore.getPutNum()
                    && putStore.getPutAddr().equals(executePutStore.getPutAddr())) {
                ExecuteState state = stateService.getById(putStore.getExId());//TODO EXID需要更换exId对象内容 wyn

                state.setEvent("D3");//设置为入库事件
                state.setStatus("D");//设置为入库事件
                state.setCreateAt(time);
                state.setStartAt(time);
                /***
                 * 启用拦截
                 */
                UpdateStateUtils.updateSupervise(state, null);
                //修改为入库
                putStore.setStatus(2);//已签收
                putStore.setReceiverNum(executePutStore.getReceiverNum());//设置签收数量
                putStore.setReceiverTime(time);//签收时间
                executePutstoreService.updateById(putStore);
            } else { //签收之部分签收情况 要考虑全部抛给下一个库签收的情况 也就是本库签收未0 下个库全签收
                if (putStore.getPutNum() < executePutStore.getReceiverNum() ||
                        executePutStore.getReceiverNum() < 0) {
                    return R.fail("请检查你的入库数！");
                } else {
                    //设置部分入库的数量
                    putStore.setStatus(2);//签收
                    putStore.setReceiverTime(time);//设置签收时间
                    putStore.setReceiverNum(executePutStore.getReceiverNum());//设置签收的数量
                    executePutstoreService.updateById(putStore);
                    if (executePutStore.getNewPutNum() != 0 && !"".equals(executePutStore.getNewPutAddr())
                            && executePutStore.getNewPutNum() != null && executePutStore.getNewPutAddr() != null) {
                        //新增一条待入库的信息（别的库）
                        putStore.setId(null);//设置为null，避免主键重复
                        putStore.setPutNum(executePutStore.getNewPutNum());//设置数量
                        putStore.setReceiverNum(null);//清空签收数
                        putStore.setPutAddr(executePutStore.getNewPutAddr());//设置新的库
                        putStore.setStatus(1);//待签收
                        putStore.setReceiver(null);//置空签收人
                        putStore.setReceiverTime(null);//清空签收时间
                        putStore.setCreateAt(time);//重置创建时间
                        putStore.setPutTime(time);//重置入库时间
                        putStore.setResult(null);//情况原因
                        executePutstoreService.save(putStore);//新增
                    }
                }
            }
        } else if (executePutStore.getStatus() == 3) { //拒绝签收
            //更新数据
            putStore.setResult(executePutStore.getResult());//设置拒绝签收原因
            putStore.setReceiverTime(time);//拒绝签收时间
            putStore.setStatus(executePutStore.getStatus());//设置拒绝签收
            executePutstoreService.updateById(putStore);
        }
        return R.success("操作成功！");
    }


}
