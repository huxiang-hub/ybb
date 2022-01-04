package com.yb.feishu.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "飞书用户列表")
public class FeiShuUser implements Serializable {

    @JsonProperty("has_more")
    private boolean hasMore;
    @JsonProperty("page_token")
    private String pageToken;
    @JsonProperty("user_infos")
    private List<UserInfos> userInfos;
}
