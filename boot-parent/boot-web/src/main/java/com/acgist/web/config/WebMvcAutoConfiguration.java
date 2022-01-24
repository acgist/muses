package com.acgist.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.web.interceptor.CsrfInterceptor;
import com.acgist.web.resolver.CurrentUserArgumentResolver;
import com.acgist.www.config.WwwMvcAutoConfiguration;

/**
 * Web MVC配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(WwwMvcAutoConfiguration.class)
@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
public class WebMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public CsrfInterceptor csrfInterceptor() {
		return new CsrfInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CurrentUserArgumentResolver currentUserArgumentResolver() {
		return new CurrentUserArgumentResolver();
	}
	
}
