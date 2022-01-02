package com.acgist.gateway.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.interceptor.PackageInterceptor;
import com.acgist.gateway.interceptor.ProcessInterceptor;
import com.acgist.rest.config.RestMvcAutoConfiguration;
import com.acgist.rest.interceptor.UserInteceptor;

/**
 * 加载配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureAfter(value = GatewayAutoConfiguration.class)
@AutoConfigureBefore(value = RestMvcAutoConfiguration.class)
@ConditionalOnProperty(value = "system.rest.mvc", matchIfMissing = true, havingValue = "true")
public class GatewayMvcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public UserInteceptor userInteceptor() {
		return new UserInteceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ProcessInterceptor processInterceptor() {
		return new ProcessInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PackageInterceptor packageInterceptor() {
		return new PackageInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GatewayMvcConfig gatewayMvcConfig() {
		return new GatewayMvcConfig();
	}
	
}
