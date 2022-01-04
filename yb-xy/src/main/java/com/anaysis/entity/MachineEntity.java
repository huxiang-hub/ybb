package com.anaysis.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class MachineEntity {
    @Id
    private String id;
    private String b_id;
    private String stop;
    private String run;
    private String error;
    private String count;
    private String countV;
    private String time;
}

