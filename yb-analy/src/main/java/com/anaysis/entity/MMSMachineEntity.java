package com.anaysis.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MMSMachineEntity {

    @Id
    private String id;
    private String uuid;
    private String state;
    private String count;
    private String time;
}
