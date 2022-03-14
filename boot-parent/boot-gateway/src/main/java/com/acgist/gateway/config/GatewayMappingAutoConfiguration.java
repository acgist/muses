package com.acgist.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.service.impl.GatewayMappingService;

/**
 * 网关映射
 * 
 * @author acgist
 */
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayMappingAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMappingAutoConfiguration.class);
	
	/**
	 * 网关映射
	 */
	private List<GatewayMapping> mapping = new ArrayList<>();
	
	public List<GatewayMapping> getMapping() {
		return mapping;
	}
	
	public void setMapping(List<GatewayMapping> mapping) {
		this.mapping = mapping;
		this.mapping.forEach(value -> LOGGER.info("网关映射：{}", value));
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GatewayMappingService gatewayMappingService() {
		return new GatewayMappingService(this.mapping);
	}
	
}
