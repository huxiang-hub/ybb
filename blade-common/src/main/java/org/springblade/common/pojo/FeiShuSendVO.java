package org.springblade.common.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class FeiShuSendVO implements Serializable {
    @JsonProperty("open_id")
    private String openId;
    //    @JsonProperty("root_id")
//    private String rootId;
    @JsonProperty("chat_id")
    private String chatId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("msg_type")
    private String msgType = "text";
    @JsonProperty("content")
    private Content content;
}
