package com.acgist.rest.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.acgist.rest.interceptor.PackageInterceptor;
import com.acgist.rest.interceptor.ProcessInterceptor;
import com.acgist.rest.interceptor.SignatureInterceptor;
import com.acgist.rest.interceptor.ValidatorInterceptor;
import com.acgist.rest.service.GatewayService;

/**
 * <p>配置 - 拦截器</p>
 * 
 * @author acgist
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorConfig.class);
	
	@Autowired
	private ProcessInterceptor processInterceptor;
	@Autowired
	private PackageInterceptor packageInterceptor;
	@Autowired
	private SignatureInterceptor signatureInterceptor;
	@Autowired
	private ValidatorInterceptor validatorInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("拦截器初始化：processInterceptor");
		registry.addInterceptor(this.processInterceptor).addPathPatterns(GatewayService.GATEWAY);
		LOGGER.info("拦截器初始化：packageInterceptor");
		registry.addInterceptor(this.packageInterceptor).addPathPatterns(GatewayService.GATEWAY);
		LOGGER.info("拦截器初始化：signatureInterceptor");
		registry.addInterceptor(this.signatureInterceptor).addPathPatterns(GatewayService.GATEWAY);
		LOGGER.info("拦截器初始化：dataVerifyInterceptor");
		registry.addInterceptor(this.validatorInterceptor).addPathPatterns(GatewayService.GATEWAY);
		WebMvcConfigurer.super.addInterceptors(registry);
	}

}
