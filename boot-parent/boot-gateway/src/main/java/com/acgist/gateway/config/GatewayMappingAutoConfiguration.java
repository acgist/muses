package com.acgist.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.service.GatewayMappingService;

/**
 * 网关映射
 * 
 * @author acgist
 */
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
	}
	
	@Bean
	public GatewayMappingService gatewayMappingService() {
		return new GatewayMappingService(this.mapping);
	}
	
}
