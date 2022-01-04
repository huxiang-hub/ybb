package com.yb.execute.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.yb.barcodeUtils.PDFUtils;
import com.yb.common.DateUtil;
import com.yb.execute.entity.ExecuteBriefer;
import com.yb.execute.entity.ExecuteExamine;
import com.yb.execute.entity.ExecuteTraycard;
import com.yb.execute.mapper.ExecuteTraycardMapper;
import com.yb.execute.service.ExecuteTraycardService;
import com.yb.execute.service.IExecuteBrieferService;
import com.yb.execute.service.IExecuteExamineService;
import com.yb.execute.utils.printDataUtil;
import com.yb.execute.vo.*;
import com.yb.execute.wrapper.ExecuteTraycardWrapper;
import com.yb.machine.entity.MachineMainfo;
import com.yb.machine.mapper.MachineMainfoMapper;
import com.yb.panelapi.user.entity.BaseFactory;
import com.yb.panelapi.user.mapper.BaseFactoryMapper;
import com.yb.stroe.entity.StoreInlog;
import com.yb.stroe.entity.StoreInventory;
import com.yb.stroe.entity.StoreOutlog;
import com.yb.stroe.entity.StoreSeat;
import com.yb.stroe.service.IStoreInlogService;
import com.yb.stroe.service.IStoreInventoryService;
import com.yb.stroe.service.IStoreOutlogService;
import com.yb.stroe.service.StoreSeatService;
import com.yb.workbatch.entity.WorkbatchOrdlink;
import com.yb.workbatch.entity.WorkbatchShift;
import com.yb.workbatch.mapper.WorkbatchOrdlinkMapper;
import com.yb.workbatch.mapper.WorkbatchShiftMapper;
import com.yb.workbatch.service.IWorkbatchOrdlinkNewService;
import com.yb.workbatch.vo.ConstructionVO;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExecuteTraycardServiceImpl extends ServiceImpl<ExecuteTraycardMapper, ExecuteTraycard> implements ExecuteTraycardService {

    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ExecuteTraycardMapper executeTraycardMapper;
    @Autowired
    private IWorkbatchOrdlinkNewService workbatchOrdlinkNewService;
    @Autowired
    private WorkbatchOrdlinkMapper workbatchOrdlinkMapper;
    @Autowired
    private WorkbatchShiftMapper workbatchShiftMapper;
    @Autowired
    private BaseFactoryMapper baseFactoryMapper;
    @Autowired
    private MachineMainfoMapper machineMainfoMapper;
    @Autowired
    private IStoreInventoryService storeInventoryService;
    @Autowired
    private StoreSeatService storeSeatService;
    @Autowired
    private IStoreInlogService storeInlogService;
    @Autowired
    private IStoreOutlogService storeOutlogService;
    @Autowired
    private IExecuteExamineService executeExamineService;
    @Autowired
    private IExecuteBrieferService executeBrieferService;


    private int divisor = 999999;//托盘编号的最大值

    @Override
    public void construction(List<Integer> etIdList) {
        String templateName = "model/ConstructionModel.pdf";
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(templateName);
        URL resource = this.getClass().getClassLoader().getResource(templateName);
        String resourcePath = resource.getPath();
//        String resourcePath = "C:\\Users\\Administrator\\Desktop\\ConstructionModel.pdf";
        List<ConstructionVO> constructionVOS = executeTraycardMapper.construction(etIdList);
        List<byte[]> bytesList = new ArrayList<>();
        for (ConstructionVO constructionVO : constructionVOS) {
            String batchNumber;//生产批号
            String functionary = null;//责任人
            String userName = constructionVO.getUserName();//操作人
            String maName = constructionVO.getMaName();//设备名称
            String dpName = constructionVO.getDpName();//车间名
            String sdDate = constructionVO.getSdDate();//排产日期
            batchNumber = sdDate + " " + dpName;
            if (!StringUtil.isEmpty(userName)) {
                functionary = userName + " " + maName;
            }
            String cmName = constructionVO.getCmName();//客户名称
            String downPorcess = constructionVO.getDownPorcess();//下工序
            String downPorcessDes = constructionVO.getDownPorcessDes();//下工序说明
            String finalTime = constructionVO.getFinalTime();//最终交期
            String inspector = constructionVO.getInspector();//质检员
            String materialName = constructionVO.getMaterialName();//材料名称
            Integer odCount = constructionVO.getOdCount();//订单数量
            String odNo = constructionVO.getOdNo();//订单编号
            Integer onTime = constructionVO.getOnTime();//贴次
            String pdName = constructionVO.getPdName();//产品名称
            String pdNo = constructionVO.getPdNo();//产品编号
            String pdSize = constructionVO.getPdSize();//产品规格
            String plateNo = constructionVO.getPlateNo();//版号
            Integer plateNumber = constructionVO.getPlateNumber();//此板数量
            String prDes = constructionVO.getPrDes();//工序说明
            String prName = constructionVO.getPrName();//工序名称
            String productBatch = constructionVO.getProductBatch();//客户产品批号
            String productNo = constructionVO.getProductNo();//客户产品编号
            Integer prPlanNum = constructionVO.getPrPlanNum();//工序应交货数
            String prRoute = constructionVO.getPrRoute();//工艺流程
            String ptName = constructionVO.getPtName();//部件名称
            String remark = constructionVO.getRemark();//备注
            Integer wasteNum = constructionVO.getWasteNum();//废品数量
            String wbNo = constructionVO.getWbNo();//施工单号
            Map<String, Object> datamap = new HashMap<>();
            datamap.put("batchNumber", batchNumber);
            datamap.put("wbNo", wbNo);
            if (wasteNum != null) {
                datamap.put("wasteNum", wasteNum.toString());
            } else {
                datamap.put("wasteNum", wasteNum);
            }
            datamap.put("remark", remark);
            datamap.put("ptName", ptName);
            datamap.put("prRoute", prRoute);
            if (prPlanNum != null) {
                datamap.put("prPlanNum", prPlanNum.toString());
            } else {
                datamap.put("prPlanNum", prPlanNum);
            }
            datamap.put("productNo", productNo);
            datamap.put("productBatch", productBatch);
            datamap.put("prName", prName);
            if (plateNumber != null) {
                datamap.put("plateNumber", plateNumber.toString());
            } else {
                datamap.put("plateNumber", plateNumber);
            }
            datamap.put("plateNo", plateNo);
            datamap.put("pdSize", pdSize);
            datamap.put("pdNo", pdNo);
            datamap.put("prDes", prDes);
            datamap.put("pdName", pdName);
            if (onTime != null) {
                datamap.put("onTime", onTime.toString());
            } else {
                datamap.put("onTime", onTime);
            }
            datamap.put("odNo", odNo);
            if (odCount != null) {
                datamap.put("odCount", odCount.toString());
            } else {
                datamap.put("odCount", odCount);
            }
            datamap.put("materialName", materialName);
            datamap.put("inspector", inspector);
            datamap.put("functionary", functionary);
            datamap.put("finalTime", finalTime);
            datamap.put("downPorcessDes", downPorcessDes);
            datamap.put("downPorcess", downPorcess);
            datamap.put("cmName", cmName);
//            datamap.put("billId", "标识卡");//pdf名称
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("datamap", datamap);
            /* 按照模板生产pdf,返回byte数组,然后用二进制数组的方式使多个pdf合并成一个*/
            byte[] bytes = PDFUtils.creatPdfByte(inputMap, resourcePath, response);
            bytesList.add(bytes);
        }

        /*-----------批量导出pdf----------*/
        if (bytesList == null || bytesList.isEmpty()) {
            return;
        }
        OutputStream out = null;// 输出流
        Document document = null;
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode("标识卡.pdf", "UTF-8"));
            out = new BufferedOutputStream(response.getOutputStream());
            document = new Document(new PdfReader(bytesList.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, out);
            document.open();
            for (int i = 0; i < bytesList.size(); i++) {
                PdfReader reader = new PdfReader(bytesList.get(i));
                int n = reader.getNumberOfPages();// 获得总页码
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);// 从当前Pdf,获取第j页
                    copy.addPage(page);
                }
                System.out.println(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            if(!etIdList.isEmpty()){
//                /*修改打印次数*/
//                executeTraycardMapper.updatePrintNumList(etIdList);
//            }
            System.out.println("finish " + new Date());
        }
        /*---------------------*/


    }

    @Override
    public void deleteListById(Integer wfId, List<Integer> etIdList) {
        executeTraycardMapper.deleteListById(wfId, etIdList);
    }

    @Override
    public Boolean updateTraycardList(TraycardDeleteListVO traycardDeleteListVO) {
        List<Integer> etIdList = traycardDeleteListVO.getEtIdList();//修改的id集合
        if (etIdList == null || etIdList.isEmpty()) {
            return false;
        }
        Integer mpId = traycardDeleteListVO.getMpId();//库位id
        String remark = traycardDeleteListVO.getRemark();//备注
        String storePlace = traycardDeleteListVO.getStorePlace();//库位名称
        Integer trayNum = traycardDeleteListVO.getTrayNum();//数量
        Integer usId = traycardDeleteListVO.getUsId();//操作人id
        Integer n = executeTraycardMapper.updateTraycardList(mpId, remark, storePlace, trayNum, usId, etIdList);
        return n > 0 ? true : false;
    }

    @Override
    public List<Integer> selectNeEtIdList(List<Integer> etIdList, Integer wfId) {
        return executeTraycardMapper.selectNeEtIdList(etIdList, wfId);
    }

    @Override
    public List<UpPrcessTraycardVO> getUpPrcessTraycardList(List<Integer> wfIdList) {
        List<UpPrcessTraycardVO> upPrcessTraycardVOList = new ArrayList<>();
        UpPrcessTraycardVO upPrcessTraycardVO;
        for (Integer wfId : wfIdList) {
            upPrcessTraycardVO = new UpPrcessTraycardVO();
            upPrcessTraycardVO.setNowaDayWfId(wfId);
//            /*查询上工序sdId*/
//            UpProcessScheduleVO upProcessScheduleVO = workbatchOrdlinkNewService.upProcessSchedule(wfId);
//            if(upProcessScheduleVO == null){
//                continue;
//            }
//            Integer sdId = upProcessScheduleVO.getId();
            /*获取上工序的sdId*/
            Integer sdId = workbatchOrdlinkMapper.getUpProcessSdIdByWfId(wfId);
            if (sdId == null) {
                continue;
            }
            /*查询排产单信息*/
            List<WorkbatchShift> workbatchShiftList =
                    workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("sd_id", sdId));
            /*查询上工序所有的托盘信息*/
            List<ExecuteTraycard> executeTraycardList =
                    executeTraycardMapper.selectList(new QueryWrapper<ExecuteTraycard>().eq("sd_id", sdId).groupBy("wf_id").groupBy("id"));
            List<UpPrTraycardVO> upPrTraycardVOList = new ArrayList<>();
            UpPrTraycardVO upPrTraycardVO;
            List<ExecuteTraycardVO> executeTraycardVOList;
            ExecuteTraycardVO executeTraycardVO;
            Integer planNum = 0;
            for (WorkbatchShift workbatchShift : workbatchShiftList) {
                executeTraycardVOList = new ArrayList<>();
                upPrTraycardVO = new UpPrTraycardVO();
                Integer finishNum = workbatchShift.getFinishNum();//已完成数量
                for (ExecuteTraycard executeTraycard : executeTraycardList) {
                    executeTraycardVO = ExecuteTraycardWrapper.build().entityVO(executeTraycard);
                    Integer prcessWfId = executeTraycardVO.getWfId();//上工序排产id
                    Integer trayNum = executeTraycardVO.getTrayNum();//本台计数
                    planNum += trayNum;
                    if (finishNum >= planNum) {//如果完成总数大于计划数则已变红负责未放满
                        executeTraycardVO.setRedStatus(1);
                    } else {
                        executeTraycardVO.setRedStatus(0);
                    }
                    if (prcessWfId != null && prcessWfId.equals(workbatchShift.getId())) {
                        executeTraycardVOList.add(executeTraycardVO);
                    }
                }
                if (executeTraycardVOList.isEmpty()) {
                    continue;
                }
                planNum = 0;
                upPrTraycardVO.setUpWfId(workbatchShift.getId());
                upPrTraycardVO.setSdDate(workbatchShift.getSdDate());
                upPrTraycardVO.setExecuteTraycardVOList(executeTraycardVOList);
                upPrTraycardVOList.add(upPrTraycardVO);
            }
            if (upPrTraycardVOList.isEmpty()) {
                continue;
            }
            upPrcessTraycardVO.setUpPrTraycardVOList(upPrTraycardVOList);
            upPrcessTraycardVOList.add(upPrcessTraycardVO);
        }
        return upPrcessTraycardVOList;
    }

    @Override
    public void updateTraycardTotalNumList(List<Integer> etIdList, int totalNum) {
        executeTraycardMapper.updateTraycardTotalNumList(etIdList, totalNum);
    }

    @Override
    public List<TraycardTextVO> getTraycardData(List<Integer> toIntList, Integer maId) {
        /*查询打印标识卡所需数据*/
        List<TraycardDataVO> traycardDataVOList = executeTraycardMapper.getTraycardData(toIntList, maId);
        /*查询公司前缀*/
        BaseFactory baseFactory = baseFactoryMapper.selectOne(new QueryWrapper<>());
        return printDataUtil.getTraycardTextVOList(traycardDataVOList, baseFactory);
    }


    @Override
    public void updatePrintNumList(List<Integer> toIntList, Integer maId, Integer exId) {
        executeTraycardMapper.updatePrintNumList(toIntList, maId, exId);
    }

    @Override
    public Integer getMaxId() {
        return executeTraycardMapper.getMaxId();
    }

    @Override
    public List<Integer> getNoPrintEtIdList(Integer wfId) {
        return executeTraycardMapper.getNoPrintEtIdList(wfId);
    }

    @Override
    public List<ExecuteTraycardVO> getExecuteTraycardByWfId(Integer wfId) {
        List<ExecuteTraycardVO> executeTraycardByWfId = executeTraycardMapper.getExecuteTraycardByWfId(wfId);
        /*查询公司前缀*/
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        for (ExecuteTraycardVO executeTraycardVO : executeTraycardByWfId) {
            String refNowDay;
            String time = "";
            Date startTime = executeTraycardVO.getStartTime();//开始时间
            if (startTime != null) {
                refNowDay = DateUtil.refNowDay(startTime);
                time = format.format(startTime);
            } else {
                refNowDay = DateUtil.refNowDay();
            }
            executeTraycardVO.setTraycardNO(executeTraycardVO.getTdNo());
            executeTraycardVO.setProductTime(refNowDay + " " + time +
                    "~" + format.format(date));
        }
        return executeTraycardByWfId;
    }

    @Override
    public ExecuteTraycard selectByTdNo(String barCode) {
        return executeTraycardMapper.selectByTdNo(barCode);
    }

    @Override
    public List<ExecuteTraycard> getByWfId(Integer wfId) {
        return executeTraycardMapper.getByWfId(wfId);
    }

    @Override
    public String getTdNo() {
        Integer maxId = this.getMaxId();
        BaseFactory baseFactory = baseFactoryMapper.selectOne(new QueryWrapper<>());
        String tenantId = baseFactory.getTenantId();
        String tdNo;
        Integer no = maxId % divisor;
        if (no == 0) {
            no = divisor;
        }
        if (no < 10) {
            tdNo = "00000" + no;
        } else if (10 <= no && no < 100) {
            tdNo = "0000" + no;
        } else if (100 <= no && no < 1000) {
            tdNo = "000" + no;
        } else if (1000 <= no && no < 10000) {
            tdNo = "00" + no;
        } else if (10000 <= no && no < 100000) {
            tdNo = "0" + no;
        } else {
            tdNo = no.toString();
        }
        return tenantId.toUpperCase() + "-" + tdNo;
    }

    @Override
    public String getTdNo2() {
        String tdno = executeTraycardMapper.getMaxTdNo();
        int i = 0;
        if (null != tdno) {
            String s = tdno.substring(5);
            i = Integer.parseInt(s);
            if (i == 999999) {
                i = 0;
            }
        }
        String hasRezo = String.format("%06d", (i + 1));
        return "NXHR-" + hasRezo;
    }

    @Override
    public String audits(Integer etId, Integer number, Integer seatId, Integer bfId, Integer exUserid) {
//        String s = auditsNotTray(bfId, number, exUserid);
//        if (null != s) {
//            return s;
//        }
        ExecuteTraycard executeTraycard = executeTraycardMapper.selectById(etId);
        StoreSeat seat = storeSeatService.getById(executeTraycard.getMpId());
        StoreSeat storeSeat = storeSeatService.getById(executeTraycard.getMpId());
        executeTraycard.setStorePlace(storeSeat.getStNo());
        executeTraycard.setMpId(storeSeat.getId());
        String beforeStNo = seat.getStNo();
        executeTraycard.setExStatus(1);
        StoreInventory storeInventory = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getEtId, etId));
        String msg = "审核成功";
        if (null == number) {
            number = storeInventory.getEtPdnum();
        }
        Integer beforEtPdnum = storeInventory.getEtPdnum();
        if (seatId != null) {
            // 审核时库位错误
            StoreSeat seat1 = storeSeatService.getById(seatId);
            executeTraycard.setStorePlace(seat1.getStNo());
            executeTraycard.setMpId(seat1.getId());
            storeInventory.setStId(seatId);
            storeInventory.setStNo(seat1.getStNo());
            executeTraycard.setTrayNum(number);
        }
        // 审核时数量错误
        if (number > storeInventory.getEtPdnum()) {
            // 审核时数量大于台账数量
            StoreInlog storeInlog = new StoreInlog();
            storeInlog.setStType(1);
            storeInlog.setStId(storeInventory.getStId());
            storeInlog.setStNo(storeInventory.getStNo());
            storeInlog.setStSize(storeInventory.getStSize());
            storeInlog.setOperateType(3);
            storeInlog.setEtId(storeInventory.getEtId());
            storeInlog.setLayNum(storeInventory.getLayNum());
            storeInlog.setEtPdnum(number - storeInventory.getEtPdnum());
            storeInlogService.save(storeInlog);
            msg = "审核数量大于台账";
        } else if (number < storeInventory.getEtPdnum()) {
            // 审核时数量小于台账数量
            StoreOutlog storeOutlog = new StoreOutlog();
            storeOutlog.setStType(1);
            storeOutlog.setStId(storeInventory.getStId());
            storeOutlog.setStNo(storeInventory.getStNo());
            storeOutlog.setStSize(storeInventory.getStSize());
            storeOutlog.setEtId(storeInventory.getEtId());
            storeOutlog.setLayNum(storeInventory.getLayNum());
            storeOutlog.setEtPdnum(storeInventory.getEtPdnum() - number);
            storeOutlog.setOperateType(3);
            storeOutlogService.save(storeOutlog);
            msg = "审核数量小于台账";
        }


        storeInventory.setEtPdnum(number);
        storeInventoryService.updateById(storeInventory);
        executeTraycardMapper.updateById(executeTraycard);

        ExecuteBriefer briefer = executeBrieferService.getById(bfId);
        ExecuteExamine executeExamine = new ExecuteExamine();
        executeExamine.setBfId(bfId);
        executeExamine.setExUserid(exUserid);
        executeExamine.setRptTime(briefer.getHandleTime());
        executeExamine.setDataBefore(beforEtPdnum.toString());
        executeExamine.setDataAfter(number.toString());
        executeExamine.setExStatus(1);
        executeExamine.setExWay(1);
        executeExamine.setTyId(storeInventory.getEtId());
        executeExamine.setRptUserid(briefer.getHandleUsid());
