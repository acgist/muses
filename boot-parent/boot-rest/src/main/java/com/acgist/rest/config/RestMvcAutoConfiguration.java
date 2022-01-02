package com.acgist.rest.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.rest.interceptor.UserInteceptor;

/**
 * Rest MVC配置
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnProperty(value = "system.rest.mvc", matchIfMissing = true, havingValue = "true")
@ConditionalOnMissingBean(value = RestMvcConfig.class)
public class RestMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UserInteceptor userInteceptor() {
		return new UserInteceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RestMvcConfig restMvcConfig() {
		return new RestMvcConfig();
	}
	
}
