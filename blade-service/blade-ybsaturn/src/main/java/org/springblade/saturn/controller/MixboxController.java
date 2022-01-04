
package org.springblade.saturn.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.saturn.entity.MachineMainfo;
import org.springblade.saturn.entity.Mixbox;
import org.springblade.saturn.entity.SuperviseBoxinfo;
import org.springblade.saturn.entity.SuperviseExecute;
import org.springblade.saturn.service.IMixboxService;
import org.springblade.saturn.service.ISuperviseBoxinfoService;
import org.springblade.saturn.service.ISuperviseExecuteService;
import org.springblade.saturn.tool.ExcelUtil;
import org.springblade.saturn.vo.ExcelErrorVo;
import org.springblade.saturn.vo.MixboxVO;
import org.springblade.saturn.wrapper.MixboxWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 印联盒（本租户的盒子），由总表分发出去 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/box/machinemixbox")
@Api(value = "印联盒（本租户的盒子），由总表分发出去", tags = "印联盒（本租户的盒子），由总表分发出去接口")
public class MixboxController extends BladeController {

    private IMixboxService machineMixboxService;
    private ISuperviseBoxinfoService superviseBoxinfoService;
    private ISuperviseExecuteService superviseExecuteService;


    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineMixbox")
    public R<MixboxVO> detail(Mixbox machineMixbox) {
      Mixbox detail = machineMixboxService.getOne(Condition.getQueryWrapper(machineMixbox));
        return R.data(MixboxWrapper.build().entityVO(detail));
    }

    /**
     * 分页 印联盒（本租户的盒子），由总表分发出去
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineMixbox")
    public R<IPage<MixboxVO>> list(Mixbox machineMixbox, Query query) {
        IPage<Mixbox> pages = machineMixboxService.page(Condition.getPage(query), Condition.getQueryWrapper(machineMixbox));
        return R.data(MixboxWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 印联盒（本租户的盒子），由总表分发出去
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineMixbox")
    public R<IPage<MixboxVO>> page(Query query,MixboxVO mixboxVO) {
        IPage<MixboxVO> pages = machineMixboxService.getMachineMixboxPage(Condition.getPage(query), mixboxVO);
        return R.data(pages);
    }

    /**
     * 新增 印联盒（本租户的盒子），由总表分发出去
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineMixbox")
    public R save(@Valid @RequestBody Mixbox machineMixbox) {
        /**
         * 如果不设置为0会报错 数据库主键重复异常
         */
        try {
            machineMixboxService.save(machineMixbox);
            if (machineMixbox.getMaId() != null){
//                初始化boxinfo与execute表
                SuperviseBoxinfo superviseBoxinfo = new SuperviseBoxinfo();
                superviseBoxinfo.setUuid(machineMixbox.getUuid());
                superviseBoxinfo.setMaId(machineMixbox.getMaId());
                superviseBoxinfoService.save(superviseBoxinfo);
                SuperviseExecute superviseExecute = new SuperviseExecute();
                superviseExecute.setUuid(machineMixbox.getUuid());
                superviseExecute.setMaId(machineMixbox.getMaId());
                superviseExecuteService.save(superviseExecute);
            }
            return R.success("新增成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("新增失败");
        }
    }

