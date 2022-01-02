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
	@ConditionalOnMissingBean(WebControllerAdvice.class)
	public WebControllerAdvice gatewayControllerAdvice() {
		return new WebControllerAdvice();
	}
	
	@Bean
	@ConditionalOnMissingBean(WebErrorController.class)
	public WebErrorController webErrorController() {
		return new WebErrorController();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WwwErrorPageRegistrar errorPageRegistrar() {
		return new WwwErrorPageRegistrar();
	}
	
}
