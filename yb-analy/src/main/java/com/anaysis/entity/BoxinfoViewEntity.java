package com.anaysis.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BoxinfoViewEntity {
   private String uuid;
   private String status;
   private String mac;
   private int numberOfDay;
   private Date updateAt;

}
