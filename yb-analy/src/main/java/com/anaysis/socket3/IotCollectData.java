package com.anaysis.socket3;

import com.anaysis.socket1.CollectData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * @Description:
 * @Author my
 * @Date Created in 2020/6/13 17:43
 */
@Data
@JsonIgnoreProperties
public class IotCollectData extends CollectData {
    private Integer temp;

}

