package com.yb.prod.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.process.entity.ProcessClassify;
import com.yb.process.vo.ProcessClassifyVO;
import com.yb.process.wrapper.ProcessClassifyWrapper;
import com.yb.prod.entity.ProdClassify;
import com.yb.prod.entity.ProdDiff;
import com.yb.prod.service.IProdClassifyService;
import com.yb.prod.service.IProdDiffService;
import com.yb.prod.vo.ProdDiffVO;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/procdiff")
public class ProdDiffController {

    private IProdDiffService prodDiffService;
    private IProdClassifyService classifyService;

    /**
     * 获取全部产品分类的难用程度
     * 自定义分页
     */
    @RequestMapping("/getAllProcdiff")
    public R<IPage<ProdDiffVO>> getAllProcdiff(ProdDiffVO prodDiffVO, Query query){
        IPage<ProdDiffVO> pages =
                prodDiffService.getAllProcdiff(Condition.getPage(query),prodDiffVO);
        return R.data(pages);
    }

    /**
     * 详情
     */
    @RequestMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入processClassify")
    public R<ProdDiffVO> detail(ProdDiffVO prodDiffVO) {
        ProdDiffVO detail =
                prodDiffService.detail(prodDiffVO.getId());
        return R.data(detail);
    }

    /**
     * 更新修改
     * @param prodDiffVO
     * @return
     */
    @RequestMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入processClassify")
    public R update(@Valid @RequestBody ProdDiffVO prodDiffVO) {
        /**
         * 修改分类名称，编号， 修改工序 和 难易程度
         */
        ProdClassify classify =
                classifyService.getById(prodDiffVO.getId());//获取产品分类
        classify.setClassify(prodDiffVO.getClassify());
        classify.setClName(prodDiffVO.getClName());
        classify.setIsUsed(prodDiffVO.getIsUsed());
        // classify.setPrNum(prodDiffVO.getPrNum());
        //classify.setSort(prodDiffVO.getSort());
        classifyService.updateById(classify);
        //修改中间表的难易程度 和工序
        ProdDiff prodDiff =
                prodDiffService.getById(prodDiffVO.getDiffId());//获取产品分类难度中间表
        if (prodDiff==null) { //没有绑定难度就生成一个
            prodDiff = new ProdDiff();
            prodDiff.setPcId(classify.getId());//外键关联
            prodDiff.setDiff(prodDiffVO.getDiff());//更新难度系数
            prodDiff.setPrId(prodDiffVO.getPrId());//更新工序
            prodDiff.setIsUsed(prodDiffVO.getIsUsed());//同步启用状态
            prodDiffService.save(prodDiff);
        }else{ //绑定了就修改
            prodDiff.setDiff(prodDiffVO.getDiff());//更新难度系数
            prodDiff.setPrId(prodDiffVO.getPrId());//更新工序
            prodDiff.setIsUsed(prodDiffVO.getIsUsed());//同步启用状态
            prodDiffService.updateById(prodDiff);
        }

        return R.success("OK");
    }

    /**
     * 新增
     * @param prodDiffVO
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public R save(@RequestBody ProdDiffVO prodDiffVO) {
        /*生成编号操作*/
        List<ProdClassify> classifies = classifyService.list();
        Integer addClassifyNum = 0;
        if (!classifies.isEmpty()) {
            for (ProdClassify itemPro : classifies){
                //如果数据库不规范就会报错
                try {
                    Integer maxClassifyNum = Integer.parseInt(itemPro.getClassify().trim());
                    if (maxClassifyNum > addClassifyNum) { //找到最大的 然后+1
                        addClassifyNum = maxClassifyNum;
                    }
                }catch (Exception e){
                    System.err.println("数字格式类型装换错误！");
                }
            }
        }

        /*新增产品分类*/
        ProdClassify classify = new ProdClassify();
        if (addClassifyNum >= 9){
            prodDiffVO.setClassify(""+(addClassifyNum+1));
        }else {
            prodDiffVO.setClassify("0"+(addClassifyNum+1));
        }
        classify.setClName(prodDiffVO.getClName());
        classify.setClassify(prodDiffVO.getClassify());
        classify.setSort(prodDiffVO.getSort());
        classify.setIsUsed(prodDiffVO.getIsUsed());
        classify.setPrNum(prodDiffVO.getPrNum());
        classifyService.save(classify);
        /*新增难易分类*/
        ProdDiff prodDiff = new ProdDiff();
        prodDiff.setIsUsed(prodDiffVO.getIsUsed());
        prodDiff.setPrId(prodDiffVO.getPrId());
        prodDiff.setDiff(prodDiffVO.getDiff());
        prodDiff.setPcId(classify.getId()); //设置产品分类主键到中间表
        prodDiffService.save(prodDiff);
        return R.success("OK");
    }


    /**物理删除（删除就不可找回）**/
    /**
     * 删除
     */
    @RequestMapping("/remove")
    public R remove(@RequestParam String diffIds) {
            Map<String,Object> map = new HashMap<>();
            for(Integer id : Func.toIntList(diffIds)){ //遍历出每一个中间表ID
                //通过中间的主键 查找出 产品分类Id，
                ProdDiff prodDiff = prodDiffService.getById(id);
                //在用产品的主键pcId看有几条记录
                map.put("pc_id",prodDiff.getPcId());
                   List<ProdDiff>  prodDiffs = prodDiffService.getBaseMapper().selectByMap(map);
                if (!prodDiffs.isEmpty()&&prodDiffs.size()>1) { //记录表中不只有一条记录 ，只删除需要删除的中间表

                    prodDiffService.removeById(id);//只删除中间表的记录
                    return R.status(true);
                }else if (prodDiffs.size()==1) { //中间表和产品分类都删除

                    classifyService.removeById(prodDiff.getPcId());
                    prodDiffService.removeById(prodDiff.getId());//只删除中间表的记录
                    return R.status(true);
                }
                map.clear();//清空map
            }
        return R.status(false);
    }

}
