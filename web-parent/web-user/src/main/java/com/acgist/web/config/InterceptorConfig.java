package com.acgist.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);

	@Autowired
	private CsrfInterceptor csrfInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：csrfInterceptor");
		registry.addInterceptor(csrfInterceptor).addPathPatterns("/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
