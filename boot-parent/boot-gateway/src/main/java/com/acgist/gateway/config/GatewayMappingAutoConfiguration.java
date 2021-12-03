package com.acgist.gateway.config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMappingAutoConfiguration.class);

	/**
	 * 映射
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
