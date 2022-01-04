package com.yb.panelapi.user.test;

import com.yb.panelapi.user.utils.MD5Utils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class testway {

    @Test
  public void nameDate(){
//        Date t1 = new Date();
//        Date t2 = new Date();
//        t1.getTime();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

       // Date d1 = df.format(df.format(t1));
//        Date d2 = df.parse( df.format(t2));
//        long diff = d1.getTime() - d2.getTime();
//        long days = diff / 60;
        MD5Utils.encodeByMD5("123");
        System.out.println( MD5Utils.encodeByMD5("123"));


    }

}
