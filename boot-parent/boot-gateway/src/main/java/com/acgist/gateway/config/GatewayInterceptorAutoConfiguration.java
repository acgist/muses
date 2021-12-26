package com.acgist.gateway.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.gateway.interceptor.PackageInterceptor;
import com.acgist.gateway.interceptor.ProcessInterceptor;
import com.acgist.rest.config.CurrentUserArgumentResolver;
import com.acgist.rest.config.RestInterceptorAutoConfiguration;
import com.acgist.rest.interceptor.UserInteceptor;

/**
 * 加载配置
 * 
 * @author acgist
 */
@Configuration
@AutoConfigureBefore(value = RestInterceptorAutoConfiguration.class)
@ConditionalOnProperty(value = "system.rest.interceptor", matchIfMissing = true, havingValue = "true")
public class GatewayInterceptorAutoConfiguration implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayInterceptorAutoConfiguration.class);
	
	private static final String GATEWAY = "/**";
	
	/**
	 * 用户拦截
	 */
	private UserInteceptor userInteceptor;
	/**
	 * 处理拦截
	 */
	private ProcessInterceptor processInterceptor;
	/**
	 * 打包拦截
	 */
	private PackageInterceptor packageInterceptor;

	@Bean
	@ConditionalOnMissingBean
	public UserInteceptor userInteceptor() {
		this.userInteceptor = new UserInteceptor();
		return this.userInteceptor;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public ProcessInterceptor processInterceptor() {
		this.processInterceptor = new ProcessInterceptor();
		return this.processInterceptor;
	}
	
	@Bean
	@ConditionalOnMissingBean
	public PackageInterceptor packageInterceptor() {
		this.packageInterceptor = new PackageInterceptor();
		return this.packageInterceptor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("加载拦截器：processInterceptor");
		registry.addInterceptor(this.processInterceptor).addPathPatterns(GATEWAY);
		LOGGER.info("加载拦截器：userInteceptor");
		registry.addInterceptor(this.userInteceptor).addPathPatterns(GATEWAY);
		LOGGER.info("加载拦截器：packageInterceptor");
		registry.addInterceptor(this.packageInterceptor).addPathPatterns(GATEWAY);
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserArgumentResolver());
		resolvers.add(new GatewayBodyArgumentResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
	
}
