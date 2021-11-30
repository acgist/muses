package com.acgist.gateway.config;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/**
 * 网关映射
 * 
 * @author acgist
 */
@Configuration
@ConfigurationProperties(prefix = "gateway")
public class GatewayMappingConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMappingConfig.class);

	/**
	 * 映射
	 */
	private List<GatewayMapping> mapping = new ArrayList<>();

	/**
	 * 加载网关映射配置
	 * 
	 * @return 网关映射配置
	 */
	@Bean
	public static final PropertySourcesPlaceholderConfigurer gatewayMapping() {
		final YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("gateway.mapping.yml"));
		final PropertySourcesPlaceholderConfigurer config = new PropertySourcesPlaceholderConfigurer();
		config.setProperties(yaml.getObject());
		return config;
	}

	@PostConstruct
	public void init() {
		this.mapping.forEach(mapping -> LOGGER.info("网关映射：{}", mapping));
	}

	/**
	 * 通过网关地址获取网关映射
	 * 
	 * @param gateway 网关地址
	 * 
	 * @return 网关映射
	 */
	public GatewayMapping gatewayMapping(String gateway) {
		for (GatewayMapping gatewayMapping : this.mapping) {
			if (gatewayMapping.getGateway().equals(gateway)) {
				return gatewayMapping;
			}
		}
		return null;
	}

	public List<GatewayMapping> getMapping() {
		return mapping;
	}

	public void setMapping(List<GatewayMapping> mapping) {
		this.mapping = mapping;
	}

}
