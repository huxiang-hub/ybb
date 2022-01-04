package com.yb.machine.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.machine.entity.MachineClassify;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.machine.request.MonitorRequest;
import com.yb.machine.response.MachineMonitorVO;
import com.yb.machine.response.MonitorCapacityAnalyVO;
import com.yb.machine.service.IMachineClassifyService;
import com.yb.machine.service.IMachineMainfoService;
import com.yb.machine.service.IMachineMixboxService;
import com.yb.machine.utils.QRCodeUtil;
import com.yb.machine.vo.MachineMainfoVO;
import com.yb.machine.vo.TodayCapacityVO;
import com.yb.machine.wrapper.MachineMainfoWrapper;
import com.yb.process.entity.ProcessMachlink;
import com.yb.process.entity.ProcessWorkinfo;
import com.yb.process.service.IProcessMachlinkService;
import com.yb.process.service.IProcessWorkinfoService;
import com.yb.supervise.service.ISuperviseBoxinfoService;
import com.yb.supervise.service.ISuperviseExecuteService;
import com.yb.system.dict.entity.Dict;
import com.yb.system.dict.service.SaIDictService;
import com.yb.system.dict.vo.DictVO;
import com.yb.workbatch.excelUtils.ExcelUtil;
import com.yb.workbatch.excelUtils.ExportlUtil;
import com.yb.workbatch.vo.ExcelErrorVo;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备_yb_mach_mainfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/machinemainfo")
@Api(value = "设备_yb_mach_mainfo", tags = "设备_yb_mach_mainfo接口")
@CrossOrigin
public class MachineMainfoController extends BladeController {
    private IMachineMainfoService machineMainfoService;
    private IMachineMixboxService machineMixboxService;
    private IBaseDeptinfoService deptinfoService;
    private IMachineClassifyService classifyService;
    private IProcessMachlinkService machlinkService;
    private ISuperviseBoxinfoService superviseBoxinfoService;
    private ISuperviseExecuteService superviseExecuteService;

    private IMachineClassifyService machineClassifyService;
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private SaIDictService saIDictService;
    @Autowired
    private IProcessWorkinfoService processWorkinfoService;


