//package com.anaysis.common;
//
//import com.anaysis.common.SpringUtil;
//import com.anaysis.entity.BoxInfoEntity;
//import com.anaysis.entity.MachineEntity;
//import com.anaysis.service.BoxInfoService;
//import com.anaysis.service.MachineService;
//import com.anaysis.socket.utils.CRC16M;
//import com.anaysis.socket.utils.StringsUtil;
//import org.springframework.stereotype.Controller;
//
//import java.io.InputStream;
//import java.io.OutputStreamWriter;
//import java.math.BigInteger;
//import java.net.Socket;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//
//public class Anaysis {
//    final String STA = "42";
//    final String TAL = "03";
//
//
//    public BoxInfoService boxInfoService = (BoxInfoService) SpringUtil.getBean(BoxInfoService.class);
//
//    private MachineService machineService = (MachineService) SpringUtil.getBean(MachineService.class);
//
//    public void anaysisstatus(String message, String sip, Socket s) {
//        System.out.println("解析主动上报数据:"+message);
//        char[] marrays = message.toCharArray();
//        ReceiveParm rp = Server.hask.get(sip);
//        String mac = rp.rmac;
//
//        //设备地址
//        char[] address_a = Arrays.copyOfRange(marrays, 0, 2);
//        //功能码
//        char[] function_a = Arrays.copyOfRange(marrays, 2, 4);
//
//
//        String address_s = String.valueOf(address_a);
//        String function_s = String.valueOf(function_a);
//
//        System.out.println("mac :"+mac);
//        BoxInfoEntity boxInfoEntity = boxInfoService.getByMac(mac);
//        if (STA.equals(function_s)) {
//            boolean jx_t = jxup(marrays, boxInfoEntity, address_s);
//            if (jx_t) {
//                String r_js = sendGetNum(s, mac);
//                System.out.println("jishu return");
//                System.out.println(r_js);
//                char[] ys = r_js.toCharArray();
//                jxjs(ys, boxInfoEntity, mac);
//            }
//        } else if (TAL.equals(function_s)) {
//            jxjs(marrays, boxInfoEntity, mac);
//        }
//    }
//
//    private boolean jxup(char[] marrays, BoxInfoEntity boxInfoEntity, String address_s) {
//        // 获取的主动上报的数据，应该更新状态和设备地址
//        //数据
//        char[] data_a = Arrays.copyOfRange(marrays, 15, 16);
//        String data_s = String.valueOf(data_a);
//
//        System.out.println("返回状态16进制");
//        System.out.println(data_s);
//
//        String num = Integer.toBinaryString(Integer.parseInt(data_s, 16));
//        //补位
//        if (num.length()==1){
//            num = "000"+num;
//        }else if (num.length()==2){
//            num = "00"+num;
//        }else if (num.length()==3){
//            num = "0"+num;
//        }
//
//        System.out.println("返回状态二进制");
//        System.out.println(num);
//
//        char[] numarrays = num.toCharArray();
//
//        char[] d_error = Arrays.copyOfRange(numarrays, 0, 1);
//        char[] d_stop = Arrays.copyOfRange(numarrays, 1, 2);
//        char[] d_run = Arrays.copyOfRange(numarrays, 2, 3);
//
//        String error_s = String.valueOf(d_error);
//        String stop_s = String.valueOf(d_stop);
//        String run_s = String.valueOf(d_run);
//
//        //判断开机关机信号是否已存在，存在不处理
//        String status = "0";
//        //解析信号
//        if ("1".equals(run_s)) {
//            status = "1";
//        }
//        if ("1".equals(stop_s)) {
//            status = "2";
//        }
//        if ("1".equals(error_s)) {
//            status = "3";
//        }
//        //对比信号
//        System.out.println("当前记录状为：" + boxInfoEntity.getStatus());
//        if (status.equals(boxInfoEntity.getStatus())) {
//            System.out.println("对比状态成功,不做处理");
//            //对比成功，不做处理
//            return false;
//        }
//        boxInfoEntity.setStatus(status);//状态
//        boxInfoEntity.setAddress(address_s);//设备地址
//        //判断是否是上班开机
//        if (datadiff(boxInfoEntity.getUpdateAt())) {
//            //上班开机。当日计数归零
//            System.out.println("上班开机,计数清零");
//            boxInfoEntity.setNumberOfDay(0);//当日计数
//            boxInfoEntity.setNumber(0);//盒子计数
//        }
//        boxInfoService.saveOrUpdate(boxInfoEntity);
//        return true;
//    }
//
//    private void jxjs(char[] marrays, BoxInfoEntity boxInfoEntity, String mac) {
//        //计数请求返回，更新计数
//        char[] mac_a = Arrays.copyOfRange(marrays, 2, 4);
//        String mac_s = String.valueOf(mac_a);
//
//        //判断是否有mac
//        String data_s ="";
//        if (TAL.equals(mac_s)){
//            char[] data_a = Arrays.copyOfRange(marrays, 6, 10);
//            data_s = String.valueOf(data_a);
//        }else{
//            char[] data_a = Arrays.copyOfRange(marrays, 18, 22);
//            data_s = String.valueOf(data_a);
//        }
//
//        //数据
//
//
//        System.out.println("计数16进制");
//        System.out.println(data_s);
//        //16转10
//        int num = Integer.parseInt(new BigInteger(data_s, 16).toString());
//        //总计数
//        System.out.println("计数10进制");
//        System.out.println(num);
//        int number = boxInfoEntity.getNumber();
//        int diffnum = 0;
//        if (number <= num) {
//            diffnum = num - number;
//        } else {
//            diffnum = 65535 - number + num;
//        }
//        System.out.println("待保存盒子计数：");
//        System.out.println(num);
//        System.out.println("待保存盒子当日计数");
//        System.out.println(boxInfoEntity.getNumberOfDay() + diffnum);
//        boxInfoEntity.setNumber(num);
//        boxInfoEntity.setNumberOfDay(boxInfoEntity.getNumberOfDay() + diffnum);
//        boxInfoService.saveOrUpdate(boxInfoEntity);
//
//
//        BoxInfoEntity boxEntity = boxInfoService.getByMac(mac);
//        String b_id = boxEntity.getMac();
//        String status = boxEntity.getStatus();
//        String count = String.valueOf(boxEntity.getNumberOfDay());
//
//        //保存到mongdb
//        save_nosql(b_id, status, count);
//    }
//
//    private boolean datadiff(Date u_data) {
//        Date nowtime = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(u_data);
//        long time1 = cal.getTimeInMillis();
//        cal.setTime(nowtime);
//        long time2 = cal.getTimeInMillis();
//        long between_days = (time2 - time1) / (1000 * 3600 * 24);
//
//        int d_num = Integer.parseInt(String.valueOf(between_days));
//        if (d_num >= 1) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private void save_nosql(String b_id, String status, String count) {
//        Date nowtime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String stop = "0";
//        String run = "0";
//        String error = "0";
//
//        if ("1".equals(status)) {
//            run = "1";
//        } else if ("2".equals(status)) {
//            stop = "1";
//        } else if ("3".equals(status)) {
//            error = "1";
//        }
//        MachineEntity machineEntity = new MachineEntity();
//        machineEntity.setB_id(b_id);
//        machineEntity.setRun(run);
//        machineEntity.setStop(stop);
//        machineEntity.setError(error);
//        machineEntity.setCount(count);
//        machineEntity.setTime(formatter.format(nowtime));
//        System.out.println("保存数据为：");
//        System.out.println(machineEntity);
//        boolean result = machineService.addMachine(machineEntity);
//        System.out.println(result);
//    }
//
//    public void jxbox(String tx, String sip) {
//        System.out.println("boxInfoService:::::::::::;" + boxInfoService);
//        if (boxInfoService != null) {
//            BoxInfoEntity boxInfoEntity = boxInfoService.getByMac(tx);
//            if (boxInfoEntity == null) {
//                BoxInfoEntity boxInfo = new BoxInfoEntity();
//                boxInfo.setMac(tx);
//                boxInfo.setStatus("2");
//                boxInfo.setNumber(0);
//                boxInfo.setNumberOfDay(0);
//                boxInfo.setSip(sip);
//                boxInfoService.save(boxInfo);
//            }else {
//                boxInfoEntity.setSip(sip);
//                boxInfoService.saveOrUpdate(boxInfoEntity);
//            }
//        }
//    }
//
//    public String requests_count_msg(String mac) {
//        BoxInfoEntity boxInfoEntity = boxInfoService.getByMac(mac);
//        String address = boxInfoEntity.getAddress();
//        //String jxm = "874E";
//        return (CRC16M.getSendBuf(address + "0300400001")).toString();
//    }
//
//    private String sendGetNum(Socket s, String mac) {
//        String tx = "";
//        try {
//            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
//
//            String sdstr = requests_count_msg(mac);
//            System.out.println("技术请求：" + sdstr);
//            osw.write(sdstr);
//            osw.flush();
//            //--输出服务器传回的消息的头信息
//            InputStream is = s.getInputStream();
//            byte[] buf = new byte[20];
//            int len = is.read(buf);
//            tx = StringsUtil.bytesToHex1(buf);
//
//            System.out.println("==================return======计数返回:" + tx);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return tx;
//    }
//}
