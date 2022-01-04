package com.anaysis.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/****
 * 盒子发送的数据，状态变化
 */
@Data
public class MachineEntity {
    @Id
    private String id;
    private String b_id;
    private String stop;
    private String run;
    private String error;
    private String count;
    private String number;
    private String time;
    private Integer pcount;
}