    /**
     * 修改 印联盒（本租户的盒子），由总表分发出去
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineMixbox")
    public R update(@Valid @RequestBody Mixbox machineMixbox) {
        return R.status(machineMixboxService.updateById(machineMixbox));
    }

    /**
     * 新增或修改 印联盒（本租户的盒子），由总表分发出去
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineMixbox")
    public R submit(@Valid @RequestBody Mixbox machineMixbox) {
        try {
            //        查询之前状态
            Mixbox machineMixbox1 = machineMixboxService.getById(machineMixbox.getId());
            //        修改前没有绑定，修改后绑定了
            if (machineMixbox1.getMaId() == -1){
                if(machineMixbox.getMaId() != null && machineMixbox.getMaId() != -1){
//                    查询之前是否存在boxinfo与execute的数据
                    Map<String,Object> map = new HashMap<>();
                    map.put("uuid",machineMixbox.getUuid());
                    List<SuperviseBoxinfo> listBoxinfo = superviseBoxinfoService.listByMap(map);
                    List<SuperviseExecute> listExecute = superviseExecuteService.listByMap(map);
                    if (!Func.isEmpty(listBoxinfo)){
//                        存在修改maid
                        SuperviseBoxinfo superviseBoxinfo = listBoxinfo.get(0);
                        superviseBoxinfo.setMaId(machineMixbox.getMaId());
                        superviseBoxinfoService.updateById(superviseBoxinfo);
                    }else{
                        //           不存在则 绑定
                        //              增加对应的boxinfo数据
                        SuperviseBoxinfo superviseBoxinfo = new SuperviseBoxinfo();
                        superviseBoxinfo.setUuid(machineMixbox.getUuid());
                        superviseBoxinfo.setMaId(machineMixbox.getMaId());
                        superviseBoxinfoService.save(superviseBoxinfo);
                    }
                    if (!Func.isEmpty(listExecute)){
                        SuperviseExecute superviseExecute = listExecute.get(0);
                        superviseExecute.setMaId(machineMixbox.getMaId());
                        superviseExecuteService.updateById(superviseExecute);
                    }else {
                        //           不存在则 绑定
                        //              增加对应的execute的数据
                        SuperviseExecute superviseExecute = new SuperviseExecute();
                        superviseExecute.setUuid(machineMixbox.getUuid());
                        superviseExecute.setMaId(machineMixbox.getMaId());
                        superviseExecuteService.save(superviseExecute);
                    }
                }
            }else if(machineMixbox1.getMaId() != -1 && machineMixbox1.getMaId() != null){
                //            修改绑定的设备
                if(machineMixbox.getMaId() == null){
                    //             解绑
                    //                删除对应的boxinfo与execute的数据
                    Map<String,Object> map = new HashMap<>();
                    map.put("uuid",machineMixbox.getUuid());
                    List<SuperviseBoxinfo> listBoxinfo = superviseBoxinfoService.listByMap(map);
                    List<SuperviseExecute> listExecute = superviseExecuteService.listByMap(map);
                    if (!Func.isEmpty(listBoxinfo)){
                        SuperviseBoxinfo superviseBoxinfo = listBoxinfo.get(0);
                        superviseBoxinfo.setMaId(-1);
                        superviseBoxinfoService.updateById(superviseBoxinfo);
                    }
                    if (!Func.isEmpty(listExecute)){
                        SuperviseExecute superviseExecute = listExecute.get(0);
                        superviseExecute.setMaId(-1);
                        superviseExecuteService.updateById(superviseExecute);
                    }
                    machineMixbox.setMaId(-1);
                }else {
                    //             换绑
                    //                修改对应的boxinfo与execute的数据
                    //                boxinfo与execute表相关的其它数据会清空
                    Map<String,Object> map = new HashMap<>();
                    map.put("uuid",machineMixbox.getUuid());
                    SuperviseBoxinfo superviseBoxinfo = new SuperviseBoxinfo();
                    superviseBoxinfoService.removeByMap(map);
                    superviseBoxinfo.setUuid(machineMixbox.getUuid());
                    superviseBoxinfo.setMaId(machineMixbox.getMaId());
                    SuperviseExecute superviseExecute = new SuperviseExecute();
                    superviseExecuteService.removeByMap(map);
                    superviseExecute.setUuid(machineMixbox.getUuid());
                    superviseExecute.setMaId(machineMixbox.getMaId());

                    superviseBoxinfoService.saveOrUpdate(superviseBoxinfo);
                    superviseExecuteService.saveOrUpdate(superviseExecute);

                }
            }
            machineMixboxService.updateById(machineMixbox);
            return R.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("修改失败");
        }
    }


    /**
     * 删除 印联盒（本租户的盒子），由总表分发出去
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    @Transactional
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        try{
            for (Integer id : Func.toIntList(ids)){
                //        刪除对应的boxinfo，execute信息
                Map<String,Object> map = new HashMap<>();
                map.put("uuid", machineMixboxService.getById(id).getUuid());
                superviseBoxinfoService.removeByMap(map);
                superviseExecuteService.removeByMap(map);
            }
            machineMixboxService.removeByIds(Func.toIntList(ids));
            return R.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return R.fail("修改失败");
        }
    }


    @PostMapping("/fileExcel")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "Excel导入盒子信息", notes = "传入file")
    public R fileExcel(@RequestParam(value = "file") MultipartFile file) throws IOException {
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        } catch (Exception e) {
            System.out.println("Excel data file cannot be found!");
        }
       try {
            //读取excel文件
            Map<String, String> maps = new HashMap<>();
            //machine_mixbox
           maps.put("盒子编号", "uuid"); // 必填
           maps.put("绑定的设备", "maIdStr");  // 选填 maId
           maps.put("硬件类型", "hdIdStr"); // 选填
           maps.put("是否激活", "active"); // 转 0 or 1  选填
           maps.put("出库状态", "depositoryStr");//转到 depository 0 1 2 3 4 //选填
           maps.put("mac地址", "mac");//转到地址  选填
           maps.put("品牌", "brand");//  选填
           maps.put("规格", "specs"); // 选填
           maps.put("标准速度", "speed"); // 选填
           Set<String> errorList = new HashSet<>();
           ExcelErrorVo excelErrorVo = ExcelUtil.readExcel(xssfWorkbook, 0, maps, errorList);
           errorList = excelErrorVo.getErrorMessageList();
           List<Map<String, String>> mapList = excelErrorVo.getResultList();
           mapList.remove(0);
           JSONArray jsonData = JSONArray.fromObject(mapList);
           /**
            *TODO  处理下空行返回
            */
           List<MixboxVO> MixboxVOs =
                   (List<MixboxVO>) JSONArray.toList(JSONArray.fromObject(mapList), MixboxVO.class);
           List<String> uuidList = new ArrayList<>(); //判断excel表中是否有uuid重复
           Map<String, Integer> hdId = new HashMap<>(); //*暂时先写死*//*
           hdId.put("白盒", 0);
           hdId.put("黑盒", 1);
           Map<String, Integer> model = new HashMap<>();
           model.put("未出场", 1);
           model.put("出厂调试", 2);
           model.put("生产运行", 3);
           model.put("停用", 4);
           int count = 0 ;//记录行数
           /**
            * 只需要判断主要内容 ，必填内容 uuid 为必填内容 不可为空 不可重复
            */
           for(MixboxVO  mixboxVO : MixboxVOs){
               /**
                * 先验证输入的字符串是否超长，等
                *  todo
                */
               if (!"".equals(mixboxVO.getMaIdStr())&&mixboxVO.getMaIdStr().length()>20){
                   errorList.add("第" + count + "行设备超出字数限制！");
               }else{
                   /**
                    * todo 没有判断maIdStr不是数字类型字符串的情况
                    */
                   mixboxVO.setMaId(Integer.parseInt(mixboxVO.getMaIdStr()));
               }
               if (!"".equals(mixboxVO.getDepositoryStr())&&mixboxVO.getDepositoryStr().length()>20){
                   errorList.add("第" + count + "行出库状态超出字数限制！");
               }
               if (!"".equals(mixboxVO.getMac())&&mixboxVO.getMac().length()>20){
                   errorList.add("第" + count + "行盒子mac超出字数限制！");
               }
               if (!"".equals(mixboxVO.getBrand())&&mixboxVO.getBrand().length()>20){
                   errorList.add("第" + count + "行盒子品牌超出字数限制！");
               }
               if (!"".equals(mixboxVO.getSpecs())&&mixboxVO.getSpecs().length()>20){
                   errorList.add("第" + count + "行盒子规格超出字数限制！");
               }
               if (!"".equals(mixboxVO.getSpeed())&&mixboxVO.getSpeed().length()>20){
                   errorList.add("第" + count + "行盒子速度超出字数限制！");
               }
                    count +=1;//记录错误行数
               if ("".equals(mixboxVO.getUuid())||mixboxVO.getUuid()==null) {
                   errorList.add("第" + count + "行盒子编号为空！");
               }else {
                   if (machineMixboxService.findMixboxIsExit(mixboxVO.getUuid())
                           !=null) {
                       errorList.add("第" + count + "行盒子编号已存在！");
                   }else{
                       if (uuidList.contains(mixboxVO.getUuid())) {
                           errorList.add("第" + count + "行盒子编号重复！");
                       }else {
                           /**uuid判断结束**/
                           /**其他字段简单的 判断 选填的 判断字符不超长就好*/
                            //判断输入类型为设定类型  白盒 or 黑盒
                            boolean isHdId = false;
                            //填了hdId的情况
                           if (! "".equals(mixboxVO.getHdIdStr())) {

                               for(String key : hdId.keySet()){

                                   if (key.equals(mixboxVO.getHdIdStr())) {
                                       isHdId = true; //类型输入正确
                                       mixboxVO.setHdId(hdId.get(key));
                                       break;
                                   }
                               }
                               if(!isHdId){
                                   errorList.add("第" + count + "行盒子型号不存在！");
                               }else{

                                   //填了盒子状态的情况下
                                   if (!"".equals(mixboxVO.getDepositoryStr())) {
                                       boolean isModel =false;
                                       //判断输入类型为设定类型  出厂，停用 ,等类型...
                                       for(String key : model.keySet()){
                                           if (key.equals(mixboxVO.getDepositoryStr())) {
                                               isModel = true; //类型输入正确
                                               mixboxVO.setDepository(model.get(key));
                                           }
                                       }
                                       if(!isModel){
                                           errorList.add("第" + count + "行盒子状态错误！");
                                       }
                                   }
                               }
                           }
                       }
                   }
               }
               if (errorList.size()!=0) {
                   return R.data(errorList,"导入数据异常，请检查！");
               }
           }

