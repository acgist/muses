package com.acgist.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.web.controller.WebControllerAdvice;
import com.acgist.web.controller.WebErrorController;

@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class WebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(WebControllerAdvice.class)
	public WebControllerAdvice gatewayControllerAdvice() {
		return new WebControllerAdvice();
	}
	
	@Bean
	@ConditionalOnMissingBean(WebErrorController.class)
	public WebErrorController webErrorController() {
		return new WebErrorController();
	}
	
}
