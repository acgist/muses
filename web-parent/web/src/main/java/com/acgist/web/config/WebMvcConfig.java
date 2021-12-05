package com.acgist.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.web.interceptor.CsrfInterceptor;

/**
 * 拦截器配置
 * 
 * @author acgist
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

	private CsrfInterceptor csrfInterceptor;

	@Bean
	public CsrfInterceptor csrfInterceptor() {
		this.csrfInterceptor = new CsrfInterceptor();
		return this.csrfInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：csrfInterceptor");
		registry.addInterceptor(this.csrfInterceptor).addPathPatterns("/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

//	网关统一处理
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry
//			.addMapping("/**")
//			.allowedOrigins("*")
//			.allowedMethods("*")
//			.allowedHeaders("*")
//			.allowCredentials(true)
//			.maxAge(3600);
//		WebMvcConfigurer.super.addCorsMappings(registry);
//	}

}
