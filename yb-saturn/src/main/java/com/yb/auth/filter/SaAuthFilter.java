///**
// * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.yb.auth.filter;
//
//import com.yb.auth.provider.SaAuthProvider;
//import com.yb.auth.utils.SaJwtUtil;
//import io.jsonwebtoken.Claims;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * 鉴权认证(Saturn进行验证)
// *
// * @author Jenny wang
// */
//@Slf4j
//@Component
//@AllArgsConstructor
//public class SaAuthFilter implements GlobalFilter, Ordered {
//
////	private SaAuthProperties authProperties;
////	private ObjectMapper objectMapper;
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		String path = exchange.getRequest().getURI().getPath();
//		if (isSkip(path)) {
//			return chain.filter(exchange);
//		}
//		ServerHttpResponse resp = exchange.getResponse();
//		String headerToken = exchange.getRequest().getHeaders().getFirst(SaAuthProvider.AUTH_KEY);
//		String paramToken = exchange.getRequest().getQueryParams().getFirst(SaAuthProvider.AUTH_KEY);
//		if (StringUtils.isAllBlank(headerToken, paramToken)) {
//			return unAuth(resp, "缺失令牌,鉴权失败");
//		}
//
//		String auth = StringUtils.isBlank(headerToken) ? paramToken : headerToken;
//		String token = SaJwtUtil.getToken(auth);
//		Claims claims = SaJwtUtil.parseJWT(token);
//		if (claims == null) {
////			return unAuth(resp, "请求未授权");
//		}
//		return chain.filter(exchange);
//	}
//
//	private boolean isSkip(String path) {
//		return SaAuthProvider.getDefaultSkipUrl().stream().map(url -> url.replace(SaAuthProvider.TARGET, SaAuthProvider.REPLACEMENT)).anyMatch(path::contains);
//		// || authProperties.getSkipUrl().stream().map(url -> url.replace(SaAuthProvider.TARGET, SaAuthProvider.REPLACEMENT)).anyMatch(path::contains)
//	}
//
//	private Mono<Void> unAuth(ServerHttpResponse resp, String msg) {
////		objectMapper = SpringUtil.getBean(ObjectMapper.class);//加载springbean文件
//		resp.setStatusCode(HttpStatus.UNAUTHORIZED);
//		resp.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
//		String result = "";
//		try {
//			result ="";// objectMapper.writeValueAsString(SaResponseProvider.unAuth(msg));
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//		}
//		DataBuffer buffer = resp.bufferFactory().wrap(result.getBytes(StandardCharsets.UTF_8));
//		return resp.writeWith(Flux.just(buffer));
//	}
//
//	@Override
//	public int getOrder() {
//		return -100;
//	}
//
//}