    /**
     * 生成设备二维吗返回给浏览器处理
     */
    @GetMapping("/getMachineQRcode")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "生成设备对应二维码", notes = "传入id")
    public R getMachineQRcode(Integer maId, HttpServletResponse response) {
        MachineMainfo mainfo = machineMainfoService.getById(maId);
        if (mainfo == null) {
            return R.fail("设备不存在");
        }
        String value = "https://www.baidu.com" + maId;
        BufferedOutputStream outputStream = null;
        try {
            outputStream = ExportlUtil.getBufferedOutputStream("设备二维码.jpg", response);
            QRCodeUtil.encode(value, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 查询所有设备类型下所有设备
     */
    @RequestMapping("/MainchineClassiflyList")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "")
    public R MainchineClassiflyList() {
        return R.data(machineMainfoService.MainchineClassiflyList());
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入machineMainfo")
    public R detail(MachineMainfoVO machineMainfo) {
        Integer maId = machineMainfo.getId();
        if (maId == null) {
            maId = machineMainfo.getMaId();
        }
        return R.data(machineMainfoService.getMachineMainfoById(maId));
    }

    /**
     * 分页 设备_yb_mach_mainfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入machineMainfo")
    public R<IPage<MachineMainfoVO>> list(MachineMainfo machineMainfo, Query query) {
        IPage<MachineMainfo> pages = machineMainfoService.page(Condition.getPage(query), Condition.getQueryWrapper(machineMainfo));
        return R.data(MachineMainfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 设备_yb_mach_mainfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入machineMainfo")
    public R<IPage<MachineMainfoVO>> page(Integer dpId, Integer prId, Integer status,
                                          Query query) {
        IPage<MachineMainfoVO> pages =
                machineMainfoService.selectMachineMainfoPage(Condition.getPage(query),
                        dpId, prId, status);
        return R.data(pages);
    }

    /**
     * 新增 设备_yb_mach_mainfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "传入machineMainfo")
    public R save(@Valid @RequestBody MachineMainfo machineMainfo) {
        machineMainfoService.save(machineMainfo);
        ProcessMachlink machlink = new ProcessMachlink(); //新增工序关联设备中间表
        machlink.setPrId(machineMainfo.getProId());//设置主要工序Id
        machlink.setMaId(machineMainfo.getId());//设置主要工序Id
        //machlink.setSpeed();
        machlinkService.save(machlink);
        return R.data(machineMainfo);
    }

    /**
     * 修改 设备_yb_mach_mainfo
     */
    @PostMapping("/update")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入machineMainfo")
    public R update(@Valid @RequestBody MachineMainfo machineMainfo) {
        return R.status(machineMainfoService.updateById(machineMainfo));
    }

    /**
     * 新增或修改 设备_yb_mach_mainfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入machineMainfo")
    public R submit(@Valid @RequestBody MachineMainfo machineMainfo) {
        /**
         * 主要工序修改后，要增加工序关联设备表
         */
        //先查找此工序中间表 有就修改没得就新增
        Map<String, Object> map = new HashMap<>();
        //获取之前的主要工序ID
        MachineMainfo oldMachlink =
                machineMainfoService.getById(machineMainfo.getId());
        if (oldMachlink != null) {
            Integer oldPrId = oldMachlink.getProId();
            map.clear();//清除数据
            map.put("ma_id", oldMachlink.getId()); //设备id
            map.put("pr_id", oldPrId);//设备的主要助攻ID
        } else {
            return null;
        }
        List<ProcessMachlink> machlinks =
                machlinkService.getBaseMapper().selectByMap(map);
        if (!machlinks.isEmpty()) { //存在就直接修改 主要工序
            ProcessMachlink machlink = machlinks.get(0);
            machlink.setPrId(machineMainfo.getProId());//修改的主要工序ID
            machlinkService.updateById(machlink);
        } else { //没有就增加一个主要工序关联
            ProcessMachlink machlink = new ProcessMachlink();
            machlink.setPrId(machineMainfo.getProId());
            machlink.setMaId(machineMainfo.getId());
            // TODO 转数从哪儿来
            machlinkService.save(machlink);
        }
        return R.status(machineMainfoService.saveOrUpdate(machineMainfo));
    }


    /**
     * 删除 设备_yb_mach_mainfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    @Transactional
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestBody List<Integer> ids) {
        try {
//        解绑删除设备的相关盒子
            machineMixboxService.setMixboxByListMaId(ids);
//        删除相关设备
            superviseBoxinfoService.removerListByMaid(ids);
            superviseExecuteService.removerListByMaid(ids);
//删除
            machineMainfoService.removeByIds(ids);
            return R.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("删除失败");
        }
    }


    /**
     * 通过部门和工序获取设备列表（信息）
     */
    @PostMapping("/getMachinsByDpIdPrId")
    @ApiOperation(value = "删除", notes = "传入ids")
    public R getMachinsByDpIdPrId(Integer maId, Integer dpId, Integer prId, Integer status) {
        List<MachineMainfoVO> list =
                machineMainfoService.getMachinsByDpIdPrId(maId, dpId, prId, status);
        return R.data(list);
    }

    /**
     * 设备管理分页
     *
     * @param query
     * @param mainfoVO
     * @return
     */
    @RequestMapping("/machineMainInfoPage")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "设备分页", notes = "")
    public R<IPage<MachineMainfoVO>> page(Query query, MachineMainfoVO mainfoVO) {
        if (mainfoVO != null) {
            String ids = mainfoVO.getIds();
            if (!StringUtil.isEmpty(ids)) {
                mainfoVO.setIdList(Func.toIntList(ids));
            }
        }
        IPage<MachineMainfoVO> pages =
                machineMainfoService.getAllMachinePages(Condition.getPage(query), mainfoVO);
        return R.data(pages);
    }

/*
    @RequestMapping("/manames")
    @ApiOperation(value = "部件名", notes = "传入prId")
    public List<MachineMainfoVO> selectMaNames(Integer prId) {
        return machineMainfoService.selectMaNames(prId);
    }
*/

    @RequestMapping("/deptMachineList")
    @ApiOperation(value = "部件名", notes = "传入prId")
    public List<MachineMainfo> machineListByDeptId(Integer prId, Integer dpId) {
        return machineMainfoService.machineListByDeptId(prId, dpId);
    }

    @GetMapping("/machineList")
    @ApiOperation(value = "所有的机器名称", notes = "传入prId")
    public List<MachineMainfo> getMachineLsit() {
        return machineMainfoService.list();
    }

    @PostMapping("/fileExcel")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "Excel导入员工信息", notes = "传入file")
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
            //yb_mach_mainfo
            maps.put("设备编号", "mno"); // 必填
            maps.put("设备型号", "model");  // 选填 maId
            maps.put("设备名称", "name"); // 必填
            maps.put("设备类型", "maType"); // 必填
            maps.put("主工序", "prName"); // 必填
            maps.put("所属车间", "dpName"); // 转 0 or 1  选填
            maps.put("品牌", "brand");//  选填
            maps.put("规格", "specs"); // 选填
            maps.put("标准速度", "speed"); // 选填
            maps.put("图片", "image"); // 选填
            Set<String> errorList = new HashSet<>();
            ExcelErrorVo excelErrorVo = ExcelUtil.readExcel(xssfWorkbook, 0, maps, errorList);
            errorList = excelErrorVo.getErrorMessageList();
            List<Map<String, String>> mapList = excelErrorVo.getResultList();
            mapList.remove(0);
            JSONArray jsonData = JSONArray.fromObject(mapList);
            /**
             *TODO  处理下空行返回
             */
            List<MachineMainfoVO> mainfoVOS =
                    (List<MachineMainfoVO>) JSONArray.toList(JSONArray.fromObject(mapList), MachineMainfoVO.class);
            List<String> mnos = new ArrayList<>(); //判断excel表中是否有设备编号重复
            List<String> names = new ArrayList<>(); //判断excel表中是否有设备名字重复
            int count = 0;//记录行数
            /**
             * 只需要判断主要内容 ，必填内容nama,mno 为必填内容 不可为空 不可重复
             */

            for (MachineMainfoVO mainfoVO : mainfoVOS) {
                count += 1;//记录错误行数
                /**
                 * 先验证输入的字符串是否超长，等  基本判断
                 *  todo
                 */
                if ("".equals(mainfoVO.getMno()) || mainfoVO.getMno() == null || mainfoVO.getMno().length() > 40) {
                    errorList.add("第" + count + "行设备超出字数限制或者为空！");
                    mnos.add(mainfoVO.getMno());//放进集合判断是否重复
                }
                if ("".equals(mainfoVO.getMaType()) || mainfoVO.getMaType() == null || mainfoVO.getMaType().length() > 40) {
                    errorList.add("第" + count + "行设备类型超出字数限制或者为空！");
                    mnos.add(mainfoVO.getMno());//放进集合判断是否重复
                }
                if ("".equals(mainfoVO.getName()) || mainfoVO.getName() == null || mainfoVO.getName().length() > 20) {
                    errorList.add("第" + count + "行设备名称超出字数限制或者为空！");
                    names.add(mainfoVO.getName());//放进集合判断是否重复
                }
                if ("".equals(mainfoVO.getDpName()) || mainfoVO.getDpName() == null || mainfoVO.getDpName().length() > 20) {
                    errorList.add("第" + count + "行所属车间超出字数限制或者为空！");
                }
                if ("".equals(mainfoVO.getPrName()) || mainfoVO.getPrName() == null || mainfoVO.getPrName().length() > 20) {
                    errorList.add("第" + count + "行所属主工序超出字数限制或者为空！");
                }
                if (mainfoVO.getModel().length() > 20) {
                    errorList.add("第" + count + "行设备型号超出字数限制或者为空！");
                }
                if (mainfoVO.getBrand().length() > 20) {
                    errorList.add("第" + count + "行设备品牌超出字数限制或者为空！");
                }
                if (mainfoVO.getSpecs().length() > 20) {
                    errorList.add("第" + count + "行设备规格超出字数限制或者为空！");
                }
                if (mainfoVO.getSpeed() > 10000) {
                    errorList.add("第" + count + "行设备速度超出数限制！");
                }
                ProcessWorkinfo processWorkinfo = processWorkinfoService.getBaseMapper()
                        .selectOne(new QueryWrapper<ProcessWorkinfo>().eq("pr_name", mainfoVO.getPrName()));
                if (processWorkinfo == null) {
                    errorList.add("第" + count + "行没有该工序！");
                }

                Integer dpId = deptinfoService.getDpIdByDpName(mainfoVO.getDpName());//部门主键
                if (dpId == null) {
                    errorList.add("第" + count + "行没有该部门！");
                }
                Map<String, Object> map = new HashMap<>();
                map.put("name", mainfoVO.getName());
                List<MachineMainfo> list = machineMainfoService.getBaseMapper().selectByMap(map);
                //特别判断name不可重复 mno不可重复
                if (!list.isEmpty()) {
                    MachineMainfo mainfo = list.get(0);
                    if (mainfo.getName().equals(mainfoVO.getName())) { //判断设备名字是否已经存在
                        errorList.add("第" + count + "行设备名称已经存在！");
                    }
                    if (mainfo.getMno().equals(mainfoVO.getName())) { //判断设备名字是否已经存在
                        errorList.add("第" + count + "行设备编号已经存在！");
                    }
                }
                if (names.contains(mainfoVO.getName())) { //判断设备名字在导入表中是否存在
                    errorList.add("第" + count + "行设备名称在表中已经存在！");
                }
                if (mnos.contains(mainfoVO.getMno())) { //判断设备名字在导入表中是否存在
                    errorList.add("第" + count + "行设备编号在表中已经存在！");
                }
                /*查询设备类型*/
                Dict dict = saIDictService.getBaseMapper().selectOne(new QueryWrapper<Dict>()
                        .eq("dict_value", mainfoVO.getMaType())
                        .eq("code", "maType"));
                if (dict == null) {
                    errorList.add("第" + count + "行设备类型不存在！");
                }

            }
            if (errorList.size() != 0) {
                return R.data(HttpStatus.BAD_REQUEST.value(), errorList, "导入数据异常，请检查！");
            }
            Map<String, Object> map = new HashMap<>();
            for (MachineMainfoVO machineMainfo : mainfoVOS) {
                /*查询设备类型*/
                Dict dict = saIDictService.getBaseMapper().selectOne(new QueryWrapper<Dict>()
                        .eq("dict_value", machineMainfo.getMaType())
                        .eq("code", "maType"));
                ProcessWorkinfo processWorkinfo = processWorkinfoService.getBaseMapper()
                        .selectOne(new QueryWrapper<ProcessWorkinfo>().eq("pr_name", machineMainfo.getPrName()));
                machineMainfo.setProId(processWorkinfo.getId());
                machineMainfo.setMaType(dict.getDictKey().toString());
                Integer dpId = deptinfoService.getDpIdByDpName(machineMainfo.getDpName());//部门主键
                machineMainfo.setDpId(dpId);
                map.put("brand", machineMainfo.getBrand());
                map.put("model", machineMainfo.getModel());
                List<MachineClassify> classifies = classifyService.getBaseMapper().selectByMap(map);
                Integer mtId = null;
                if (!classifies.isEmpty()) {
                    mtId = classifies.get(0).getId();
                } else {
                    MachineClassify classify = new MachineClassify();
                    classify.setCreateAt(new Date());
                    classify.setSpecs(machineMainfo.getSpecs());
                    classify.setBrand(machineMainfo.getBrand());
                    classify.setModel(machineMainfo.getModel());
                    classify.setSpeed(machineMainfo.getSpeed());
                    //设置默认图片 不设置前端会报错
                    if (machineMainfo.getImage() == null || "".equals(machineMainfo.getImage())) {
                        classify.setImage("http://d.lanrentuku.com/down/png/1709/mahjong-icons/wind-east.png");
                    } else {
                        classify.setImage(machineMainfo.getImage());
                    }
                    classifyService.save(classify);
                    mtId = classify.getId();
                }
                machineMainfo.setMtId(mtId);
                machineMainfoService.save(machineMainfo); //保存所有导入信息
                map.clear();
            }
            return R.success("导入成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("导入数据异常,请使用模板");
        }
    }

    @GetMapping("/getMachine")
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "获取设备分类树接口")
    public R getProcessMach() {
//        查询所有分类
        List<DictVO> dictvo = machineClassifyService.listByMachine();
        if (!Func.isEmpty(dictvo)) {
            dictvo.forEach(dict -> {
//                查询分类下的设备
//                Map<String, Object> maMap = new HashMap<>();
//                maMap.put("ma_type", dict.getDictKey());//getMachineMainfoById
                List<MachineMainfoVO> machins = machineMainfoService.getMachineMainfoByType(dict.getDictKey());
                dict.setMainfos(machins);
            });
        }
        return R.data(dictvo);
    }

    @GetMapping("/getMachineTree")
    @ApiOperation(value = "设备管理界面,设备分类树接口")
    public R getMachineTree() {
//        查询所有分类
        List<DictVO> dictvo = machineClassifyService.listByMachine();
        if (!Func.isEmpty(dictvo)) {
            dictvo.forEach(dict -> {
                List<MachineMainfoVO> machins = machineMainfoService.getMachineTree(dict.getDictKey());
                dict.setMainfos(machins);
            });
        }
        return R.data(dictvo);
    }

    @GetMapping("/getDeviceByType")
    @ApiOperation(value = "根据设备类型获取设备")
    public R<List<MachineMainfo>> getDeviceByType(String type) {
        List<MachineMainfo> machineMainfos = machineMainfoMapper.findByMaType(type);
        return R.data(machineMainfos);
    }


    @GetMapping("/monitor")
    @ApiOperation(value = "设备实时监控")
    public R<List<MachineMonitorVO>> monitor(MonitorRequest request) {
        List<MachineMonitorVO> machineMainfos = machineMainfoService.monitor(request);
        return R.data(machineMainfos);
    }


    @GetMapping("/monitorCapacityAnaly")
    @ApiOperation(value = "设备实时监控产能分析图")
    public R<List<MonitorCapacityAnalyVO>> monitorCapacityAnaly(Integer wsId) {
        List<MonitorCapacityAnalyVO> vos = machineMainfoService.monitorCapacityAnaly(wsId);
        return R.data(vos);
    }

    @GetMapping("/todayCapacity")
    @ApiOperation(value = "今日产能报表")
    public List<TodayCapacityVO> todayCapacity() {

        List<TodayCapacityVO> vos = machineMainfoService.todayCapacity();

        return vos;
    }

}
