package com.acgist.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.web.interceptor.CsrfInterceptor;

/**
 * Web MVC配置
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnProperty(value = "system.web.mvc", matchIfMissing = true, havingValue = "true")
public class WebMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public CsrfInterceptor csrfInterceptor() {
		return new CsrfInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public WebMvcConfig webMvcConfig() {
		return new WebMvcConfig();
	}
	
}
