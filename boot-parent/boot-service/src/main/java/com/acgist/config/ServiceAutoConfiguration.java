package com.acgist.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.service.CacheService;
import com.acgist.service.impl.CacheServiceImpl;

/**
 * Service自动配置
 * 
 * @author acgist
 */
@Configuration
public class ServiceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public CacheService cacheService() {
		return new CacheServiceImpl();
	}
	
}
