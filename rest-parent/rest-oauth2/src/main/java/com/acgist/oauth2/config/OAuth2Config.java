package com.acgist.oauth2.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * OAuth2配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "system.oauth2")
public class OAuth2Config {

	/**
	 * 客户端
	 */
	private Map<String, String> clients;
	/**
	 * Code刷新时间
	 */
	private int code;
	/**
	 * Access Code刷新时间
	 */
	private int access;
	/**
	 * Refresh Code刷新时间
	 */
	private int refresh;
	/**
	 * 跳转地址
	 */
	private String redirectUri;

}
