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
package com.yb.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yb.base.IdCardUtil.ValidateIdCardUtil;
import com.yb.base.entity.BaseStaffext;
import com.yb.base.entity.BaseStaffinfo;
import com.yb.base.service.IBaseDeptinfoService;
import com.yb.base.service.IBaseStaffextService;
import com.yb.base.service.IBaseStaffinfoService;
import com.yb.base.vo.BaseClassinfoVO;
import com.yb.base.vo.BaseStaffextVO;
import com.yb.base.vo.BaseStaffinfoVO;
import com.yb.base.wrapper.BaseStaffinfoWrapper;
import com.yb.common.DateUtil;
import com.yb.dynamicData.datasource.DBIdentifier;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteInfo;
import com.yb.execute.mapper.ExecuteBrieferMapper;
import com.yb.execute.mapper.ExecuteInfoMapper;
import com.yb.system.user.entity.SaUser;
import com.yb.system.user.service.SaIUserService;
import com.yb.workbatch.excelUtils.ExcelUtil;
import com.yb.workbatch.vo.ExcelErrorVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 人员表_yb_base_staffinfo 控制器
 *
 * @author Blade
 * @since 2020-03-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/basestaffinfo")
@Api(value = "人员表_yb_base_staffinfo", tags = "人员表_yb_base_staffinfo接口")
public class BaseStaffinfoController extends BladeController {

    private IBaseStaffinfoService baseStaffinfoService;
    @Autowired
    private IBaseStaffextService baseStaffextService;
    @Autowired
    private IBaseDeptinfoService baseDeptinfoService;
    @Autowired
    private ExecuteBrieferMapper executeBrieferMapper;
    @Autowired
    private ExecuteInfoMapper executeInfoMapper;
    @Autowired
    private SaIUserService saIUserService;

