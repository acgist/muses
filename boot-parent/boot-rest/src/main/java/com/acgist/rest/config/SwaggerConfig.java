package com.acgist.rest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * OAuth2服务器配置
 * 
 * @author acgist
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "system.swagger")
public class SwaggerConfig {

	/**
	 * 认证服务器地址
	 */
	private String server;
	/**
	 * ClientId
	 */
	private String clientId;
	/**
	 * ClientSecret
	 */
	private String clientSecret;

}
