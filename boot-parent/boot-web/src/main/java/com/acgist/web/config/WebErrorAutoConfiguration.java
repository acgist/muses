package com.acgist.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.web.controller.WebControllerAdvice;
import com.acgist.web.controller.WebErrorController;
import com.acgist.www.WwwErrorPageRegistrar;

/**
 * Web异常配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class WebErrorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public WebControllerAdvice webControllerAdvice() {
		return new WebControllerAdvice();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WebErrorController webErrorController() {
		return new WebErrorController();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WwwErrorPageRegistrar wwwErrorPageRegistrar() {
		return new WwwErrorPageRegistrar();
	}
	
}
