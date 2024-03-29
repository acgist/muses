package com.acgist.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.service.GatewayMappingService;
import com.acgist.gateway.service.impl.GatewayMappingServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 网关映射
 * 
 * @author acgist
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayMappingAutoConfiguration {

	/**
	 * 网关映射
	 */
	private List<GatewayMapping> mapping = new ArrayList<>();
	
	public List<GatewayMapping> getMapping() {
		return mapping;
	}
	
	public void setMapping(List<GatewayMapping> mapping) {
		this.mapping = mapping;
		this.mapping.forEach(value -> log.info("网关映射：{}", value));
	}
	
	@Bean
	@ConditionalOnMissingBean
	public GatewayMappingService gatewayMappingService() {
		return new GatewayMappingServiceImpl(this.mapping);
	}
	
}
