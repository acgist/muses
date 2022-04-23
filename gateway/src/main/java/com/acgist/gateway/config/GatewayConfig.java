package com.acgist.gateway.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.acgist.boot.model.Message;
import com.acgist.boot.model.MessageCode;
import com.acgist.boot.model.MessageCodeException;
import com.acgist.gateway.utils.ResponseUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 网关配置
 * 
 * @author acgist
 */
@Slf4j
@Configuration
public class GatewayConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public ErrorWebExceptionHandler errorWebExceptionHandler() {
		return new ErrorWebExceptionHandler() {
			@Override
			public Mono<Void> handle(ServerWebExchange exchange, Throwable t) {
				log.debug("网关异常", t);
				final ServerHttpResponse response = exchange.getResponse();
				if (response.isCommitted()) {
					return Mono.error(t);
				}
				HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				final Message<String> message;
				t = MessageCodeException.root(t);
				if (t instanceof ResponseStatusException) {
					final ResponseStatusException responseStatusException = (ResponseStatusException) t;
					status = responseStatusException.getStatus();
					message = Message.fail(MessageCode.of(status.value()), responseStatusException.getMessage());
				} else if(t instanceof MessageCodeException) {
					final MessageCodeException messageCodeException = (MessageCodeException) t;
					message = Message.fail(messageCodeException.getCode(), messageCodeException.getMessage());
				} else if(t != null) {
					message = Message.fail(MessageCode.CODE_9999, t.getMessage());
				} else {
					message = Message.fail(MessageCode.CODE_9999);
				}
				return ResponseUtils.response(message, status, response);
			}
		};
	}

}
