package com.acgist.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.rest.controller.RestControllerAdvice;

@Configuration
public class RestAutoConfiguration {

	@Bean
	public RestControllerAdvice gatewayControllerAdvice() {
		return new RestControllerAdvice();
	}
	
}
