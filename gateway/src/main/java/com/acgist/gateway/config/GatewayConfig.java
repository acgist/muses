package com.acgist.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.acgist.boot.MessageCodeException;
import com.acgist.boot.pojo.bean.Message;
import com.acgist.boot.pojo.bean.MessageCode;

import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayConfig.class);
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public ErrorWebExceptionHandler errorWebExceptionHandler() {
		return new ErrorWebExceptionHandler() {
			@Override
			public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
				LOGGER.debug("网关异常", e);
				final ServerHttpResponse response = exchange.getResponse();
				if (response.isCommitted()) {
					return Mono.error(e);
				}
				HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				final Message<String> message;
				if (e instanceof ResponseStatusException) {
					final ResponseStatusException responseStatusException = (ResponseStatusException) e;
					status = responseStatusException.getStatus();
					message = Message.fail(MessageCode.of(status.value()), responseStatusException.getMessage());
				} else if(e instanceof MessageCodeException) {
					final MessageCodeException messageCodeException = (MessageCodeException) e;
					message = Message.fail(messageCodeException.getCode(), messageCodeException.getMessage());
				} else if(e != null) {
					message = Message.fail(MessageCode.CODE_9999, e.getMessage());
				} else {
					message = Message.fail(MessageCode.CODE_9999);
				}
				response.setStatusCode(status);
				response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
				return response.writeWith(Mono.fromSupplier(() -> {
					return response.bufferFactory().wrap(message.toString().getBytes());
				}));
			}
		};
	}

	
	
}
