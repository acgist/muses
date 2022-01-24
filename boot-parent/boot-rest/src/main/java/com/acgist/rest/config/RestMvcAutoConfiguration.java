package com.acgist.rest.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.rest.interceptor.UserInterceptor;
import com.acgist.rest.resolver.CurrentUserArgumentResolver;
import com.acgist.www.config.WwwMvcAutoConfiguration;

/**
 * Rest MVC配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(WwwMvcAutoConfiguration.class)
@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
public class RestMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UserInterceptor userInterceptor() {
		return new UserInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CurrentUserArgumentResolver currentUserArgumentResolver() {
		return new CurrentUserArgumentResolver();
	}
	
}
