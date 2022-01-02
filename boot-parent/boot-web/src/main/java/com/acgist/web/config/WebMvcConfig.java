package com.acgist.web.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.web.interceptor.CsrfInterceptor;

/**
 * Web MVC配置
 * 
 * @author acgist
 */
public class WebMvcConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

	@Autowired
	private CsrfInterceptor csrfInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：csrfInterceptor");
		registry.addInterceptor(this.csrfInterceptor).addPathPatterns("/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserArgumentResolver());
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
//	}

}
