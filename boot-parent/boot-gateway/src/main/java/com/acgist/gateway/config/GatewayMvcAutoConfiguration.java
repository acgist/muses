package com.acgist.gateway.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.interceptor.PackageInterceptor;
import com.acgist.gateway.interceptor.ProcessInterceptor;
import com.acgist.gateway.resolver.GatewayBodyArgumentResolver;
import com.acgist.www.config.WwwMvcAutoConfiguration;

/**
 * 加载配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(WwwMvcAutoConfiguration.class)
@ConditionalOnProperty(value = "system.mvc", matchIfMissing = true, havingValue = "true")
public class GatewayMvcAutoConfiguration {

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
	public GatewayBodyArgumentResolver gatewayBodyArgumentResolver() {
		return new GatewayBodyArgumentResolver();
	}
	
}
