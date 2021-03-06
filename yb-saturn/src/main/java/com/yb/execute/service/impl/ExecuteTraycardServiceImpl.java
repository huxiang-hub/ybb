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


    private int divisor = 999999;//????????????????????????

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
            String batchNumber;//????????????
            String functionary = null;//?????????
            String userName = constructionVO.getUserName();//?????????
            String maName = constructionVO.getMaName();//????????????
            String dpName = constructionVO.getDpName();//?????????
            String sdDate = constructionVO.getSdDate();//????????????
            batchNumber = sdDate + " " + dpName;
            if (!StringUtil.isEmpty(userName)) {
                functionary = userName + " " + maName;
            }
            String cmName = constructionVO.getCmName();//????????????
            String downPorcess = constructionVO.getDownPorcess();//?????????
            String downPorcessDes = constructionVO.getDownPorcessDes();//???????????????
            String finalTime = constructionVO.getFinalTime();//????????????
            String inspector = constructionVO.getInspector();//?????????
            String materialName = constructionVO.getMaterialName();//????????????
            Integer odCount = constructionVO.getOdCount();//????????????
            String odNo = constructionVO.getOdNo();//????????????
            Integer onTime = constructionVO.getOnTime();//??????
            String pdName = constructionVO.getPdName();//????????????
            String pdNo = constructionVO.getPdNo();//????????????
            String pdSize = constructionVO.getPdSize();//????????????
            String plateNo = constructionVO.getPlateNo();//??????
            Integer plateNumber = constructionVO.getPlateNumber();//????????????
            String prDes = constructionVO.getPrDes();//????????????
            String prName = constructionVO.getPrName();//????????????
            String productBatch = constructionVO.getProductBatch();//??????????????????
            String productNo = constructionVO.getProductNo();//??????????????????
            Integer prPlanNum = constructionVO.getPrPlanNum();//??????????????????
            String prRoute = constructionVO.getPrRoute();//????????????
            String ptName = constructionVO.getPtName();//????????????
            String remark = constructionVO.getRemark();//??????
            Integer wasteNum = constructionVO.getWasteNum();//????????????
            String wbNo = constructionVO.getWbNo();//????????????
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
//            datamap.put("billId", "?????????");//pdf??????
            Map<String, Object> inputMap = new HashMap<>();
            inputMap.put("datamap", datamap);
            /* ??????????????????pdf,??????byte??????,??????????????????????????????????????????pdf???????????????*/
            byte[] bytes = PDFUtils.creatPdfByte(inputMap, resourcePath, response);
            bytesList.add(bytes);
        }

        /*-----------????????????pdf----------*/
        if (bytesList == null || bytesList.isEmpty()) {
            return;
        }
        OutputStream out = null;// ?????????
        Document document = null;
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + URLEncoder.encode("?????????.pdf", "UTF-8"));
            out = new BufferedOutputStream(response.getOutputStream());
            document = new Document(new PdfReader(bytesList.get(0)).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, out);
            document.open();
            for (int i = 0; i < bytesList.size(); i++) {
                PdfReader reader = new PdfReader(bytesList.get(i));
                int n = reader.getNumberOfPages();// ???????????????
                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);// ?????????Pdf,?????????j???
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
//                /*??????????????????*/
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
        List<Integer> etIdList = traycardDeleteListVO.getEtIdList();//?????????id??????
        if (etIdList == null || etIdList.isEmpty()) {
            return false;
        }
        Integer mpId = traycardDeleteListVO.getMpId();//??????id
        String remark = traycardDeleteListVO.getRemark();//??????
        String storePlace = traycardDeleteListVO.getStorePlace();//????????????
        Integer trayNum = traycardDeleteListVO.getTrayNum();//??????
        Integer usId = traycardDeleteListVO.getUsId();//?????????id
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
//            /*???????????????sdId*/
//            UpProcessScheduleVO upProcessScheduleVO = workbatchOrdlinkNewService.upProcessSchedule(wfId);
//            if(upProcessScheduleVO == null){
//                continue;
//            }
//            Integer sdId = upProcessScheduleVO.getId();
            /*??????????????????sdId*/
            Integer sdId = workbatchOrdlinkMapper.getUpProcessSdIdByWfId(wfId);
            if (sdId == null) {
                continue;
            }
            /*?????????????????????*/
            List<WorkbatchShift> workbatchShiftList =
                    workbatchShiftMapper.selectList(new QueryWrapper<WorkbatchShift>().eq("sd_id", sdId));
            /*????????????????????????????????????*/
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
                Integer finishNum = workbatchShift.getFinishNum();//???????????????
                for (ExecuteTraycard executeTraycard : executeTraycardList) {
                    executeTraycardVO = ExecuteTraycardWrapper.build().entityVO(executeTraycard);
                    Integer prcessWfId = executeTraycardVO.getWfId();//???????????????id
                    Integer trayNum = executeTraycardVO.getTrayNum();//????????????
                    planNum += trayNum;
                    if (finishNum >= planNum) {//????????????????????????????????????????????????????????????
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
        /*?????????????????????????????????*/
        List<TraycardDataVO> traycardDataVOList = executeTraycardMapper.getTraycardData(toIntList, maId);
        /*??????????????????*/
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
        /*??????????????????*/
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        for (ExecuteTraycardVO executeTraycardVO : executeTraycardByWfId) {
            String refNowDay;
            String time = "";
            Date startTime = executeTraycardVO.getStartTime();//????????????
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
        String msg = "????????????";
        if (null == number) {
            number = storeInventory.getEtPdnum();
        }
        Integer beforEtPdnum = storeInventory.getEtPdnum();
        if (seatId != null) {
            // ?????????????????????
            StoreSeat seat1 = storeSeatService.getById(seatId);
            executeTraycard.setStorePlace(seat1.getStNo());
            executeTraycard.setMpId(seat1.getId());
            storeInventory.setStId(seatId);
            storeInventory.setStNo(seat1.getStNo());
            executeTraycard.setTrayNum(number);
        }
        // ?????????????????????
        if (number > storeInventory.getEtPdnum()) {
            // ?????????????????????????????????
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
            msg = "????????????????????????";
        } else if (number < storeInventory.getEtPdnum()) {
            // ?????????????????????????????????
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
            msg = "????????????????????????";
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

        // ?????????????????????
        if (number > storeInventory.getEtPdnum()) {
            // ?????????????????????????????????
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
            return "????????????????????????";
        } else if (number < storeInventory.getEtPdnum()) {
            // ?????????????????????????????????
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
            return "????????????????????????";
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
        /*????????????????????????????????????*/
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
