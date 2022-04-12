package com.acgist.www.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.acgist.www.resolver.RequestJsonArgumentResolver;

/**
 * Www MVC配置
 * 
 * @author acgist
 */
@Configuration
public class WwwMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
	public RequestJsonArgumentResolver requestJsonArgumentResolver() {
		return new RequestJsonArgumentResolver();
	}
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
	public WwwMvcConfig wwwMvcConfig() {
		return new WwwMvcConfig();
	}

}
