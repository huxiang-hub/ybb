package com.anaysis.socket1;

import lombok.Data;

import java.util.Date;

@Data
public class ConmachRs {
    String uuid;
    String operateid;
    String uindex;
    String optime;
    String msgstatus;
    String msg;
    Date createAt;
}
