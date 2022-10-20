package com.acgist.www.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.www.interceptor.WwwInterceptor;
import com.acgist.www.resolver.WwwMethodArgumentResolver;

import lombok.extern.slf4j.Slf4j;

/**
 * MVC配置
 * 
 * @author acgist
 */
@Slf4j
public class WwwMvcConfig implements WebMvcConfigurer {

//	@Autowired
//	private ObjectMapper mapper;
	@Autowired
	private ApplicationContext context;
//	@Autowired
//	private MappingJackson2HttpMessageConverter converter;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		this.context.getBeansOfType(WwwInterceptor.class).values().stream()
			.filter(WwwInterceptor::enable)
			.sorted()
			.forEach(interceptor -> {
				log.info("加载请求拦截器：{}", interceptor.name());
				registry.addInterceptor(interceptor).addPathPatterns(interceptor.patterns());
			});
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		this.context.getBeansOfType(WwwMethodArgumentResolver.class).values().stream()
			.filter(WwwMethodArgumentResolver::enable)
			.forEach(resolver -> {
				log.info("加载参数解析器：{}", resolver.name());
				resolvers.add(resolver);
			});
	}
	
//	@Override
//	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//		// 修复日期覆盖问题
//		this.converter.setObjectMapper(this.mapper);
//		converters.add(0, this.converter);
//	}
	
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
