package com.acgist.redis.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 缓存配置
 * 
 * @author acgist
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system.cache")
public class CacheProperties {
	
	/**
	 * 缓存配置
	 */
	private Map<String, Long> keys;

}
