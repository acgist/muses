package com.acgist.rest.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.rest.interceptor.UserInteceptor;

/**
 * Rest MVC配置
 * 
 * @author acgist
 */
public class RestMvcConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestMvcConfig.class);
	
	private static final String GATEWAY = "/**";
	
	/**
	 * 用户拦截
	 */
	@Autowired
	private UserInteceptor userInteceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("加载拦截器：userInteceptor");
		registry.addInterceptor(this.userInteceptor).addPathPatterns(GATEWAY);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CurrentUserArgumentResolver());
	}
	
}
