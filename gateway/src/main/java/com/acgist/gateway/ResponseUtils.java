package com.acgist.gateway;

import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.acgist.boot.config.MusesConfig;
import com.acgist.boot.pojo.bean.Message;

import reactor.core.publisher.Mono;

/**
 * 响应工具
 * 
 * @author acgist
 */
public final class ResponseUtils {

	/**
	 * 写出响应
	 * 
	 * @param message 信息
	 * @param status 状态
	 * @param response 响应
	 * 
	 * @return Mono
	 */
	public static final Mono<Void> response(Message<?> message, HttpStatus status, ServerHttpResponse response) {
		response.setStatusCode(status);
		response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MusesConfig.APPLICATION_JSON_UTF8);
		return response.writeWith(Mono.fromSupplier(() -> response.bufferFactory().wrap(message.toString().getBytes())));
	}
	
}
