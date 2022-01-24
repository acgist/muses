package com.acgist.www.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.www.interceptor.WwwInterceptor;
import com.acgist.www.resolver.WwwMethodArgumentResolver;

/**
 * MVC配置
 * 
 * @author acgist
 */
public class WwwMvcConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(WwwMvcConfig.class);
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		this.context.getBeansOfType(WwwInterceptor.class).values().stream()
			.filter(WwwInterceptor::enable)
			.sorted()
			.forEach(interceptor -> {
				LOGGER.info("加载请求拦截器：{}", interceptor.name());
				registry.addInterceptor(interceptor).addPathPatterns(interceptor.patterns());
			});
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		this.context.getBeansOfType(WwwMethodArgumentResolver.class).values().stream()
			.filter(WwwMethodArgumentResolver::enable)
			.forEach(resolver -> {
				LOGGER.info("加载参数解析器：{}", resolver.name());
				resolvers.add(resolver);
			});
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
