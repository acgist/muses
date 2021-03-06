package com.acgist.rest.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.rest.controller.RestControllerAdvice;
import com.acgist.rest.controller.RestErrorController;
import com.acgist.www.config.WwwErrorPageRegistrar;

/**
 * Rest异常配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class RestErrorAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public RestErrorController restErrorController() {
		return new RestErrorController();
	}

	@Bean
	@ConditionalOnMissingBean
	public RestControllerAdvice restControllerAdvice() {
		return new RestControllerAdvice();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WwwErrorPageRegistrar wwwErrorPageRegistrar() {
		return new WwwErrorPageRegistrar();
	}

}