    /**
     * 详情
     */
    @GetMapping("/detail")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "传入sf_id")
    public R<BaseStaffinfoVO> detail(Integer id) {
        BaseStaffinfo detail = baseStaffinfoService.getOneStaffInfo(id);
        return R.data(BaseStaffinfoWrapper.build().entityVO(detail));
    }

    /**
     * 分页 人员表_yb_base_staffinfo
     */
    @GetMapping("/list")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "分页", notes = "传入baseStaffinfo")
    public R<IPage<BaseStaffinfoVO>> list(BaseStaffinfo baseStaffinfo, Query query) {
        IPage<BaseStaffinfo> pages = baseStaffinfoService.page(Condition.getPage(query), Condition.getQueryWrapper(baseStaffinfo));
        return R.data(BaseStaffinfoWrapper.build().pageVO(pages));
    }

    /**
     * 自定义分页 人员表_yb_base_staffinfo
     */
    @GetMapping("/page")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "分页", notes = "传入baseStaffinfo")
    public R<IPage<BaseStaffinfoVO>> page(BaseStaffinfoVO baseStaffinfo, Query query) {
        IPage<BaseStaffinfoVO> pages = baseStaffinfoService.selectBaseStaffinfoPage(Condition.getPage(query), baseStaffinfo);
        return R.data(pages);
    }

    /**
     * 新增 人员表_yb_base_staffinfo
     */
    @PostMapping("/save")
    @ApiOperationSupport(order = 4)
    @ApiOperation(value = "新增", notes = "baseStaffinfoVo")
    public R save(@Valid @RequestBody BaseStaffinfoVO baseStaffinfoVo) {
//        查询工号是否重复
        if (baseStaffinfoService.getJobnumIsExit(baseStaffinfoVo.getJobnum()) != null) {
            return R.fail("新增失败,工号重复");
        }
//            查询身份证号是否重复
        if (baseStaffextService.getIdcardIsExit(baseStaffinfoVo.getIdcard()) != null) {
            return R.fail("新增失败,身份证号重复");
        }
        //存储用户信息
        SaUser user = new SaUser();
        user.setAccount(baseStaffinfoVo.getJobnum());
        user.setName(baseStaffinfoVo.getName());
        user.setRealName(baseStaffinfoVo.getName());
        user.setPassword(DigestUtil.encrypt("123"));
        user.setBirthday(baseStaffinfoVo.getBirthday());
        user.setSex(baseStaffinfoVo.getSex());
        user.setDeptId(baseStaffinfoVo.getDpId().toString());
        user.setCreateTime(new Date());
        user.setTenantId(DBIdentifier.getProjectCode());
        saIUserService.save(user);
//            baseStaffinfoVo.setDpId(1);//1号部门
//            baseStaffinfoVo.setMold(2);//生产
//            baseStaffinfoVo.setJobs(4);//排产员
        baseStaffinfoVo.setHireTime(new Date());//入职时间
        baseStaffinfoVo.setProcesses("模切");
        baseStaffinfoVo.setIsUsed(1);//默认启用
        baseStaffinfoVo.setCreateAt(new Date());//创建时间
        BaseStaffinfo baseStaffinfo = baseStaffinfoVo;
        baseStaffinfo.setUserId(user.getId());
        baseStaffinfoService.save(baseStaffinfo);
//            用工号查询出对于人员ID增加人员扩展信息
        BaseStaffext baseStaffext = new BaseStaffext();
        BaseStaffinfoVO vo = baseStaffinfoService.getBaseStaffinfoVOByjobnum(baseStaffinfoVo.getJobnum());
        baseStaffext.setSfId(vo.getId());
        baseStaffext.setSex(baseStaffinfoVo.getSex());
        baseStaffext.setEducation(baseStaffinfoVo.getEducation());
        baseStaffext.setHometown(baseStaffinfoVo.getHometown());
        baseStaffext.setBirthday(baseStaffinfoVo.getBirthday());
        baseStaffext.setIdcard(baseStaffinfoVo.getIdcard());
        baseStaffext.setIdaddr(baseStaffinfoVo.getIdaddr());
        baseStaffext.setCurraddr(baseStaffinfoVo.getCurraddr());
        baseStaffext.setCreateAt(new Date());
        return R.status(baseStaffextService.saveOrUpdate(baseStaffext));
    }

    /**
     * 修改 人员表_yb_base_staffinfo
     */
    @PostMapping("/updateStaffinfo")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改", notes = "传入baseStaffinfo")
    public R updateStaffinfo(@Valid @RequestBody BaseStaffinfoVO baseStaffinfoVO) {
        try {
            baseStaffinfoService.updateById(baseStaffinfoVO);
//            修改人员扩展信息
            BaseStaffext baseStaffext = new BaseStaffext();
            baseStaffext.setId(baseStaffinfoVO.getExtId());
            baseStaffext.setSfId(baseStaffinfoVO.getId());
            baseStaffext.setSex(baseStaffinfoVO.getSex());
            baseStaffext.setEducation(baseStaffinfoVO.getEducation());
            baseStaffext.setHometown(baseStaffinfoVO.getHometown());
            baseStaffext.setBirthday(baseStaffinfoVO.getBirthday());
            baseStaffext.setIdcard(baseStaffinfoVO.getIdcard());
            baseStaffext.setIdaddr(baseStaffinfoVO.getIdaddr());
            baseStaffext.setCurraddr(baseStaffinfoVO.getCurraddr());
            return R.status(baseStaffextService.updateById(baseStaffext));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("新增失败");
        }
    }

    /**
     * 新增或修改 人员表_yb_base_staffinfo
     */
    @PostMapping("/submit")
    @ApiOperationSupport(order = 6)
    @ApiOperation(value = "新增或修改", notes = "传入baseStaffinfo")
    @Transactional
    public R submit(@Valid @RequestBody BaseStaffinfoVO baseStaffinfoVO) {
        R r = new R();
        try {
            if (!Func.isEmpty(baseStaffinfoVO.getId())) {
//            修改
                BaseStaffinfo baseStaffinfo = baseStaffinfoVO;
                r = R.status(baseStaffinfoService.saveOrUpdate(baseStaffinfo));
                baseStaffinfoVO.getId();
            } else {
                BaseStaffext baseStaffext = new BaseStaffext();
                baseStaffext.setSex(baseStaffinfoVO.getSex());
                baseStaffext.setEducation(baseStaffinfoVO.getEducation());
                baseStaffext.setHometown(baseStaffinfoVO.getHometown());
                baseStaffext.setBirthday(baseStaffinfoVO.getBirthday());
                baseStaffext.setIdcard(baseStaffinfoVO.getIdcard());
                baseStaffext.setCurraddr(baseStaffinfoVO.getCurraddr());
                baseStaffext.setCreateAt(new Date());
                r = R.status(baseStaffextService.saveOrUpdate(baseStaffext));
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            r = R.status(false);
        }
        return r;
    }


    /**
     * 删除 人员表_yb_base_staffinfo
     */
    @PostMapping("/remove")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
        return R.status(baseStaffinfoService.removeByIds(Func.toIntList(ids)));
    }

    /**
     * 离职 人员表_yb_base_staffinfo
     */
    @PostMapping("/leave")
    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "离职", notes = "传入ids")
    public R leave(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
//        将人员状态修改为停用
//        return R.status(baseStaffinfoService.removeByIds(Func.toIntList(ids)));
        String[] strings = ids.split(",");
        Integer[] id = new Integer[strings.length];
        for (int a = 0; a < strings.length; a++) {
            id[a] = Integer.parseInt(strings[a]);
        }

        return R.status(baseStaffinfoService.updateStaffInfoIsUsedById(id));
    }

    /**
     * 查找 人员表_yb_base_staffinfo
     */

    @ApiOperationSupport(order = 7)
    @ApiOperation(value = "删除", notes = "传入ids")
    @PostMapping("/getStaffinfoById")
    public R getStaffinfoById(Integer id) {
        return R.data(baseStaffinfoService.getById(id));
    }

    /**
     * 自定义分页 人员表 人员管理
     */
    @GetMapping("/lists")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "列表", notes = "传入current和size")
    public R<IPage<BaseStaffinfoVO>> lists(Query query, @ApiIgnore @RequestParam() Map<String, Object> baseStaffinfoVO) {
        BaseStaffinfoVO baseStaffinfoVO1 = new BaseStaffinfoVO();
        if (!baseStaffinfoVO.isEmpty()) {
            for (Map.Entry<String, Object> entry : baseStaffinfoVO.entrySet()) {
                if (entry.getKey().equals("jobnum")) {
                    baseStaffinfoVO1.setJobnum("" + entry.getValue());
                }
                if (entry.getKey().equals("name")) {
                    baseStaffinfoVO1.setName("" + entry.getValue());
                }
                if (entry.getKey().equals("idcard")) {
                    baseStaffinfoVO1.setIdcard("" + entry.getValue());
                }
            }
        }
        IPage<BaseStaffinfoVO> pages = baseStaffinfoService.selectBaseStaffPage(Condition.getPage(query), baseStaffinfoVO1);
        return R.data(pages);
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
//            读取excel文件
            Map<String, String> maps = new HashMap<>();
//            base_staffinfo
            maps.put("姓名", "name");
            maps.put("工号", "jobnum");
            maps.put("联系电话", "phone");
            maps.put("部门名称", "dpName"); //转到dpId
            maps.put("类型", "modelName");//转到model
            maps.put("岗位", "jobsName");//转到jobs
            maps.put("劳动类型", "laborerName");//转到laborer
            maps.put("工种", "processes");
            maps.put("入职时间", "hireTimeString");

//            base_staffext
            maps.put("学历", "educationName");//转到education
            maps.put("身份证号", "idcard");
            maps.put("身份证地址", "idaddr");
            maps.put("籍贯", "hometown");
            maps.put("现居住地址", "curraddr");
            Set<String> errorList = new HashSet<>();
            ExcelErrorVo excelErrorVo = ExcelUtil.readExcel(xssfWorkbook, 0, maps, errorList);
            errorList = excelErrorVo.getErrorMessageList();
            List<Map<String, String>> mapList = excelErrorVo.getResultList();
            mapList.remove(0);
            JSONArray jsonData = JSONArray.fromObject(mapList);
            List<BaseStaffinfoVO> baseStaffinfoVOS = (List<BaseStaffinfoVO>) JSONArray.toList(JSONArray.fromObject(mapList), BaseStaffinfoVO.class);
            List<BaseStaffextVO> baseStaffextVOS = (List<BaseStaffextVO>) JSONArray.toList(JSONArray.fromObject(mapList), BaseStaffextVO.class);
            int count = 1;
            int num = 0;
            List<String> jobnumList = new ArrayList<>();
            List<String> idcardList = new ArrayList<>();
            Map<String, Integer> jobslMap = new HashMap<>();
            jobslMap.put("机长", 1);
            jobslMap.put("班长", 2);
            jobslMap.put("车间主管", 3);
            jobslMap.put("排产员", 4);
            Map<String, Integer> laborerlMap = new HashMap<>();
            laborerlMap.put("正式员工", 1);
            laborerlMap.put("临时员工", 2);
            laborerlMap.put("试用期", 3);
            laborerlMap.put("实习员工", 4);
            Map<String, Integer> modelMap = new HashMap<>();
            modelMap.put("管理", 1);
            modelMap.put("生产", 2);
            Map<String, Integer> educationMap = new HashMap<>();
            educationMap.put("初中及以下", 1);
            educationMap.put("高中", 2);
            educationMap.put("大专", 3);
            educationMap.put("本科", 4);
            educationMap.put("硕士", 5);
            educationMap.put("博士及以上", 6);
            for (BaseStaffinfoVO baseStaffinfoVO : baseStaffinfoVOS) {
                count = count + 1;
//                对工号进行判断是否重复
                if (baseStaffinfoVO.getName().equals("")) {
                    errorList.add("第" + count + "行姓名为空");
                }

                if (baseStaffinfoVO.getJobnum().equals("")) {
                    errorList.add("第" + count + "行工号为空");
                } else {
                    if (baseStaffinfoService.getJobnumIsExit(baseStaffinfoVO.getJobnum()) != null) {
                        errorList.add("第" + count + "行工号重复");
                    } else {
                        if (jobnumList.contains(baseStaffinfoVO.getJobnum())) {
                            errorList.add("第" + count + "行工号在表中有重复");
                        } else {
                            jobnumList.add(baseStaffinfoVO.getJobnum());//存入工号表中
                        }
                    }
                }
                if (baseStaffinfoVO.getPhone().equals("")) {
                    errorList.add("第" + count + "行联系电话为空");
                }
                if (baseStaffinfoVO.getDpName().equals("")) {
                    errorList.add("第" + count + "行部门名称为空");
                } else {
                    //                查询部门是否存在
                    Integer dpId = baseDeptinfoService.getDpIdByDpName(baseStaffinfoVO.getDpName());
                    if (dpId == null) {
                        errorList.add("第" + count + "行部门名称不存在或已被删除");
                    }
                }
                if (baseStaffinfoVO.getModelName().equals("")) {
                    errorList.add("第" + count + "行类型为空");
                } else {
                    //                检查类型
                    Integer modelResult = modelMap.get(baseStaffinfoVO.getModelName());
                    if (modelResult == null) {
                        errorList.add("第" + count + "行的类型有问题");
                    }
                }
                if (baseStaffinfoVO.getJobsName().equals("")) {
                    errorList.add("第" + count + "行岗位为空");
                } else {
                    //                检查岗位
                    Integer jobsResult = jobslMap.get(baseStaffinfoVO.getJobsName());
                    if (jobsResult == null) {
                        errorList.add("第" + count + "行的岗位有问题");
                    }
                }
                if (baseStaffinfoVO.getLaborerName().equals("")) {
                    errorList.add("第" + count + "行劳动类型为空");
                } else {
                    //                检查劳动类型
                    Integer laborerResult = laborerlMap.get(baseStaffinfoVO.getLaborerName());
                    if (laborerResult == null) {
                        errorList.add("第" + count + "行的劳动类型有问题");
                    }
                }
                if (baseStaffinfoVO.getProcesses().equals("")) {
                    errorList.add("第" + count + "行工种为空");
                }
                if (baseStaffinfoVO.getHireTimeString().equals("")) {
                    errorList.add("第" + count + "行入职时间为空");
                } else {
                    //                检查入职时间
                    if (!DateUtil.isValidDate(baseStaffinfoVO.getHireTimeString())) {
                        errorList.add("第" + count + "列的截止时间有问题");
                    }
                }

                if (baseStaffextVOS.get(num).getEducationName().equals("")) {
                    errorList.add("第" + count + "行学历为空");
                } else {
                    //                检查学历
                    Integer educationResult = educationMap.get(baseStaffextVOS.get(num).getEducationName());
                    if (educationResult == null) {
                        errorList.add("第" + count + "行的学历有问题");
                    }
                }
//                    检查并获取性别生日
                if (!ValidateIdCardUtil.isIDCard(baseStaffextVOS.get(num).getIdcard())) {
                    errorList.add("第" + count + "行身份证号格式有问题");
                }
//                查询身份证号是否已存在
                if (baseStaffextService.getIdcardIsExit(baseStaffextVOS.get(num).getIdcard()) != null) {
                    errorList.add("第" + count + "行该身份证号已存在");
                }
                if (baseStaffextVOS.get(num).getIdcard().equals("")) {
                    errorList.add("第" + count + "行身份证号为空");
                } else {
                    if (idcardList.contains(baseStaffextVOS.get(num).getIdcard())) {
                        errorList.add("第" + count + "行身份证号在表中有重复");
                    } else {
                        idcardList.add(baseStaffextVOS.get(num).getIdcard());//存入身份证号集合中
                    }
                }
                if (baseStaffextVOS.get(num).getIdaddr().equals("")) {
                    errorList.add("第" + count + "行身份证地址为空");
                }
                if (baseStaffextVOS.get(num).getHometown().equals("")) {
                    errorList.add("第" + count + "行籍贯为空");
                }
                if (baseStaffextVOS.get(num).getCurraddr().equals("")) {
                    errorList.add("第" + count + "行现居住地址为空");
                }
                num++;
            }
            if (errorList.size() == 0) {
                for (int i = 0; i < jsonData.size(); i++) {
                    JSONObject jason = jsonData.getJSONObject(i);
                    BaseStaffinfoVO baseStaffinfoVO = (BaseStaffinfoVO) JSONObject.toBean(jason, BaseStaffinfoVO.class);
                    BaseStaffextVO baseStaffextVO = (BaseStaffextVO) JSONObject.toBean(jason, BaseStaffextVO.class);
//                    插入部门ID
                    baseStaffinfoVO.setDpId(baseDeptinfoService.getDpIdByDpName(baseStaffinfoVO.getDpName()));
//                    插入类型
                    baseStaffinfoVO.setMold(modelMap.get(baseStaffinfoVO.getModelName()));
//                    插入岗位
                    baseStaffinfoVO.setJobs(jobslMap.get(baseStaffinfoVO.getJobsName()));
//                    插入劳动类型
                    baseStaffinfoVO.setLaborer(laborerlMap.get(baseStaffinfoVO.getLaborerName()));
//                    插入入职时间
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                    baseStaffinfoVO.setHireTime(ft.parse(baseStaffinfoVO.getHireTimeString()));
                    baseStaffinfoVO.setIsUsed(1);//默认启用
                    baseStaffinfoVO.setCreateAt(new Date());//创建时间
//                    存入数据库
                    baseStaffinfoService.save(baseStaffinfoVO);
//                    获取对应id插入
                    baseStaffextVO.setSfId(baseStaffinfoVO.getId());
//                    插入学历
                    baseStaffextVO.setEducation(educationMap.get(baseStaffextVO.getEducationName()));
//                        获取生日
                    baseStaffextVO.setBirthday(ValidateIdCardUtil.getBirthday(baseStaffextVO.getIdcard()));
//                        获取性别
                    baseStaffextVO.setSex(ValidateIdCardUtil.getSex(baseStaffextVO.getIdcard()));
                    baseStaffextVO.setCreateAt(new Date());
                    baseStaffextService.save(baseStaffextVO);

                }
                return R.success("导入成功");
            } else {
//                插入数据
                return R.data(400, errorList, "导入失败");
            }
        } catch (Exception e) {
            return R.fail("请使用模板");
        }
    }

    /**
     * 车间下的人员
     */
    @PostMapping("/getUserListByPdId")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "车间下的人员", notes = "传入dpId")
    public R<List<BaseStaffinfoVO>> getUserListByPdId(@Valid @RequestBody BaseStaffinfoVO baseStaffinfoVO) {
        return R.data(baseStaffinfoService.getUserListByPdId(baseStaffinfoVO));
    }

    /**
     * 同一班组人员
     */
    @GetMapping("/getTeamByUsId")
    @ApiOperationSupport(order = 3)
    @ApiOperation(value = "根据上报id获取同班组的人员", notes = "传入usId")
    public R<List<BaseStaffinfo>> getTeamByUsId(@ApiParam("上报id")@RequestParam Integer bfId) {
        ExecuteBriefer executeBriefer = executeBrieferMapper.selectById(bfId);
        if(executeBriefer != null){
            Integer exId = executeBriefer.getExId();
            if(exId == null) return R.fail("上报表exId不存在");
            ExecuteInfo executeInfo = executeInfoMapper.selectById(exId);
            if(executeInfo == null) return R.fail("上报表中的执行id我查到执行单");
            String usId = executeInfo.getUsId();
            if(StringUtil.isEmpty(usId)) return R.fail("执行单中的用户id为null");
            BaseStaffinfo baseStaffinfo = baseStaffinfoService.getOne(new QueryWrapper<BaseStaffinfo>().eq("user_id",usId));
            if (baseStaffinfo == null) {
                return R.fail("用户id不存在");
            }
            List<BaseStaffinfo> baseStaffinfoList = new ArrayList<>();
            Integer bcId = baseStaffinfo.getBcId();
            if (bcId != null) {
                baseStaffinfoList = baseStaffinfoService.list(new QueryWrapper<BaseStaffinfo>().eq("bc_id", bcId));
            }else {
                baseStaffinfoList.add(baseStaffinfo);
            }
            return R.data(baseStaffinfoList);
        }
        return R.fail("传入的上报表id没查到数据");
    }
}
