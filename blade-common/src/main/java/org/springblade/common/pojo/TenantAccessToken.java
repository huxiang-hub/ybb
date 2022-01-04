package org.springblade.common.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class TenantAccessToken implements Serializable {
    private int code;
    private int expire;
    private String msg;
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;
}
