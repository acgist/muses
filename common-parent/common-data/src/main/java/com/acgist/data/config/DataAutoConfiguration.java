package com.acgist.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.data.service.IdService;

/**
 * 自动装配
 * 
 * @author acgist
 */
@Configuration
public class DataAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public IdService idService() {
		return new IdService();
	}
	
}
