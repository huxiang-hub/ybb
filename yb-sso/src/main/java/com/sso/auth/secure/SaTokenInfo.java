package com.sso.auth.secure;

import lombok.Data;

@Data
public class SaTokenInfo {

        /**
         * 令牌值
         */
        private String token;

        /**
         * 过期秒数
         */
        private int expire;

}