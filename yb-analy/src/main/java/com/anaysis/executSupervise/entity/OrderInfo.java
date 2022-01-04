package com.anaysis.executSupervise.entity;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

/**
 * @author by summer
 * @date 2020/5/25.
 */
@Data
public class OrderInfo {
    //质检id
    private Integer id;

    private String orderName;  //订单名称

    private String orderNo;  //订单编号


}
