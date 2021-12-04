package com.acgist.rest.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.rest.interceptor.UserInteceptor;

/**
 * 加载配置
 * 
 * @author acgist
 */
@Configuration
@ConditionalOnProperty(value = "system.gateway.interceptor", matchIfMissing = true, havingValue = "true")
@ConditionalOnMissingClass("com.acgist.gateway.config.GatewayInterceptorAutoConfiguration")
public class RestInterceptorAutoConfiguration implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestInterceptorAutoConfiguration.class);
	
	private static final String GATEWAY = "/**";
	
	/**
	 * 用户拦截
	 */
	private UserInteceptor userInteceptor;

	@Bean
	@ConditionalOnMissingBean
	public UserInteceptor userInteceptor() {
		this.userInteceptor = new UserInteceptor();
		return this.userInteceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("加载拦截器：userInteceptor");
		registry.addInterceptor(this.userInteceptor).addPathPatterns(GATEWAY);
		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserArgumentResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
	
}
