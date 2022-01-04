package com.anaysis.service.impl;

import com.anaysis.entity.*;
import com.anaysis.mysqlmapper.BaseClassinfoMapper;
import com.anaysis.mysqlmapper.BladeUserMapper;
import com.anaysis.mysqlmapper.MachineMainfoMapper;
import com.anaysis.service.IBaseDeptinfoService;
import com.anaysis.service.IBaseStaffextService;
import com.anaysis.service.IBaseStaffinfoService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import com.anaysis.service.IBladeUserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * @author lzb
 * @date 2020-11-25
 */

@Slf4j
@Service
public class BladeUserServiceImpl extends ServiceImpl<BladeUserMapper, BladeUser> implements IBladeUserService {
    @Autowired
    private BladeUserMapper bladeUserMapper;
    @Autowired
    private HrPersonService personService;
    @Autowired
    private IBaseStaffinfoService staffinfoService;
    @Autowired
    private IBaseStaffextService staffextService;
    @Autowired
    private IBaseDeptinfoService deptinfoService;
    @Autowired
    private BaseClassinfoMapper baseClassinfoMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;

    @Override
    public void sync() {
        List<HrPerson> list;
        List<String> notImport = this.notImport();
        if (notImport.size() > 0) {
            list = personService.getByIds(notImport);
        } else {
            return;
        }
        List<BladeUser> bladeUsers = bladeUserMapper.selectList(null);
        List<BladeUser> collect = bladeUsers.stream().filter(d -> null != d.getAccount() && "".equals(d.getAccount())).collect(Collectors.toList());
        Map<String, String> accountMap = collect.stream().collect(Collectors.toMap(BladeUser::getAccount, BladeUser::getAccount));
        Map<String, Integer> deptLink = deptinfoService.deptLink();
        for (HrPerson hrPerson : list) {
            String dpErp = hrPerson.getDpErp();
            Integer deptId = deptLink.get(dpErp);
            if (null != deptId) {
                // todo erp有些人没部门
                hrPerson.setDeptId(deptId.toString());
            }
            // 密码默认为：123
            hrPerson.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
            hrPerson.setAccount(checkAccount(hrPerson.getRealName(), accountMap));
            hrPerson.setTenantId("nxhr");
            hrPerson.setName(hrPerson.getRealName());
            hrPerson.setStatus(1);
            hrPerson.setRoleId("8");
            bladeUserMapper.insert(hrPerson);
            BaseStaffinfo baseStaffinfo = new BaseStaffinfo();
            baseStaffinfo.setUserId(hrPerson.getId());
            baseStaffinfo.setName(hrPerson.getName());
            baseStaffinfo.setPhone(hrPerson.getPhone());
            baseStaffinfo.setErpId(hrPerson.getErpId());
            baseStaffinfo.setJobs(hrPerson.getJobs());
            baseStaffinfo.setJobnum(hrPerson.getAccount());
            baseStaffinfo.setIsUsed(1);
            baseStaffinfo.setLaborer(1);
//            baseStaffinfo.setBcId(); // 班组id
            if (null != hrPerson.getDeptId()) {
                baseStaffinfo.setDpId(Integer.valueOf(hrPerson.getDeptId()));
            }
            staffinfoService.save(baseStaffinfo);
            BaseStaffext baseStaffext = new BaseStaffext();
            baseStaffext.setIdcard(hrPerson.getIdcard());
            baseStaffext.setSex(hrPerson.getSex());
            baseStaffext.setEducation(hrPerson.getEducation());
            baseStaffext.setHometown(hrPerson.getHometown());
            baseStaffext.setSfId(baseStaffinfo.getId());
            baseStaffext.setCurraddr(hrPerson.getCurraddr());
            baseStaffext.setErpId(hrPerson.getErpId());
            LocalDateTime birthday = hrPerson.getBirthday();
            if (null != birthday) {
                baseStaffext.setBirthday(birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            baseStaffext.setIdaddr(hrPerson.getHometown());
            staffextService.save(baseStaffext);
        }
    }

    @Override
    public void syncDeptStaff() {
        List<MachineMainfo> machineMainfos = machineMainfoMapper.selectList(Wrappers.<MachineMainfo>lambdaQuery().select(MachineMainfo::getErpId, MachineMainfo::getDpId));
        List<BladeUser> bladeUsers = bladeUserMapper.selectList(null);
        List<BladeUser> collect = bladeUsers.stream().filter(d -> null != d.getAccount() && !"".equals(d.getAccount())).collect(Collectors.toList());
        Map<String, String> accountMap = collect.stream().collect(Collectors.toMap(BladeUser::getAccount, BladeUser::getAccount));
        Map<String, Integer> machineMap = machineMainfos.stream().collect(Collectors.toMap(MachineMainfo::getErpId, MachineMainfo::getDpId, (k1, k2) -> k2));
        List<BaseClassinfo> baseClassinfos = baseClassinfoMapper.selectList(Wrappers.<BaseClassinfo>lambdaQuery().select(BaseClassinfo::getBcName, BaseClassinfo::getDpId, BaseClassinfo::getId));
        Map<String, Integer> bcNameIdMap = baseClassinfos.stream().collect(Collectors.toMap(BaseClassinfo::getBcName, BaseClassinfo::getId, (k1, k2) -> k2));
        List<ShiftMachine> shiftMachines = personService.getClassInfoMachine();
        Map<String, String> shiftMachinesMap = shiftMachines.stream().collect(Collectors.toMap(ShiftMachine::getShift, ShiftMachine::getMaErpId, (k1, k2) -> k2));
        List<BladeUser> list = personService.getWorkStaffNotInHrperson();
        List<BladeUser> list1 = personService.getWorkStaffInHrperson();
        // 同步erp人事表不存在但是车间班组存在人员信息
        for (BladeUser bladeUser : list) {
            String maErpId = shiftMachinesMap.get(bladeUser.getShift());
            String deptId = machineMap.get(maErpId) + "";
            bladeUser.setAccount(checkAccount(bladeUser.getRealName(), accountMap));
            bladeUser.setPassword("adcd7048512e64b48da55b027577886ee5a36350");
            bladeUser.setName(bladeUser.getRealName());
            bladeUser.setRoleId("8");
            bladeUser.setTenantId("nxhr");
            bladeUser.setStatus(1);
            bladeUser.setDeptId(deptId);
            bladeUserMapper.insert(bladeUser);
            BaseStaffinfo baseStaffinfo = new BaseStaffinfo();
            baseStaffinfo.setUserId(bladeUser.getId());
            baseStaffinfo.setName(bladeUser.getName());
            baseStaffinfo.setIsUsed(1);
            baseStaffinfo.setLaborer(1);
            Integer bcId = bcNameIdMap.get(bladeUser.getShift());
            baseStaffinfo.setBcId(bcId);
            baseStaffinfo.setJobnum(bladeUser.getAccount());
            baseStaffinfo.setDpId(Integer.valueOf(bladeUser.getDeptId()));
            staffinfoService.save(baseStaffinfo);
            BaseStaffext baseStaffext = new BaseStaffext();
            baseStaffext.setSfId(baseStaffinfo.getId());
            staffextService.save(baseStaffext);
        }
        // 同步erp人事表存在、车间班组存在的人员信息
        for (BladeUser bladeUser : list1) {
            List<BaseStaffinfo> list2 = staffinfoService.list(Wrappers.<BaseStaffinfo>lambdaQuery().eq(BaseStaffinfo::getName, bladeUser.getRealName()));
            if (list2.size() > 1) {
                StringBuilder s = new StringBuilder();
                for (BaseStaffinfo baseStaffinfo : list2) {
                    s.append("[").append(baseStaffinfo.getName()).append("]");
                }
                log.error("===================同步时存在同名人员：{}===================", s);
            } else if (1 == list2.size()) {
                String maErpId = shiftMachinesMap.get(bladeUser.getShift());
                Integer deptId = machineMap.get(maErpId);
                BaseStaffinfo baseStaffinfo = list2.get(0);
                baseStaffinfo.setDpId(deptId);
                baseStaffinfo.setBcId(bcNameIdMap.get(bladeUser.getShift()));
                staffinfoService.updateById(baseStaffinfo);
            }

        }
    }

    /**
     * 未导入erp表HRPersonProf的ObjID
     *
     * @return
     */
    public List<String> notImport() {
        List<String> myErpIds = bladeUserMapper.getErpIds();
        List<String> hrErpIds = personService.getAllErpId();
        List<String> notImport = hrErpIds.stream().filter(o -> !myErpIds.contains(o)).collect(Collectors.toList());
        return notImport;
    }


    /**
     * 验证账户是否存在
     *
     * @param name
     * @return 返回姓名转拼音格式账号：如张三 -> zhangshan,如有重复后面补数字1,2,3
     */
    private String checkAccount(String name, Map<String, String> accountMap) {
        String nameFirstSpell = cn2FirstSpell(name);
        String account = "nx" + nameFirstSpell;
        String nativeAccount = "nx" + nameFirstSpell;
        int i = 1;
        while (accountMap.containsKey(account)) {
            String numbers = getNumbers(accountMap.get(account));
            if (null == numbers || "".equals(numbers)) {
                account += "1";
            } else {
                i++;
                account = nativeAccount + i;
            }
        }
        accountMap.put(account, account);
        return account;
    }

    private static final Pattern pattern = Pattern.compile("\\d+");

    /**
     * 截取数字: 非连续的返回第一个数字
     *
     * @param content
     * @return
     */
    private String getNumbers(String content) {
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 汉语转拼音：首字母
     *
     * @param chinese
     * @return 返回首字母
     */
    public static String cn2FirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (_t != null) {
                        pybf.append(_t[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim().toLowerCase();
    }

}