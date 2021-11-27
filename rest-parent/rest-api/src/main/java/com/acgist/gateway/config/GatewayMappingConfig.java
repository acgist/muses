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
 * <p>配置 - 网关映射</p>
 * <p>使用yml配置（properties不能配置List）</p>
 * 
 * @author acgist
 */
@Configuration
//@PropertySource(value = "classpath:gateway.mapping.properties", encoding = "UTF-8") // 加载properties
@ConfigurationProperties(prefix = "gateway")
public class GatewayMappingConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMappingConfig.class);
	
	/**
	 * <p>加载YML网关映射配置</p>
	 * 
	 * @return 网关映射配置
	 */
    @Bean
	public static final PropertySourcesPlaceholderConfigurer gatewayMapping() {
		final PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		final YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(new ClassPathResource("gateway.mapping.yml"));
		configurer.setProperties(yaml.getObject());
		return configurer;
	}
	
	/**
	 * <p>映射</p>
	 * <p>注意：需要生产GETTER和SETTER才能注入成功</p>
	 */
	private List<GatewayMapping> mapping = new ArrayList<>();

	@PostConstruct
	public void init() {
		this.mapping.forEach(mapping -> {
			LOGGER.info(
				"网关映射：{}-{}-{}-{}",
				mapping.getRecord(),
				mapping.getGateway(),
				mapping.getGatewayName(),
				mapping.getRequestClass()
			);
		});
	}
	
	/**
	 * <p>通过网关地址获取网关映射</p>
	 * 
	 * @param gateway 网关地址
	 * 
	 * @return 网关映射
	 */
	public GatewayMapping gatewayMapping(Object gateway) {
		for (GatewayMapping gatewayMapping : this.mapping) {
			if(gatewayMapping.getGateway().equals(gateway)) {
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