           for(MixboxVO  mixboxVO : MixboxVOs){
               machineMixboxService.save(mixboxVO);
               if (mixboxVO.getMaId() != null){
//                   如果绑定了设备，增加相关的boxinfo与execute
                   SuperviseBoxinfo superviseBoxinfo = new SuperviseBoxinfo();
                   superviseBoxinfo.setUuid(mixboxVO.getUuid());
                   superviseBoxinfo.setMaId(mixboxVO.getMaId());
                   superviseBoxinfoService.save(superviseBoxinfo);
                   SuperviseExecute superviseExecute = new SuperviseExecute();
                   superviseExecute.setUuid(mixboxVO.getUuid());
                   superviseExecute.setMaId(mixboxVO.getMaId());
                   superviseExecuteService.save(superviseExecute);
               }
           }
           return R.success("导入成功！");
    }catch (Exception e){
           e.printStackTrace();
           return R.fail("导入数据异常,请使用模板");
       }
    }


    /**
     * 查询未绑定盒子的设备信息系
     */
    @RequestMapping("/selectMachineList")
    @ApiOperationSupport(order = 7)
    public R<List<MachineMainfo>> selectMachineList() {
        List<MachineMainfo> machineMainfos = machineMixboxService.selectMachineList();
        return R.data(machineMainfos);
    }

}