//        executeExamine.setExamineStatus(1);
        executeExamine.setStoreBefore(beforeStNo);
        executeExamine.setStoreAfter(storeSeat.getStNo());
        executeExamineService.save(executeExamine);

        List<ExecuteTraycard> executeTraycards = executeTraycardMapper.selectList(Wrappers.<ExecuteTraycard>lambdaQuery().eq(ExecuteTraycard::getExId, briefer.getExId()));
        List<ExecuteTraycard> collect = executeTraycards.stream().filter(a -> ((a.getExStatus() != null && 0 == a.getExStatus()) || null == a.getExStatus())).collect(Collectors.toList());
        if (collect.isEmpty()) {
            briefer.setExStatus(1);
            executeBrieferService.updateById(briefer);
        }
        return msg;
    }

    public String auditsNotTray(Integer bfId, Integer number, Integer exUserid) {
        ExecuteBriefer briefer = executeBrieferService.getById(bfId);
        StoreInventory storeInventory = storeInventoryService.getOne(Wrappers.<StoreInventory>lambdaQuery().eq(StoreInventory::getDbId, bfId).isNull(StoreInventory::getEtId));
        if (null == storeInventory) {
            return null;
        }
        if (null == number) {
            number = storeInventory.getEtPdnum();
        }
        ExecuteExamine executeExamine = new ExecuteExamine();
        executeExamine.setBfId(bfId);
        executeExamine.setExUserid(exUserid);
        executeExamine.setRptTime(briefer.getHandleTime());
        executeExamine.setDataBefore(storeInventory.getEtPdnum().toString());
        executeExamine.setDataAfter(number.toString());
        executeExamine.setExStatus(1);
        executeExamine.setExWay(1);
        executeExamine.setRptUserid(briefer.getHandleUsid());
        executeExamineService.save(executeExamine);

        // 审核时数量错误
        if (number > storeInventory.getEtPdnum()) {
            // 审核时数量大于台账数量
            StoreInlog storeInlog = new StoreInlog();
            storeInlog.setStType(1);
            storeInlog.setStId(storeInventory.getStId());
            storeInlog.setStNo(storeInventory.getStNo());
            storeInlog.setStSize(storeInventory.getStSize());
            storeInlog.setOperateType(3);
            storeInlog.setEtId(storeInventory.getEtId());
            storeInlog.setLayNum(storeInventory.getLayNum());
            storeInlog.setEtPdnum(number - storeInventory.getEtPdnum());
            storeInlogService.save(storeInlog);
            return "审核数量大于台账";
        } else if (number < storeInventory.getEtPdnum()) {
            // 审核时数量小于台账数量
            StoreOutlog storeOutlog = new StoreOutlog();
            storeOutlog.setStType(1);
            storeOutlog.setStId(storeInventory.getStId());
            storeOutlog.setStNo(storeInventory.getStNo());
            storeOutlog.setStSize(storeInventory.getStSize());
            storeOutlog.setEtId(storeInventory.getEtId());
            storeOutlog.setLayNum(storeInventory.getLayNum());
            storeOutlog.setEtPdnum(storeInventory.getEtPdnum() - number);
            storeOutlog.setOperateType(3);
            storeOutlogService.save(storeOutlog);
            return "审核数量小于台账";
        }
        return null;

    }


    @Override
    public List<PhoneTrayCardVO> getTrayByBfId(Integer bfId) {
        List<PhoneTrayCardVO> list = executeTraycardMapper.getByBfId(bfId);
        List<PhoneTrayCardVO> list2 = storeInventoryService.getByBfId(bfId);
        list.addAll(list2);
        int sum = list.stream().filter(a -> null != a.getEtPdnum()).mapToInt(PhoneTrayCardVO::getEtPdnum).sum();
        for (PhoneTrayCardVO phoneTrayCardVO : list) {
            phoneTrayCardVO.setEtPdTotalNum(sum);
        }
        return list;
    }


    @Override
    public FlowCardVO getFlowCard(String tdNo) {
        ExecuteTraycard executeTraycard =
                executeTraycardMapper.selectOne(new QueryWrapper<ExecuteTraycard>().eq("td_no", tdNo));
        if (executeTraycard == null) {
            return null;
        }
        Integer sdId = executeTraycard.getSdId();
        WorkbatchOrdlink workbatchOrdlink = workbatchOrdlinkMapper.selectById(sdId);
        Integer wfId = executeTraycard.getWfId();
        WorkbatchShift workbatchShift = workbatchShiftMapper.selectById(wfId);
        Integer mpId = executeTraycard.getMpId();
        FlowCardVO flowCardVO = new FlowCardVO();
        flowCardVO.setTrayNum(executeTraycard.getTrayNum());
        String wbNo = null;
        Integer pdId = null;
        if (workbatchOrdlink != null) {
            wbNo = workbatchOrdlink.getWbNo();
            if (workbatchOrdlink.getPdId() != null) {
                pdId = Integer.valueOf(workbatchOrdlink.getPdId());
            }
            flowCardVO.setCmName(workbatchOrdlink.getCmName());
            flowCardVO.setOdNo(workbatchOrdlink.getOdNo());
            flowCardVO.setPdName(workbatchOrdlink.getPdName());
            flowCardVO.setPtName(workbatchOrdlink.getPartName());
            flowCardVO.setWbNo(wbNo);
        }
        if (workbatchShift != null) {
            Integer maId = workbatchShift.getMaId();
            MachineMainfo machineMainfo = machineMainfoMapper.selectById(maId);
            flowCardVO.setMaName(machineMainfo.getName());
        }
        StoreSeat storeSeat = storeSeatService.getById(mpId);
        flowCardVO.setId(executeTraycard.getId());
        if (storeSeat != null) {
            flowCardVO.setSrNo(storeSeat.getSrNo());
        }
        flowCardVO.setStNo(executeTraycard.getStorePlace());
        /*查询该产品所有的工序流程*/
        List<TrayCardSumVO> trayCardSumVOList = executeTraycardMapper.processFlow(wbNo, pdId);
        trayCardSumVOList.stream().filter(e -> sdId.equals(e.getSdId())).forEach(trayCardSumVO -> trayCardSumVO.setIdentifier(1));
        flowCardVO.setTrayCardSumVOList(trayCardSumVOList);
        return flowCardVO;
    }


    @Override
    public IPage<ExecuteTraycardVO> getTraycardByTrayParam(IPage<ExecuteTraycardVO> page, ExecuteTraycardStoreVO trayParam) {
        List<ExecuteTraycardVO> traycardByTrayParam = executeTraycardMapper.getTraycardByTrayParam(page, trayParam);
        return page.setRecords(traycardByTrayParam);
    }

    @Override
    public IPage<ExecuteTraycardVO> getTraycardByStoreinfo(IPage<ExecuteTraycardVO> page, ExecuteTraycardStoreVO trayParam) {
        List<ExecuteTraycardVO> traycardByTrayParam = executeTraycardMapper.getTraycardByStoreinfo(page, trayParam);
        return page.setRecords(traycardByTrayParam);
    }


    @Override
    public PhoneSeatTrayInfoVO getPhoneInfoBySeatId(Integer seatId) {
        StoreSeat storeSeat = storeSeatService.getById(seatId);
        List<PhoneSeatTrayInfoRecordVO> list = executeTraycardMapper.getPhoneInfoBySeatId(seatId);
        int pdTotal = list.stream().mapToInt(PhoneSeatTrayInfoRecordVO::getPdTotal).sum();
        PhoneSeatTrayInfoVO phoneSeatTrayInfoVO = new PhoneSeatTrayInfoVO();
        phoneSeatTrayInfoVO.setStNo(storeSeat.getStNo());
        phoneSeatTrayInfoVO.setCapacity(storeSeat.getCapacity());
        phoneSeatTrayInfoVO.setUseTrayNumber(list.size());
        phoneSeatTrayInfoVO.setPdTotal(pdTotal);
        phoneSeatTrayInfoVO.setPhoneSeatTrayInfoRecordVOList(list);
        return phoneSeatTrayInfoVO;
    }

    @Override
    public boolean hasTray(Integer exId) {
        List<ExecuteTraycard> executeTraycards = executeTraycardMapper.selectList(Wrappers.<ExecuteTraycard>lambdaQuery().eq(ExecuteTraycard::getExId, exId).select(ExecuteTraycard::getId));
        return executeTraycards.size() > 0;
    }

}
