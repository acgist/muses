package com.acgist.gateway.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.acgist.gateway.interceptor.PackageInterceptor;
import com.acgist.gateway.interceptor.ProcessInterceptor;
import com.acgist.rest.config.RestMvcConfig;

/**
 * Gateway MVC配置
 * 
 * @author acgist
 */
public class GatewayMvcConfig extends RestMvcConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMvcConfig.class);
	
	private static final String GATEWAY = "/**";
	
	/**
	 * 处理拦截
	 */
	@Autowired
	private ProcessInterceptor processInterceptor;
	/**
	 * 打包拦截
	 */
	@Autowired
	private PackageInterceptor packageInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		LOGGER.info("加载拦截器：processInterceptor");
		registry.addInterceptor(this.processInterceptor).addPathPatterns(GATEWAY);
		LOGGER.info("加载拦截器：packageInterceptor");
		registry.addInterceptor(this.packageInterceptor).addPathPatterns(GATEWAY);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		super.addArgumentResolvers(resolvers);
		resolvers.add(new GatewayBodyArgumentResolver());
	}
	
}
