package com.acgist.gateway.notify.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.gateway.notify.NotifyConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * HTTP通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "notify.http")
public class HttpNotifyConfig extends NotifyConfig {

	/**
	 * 超时时间
	 */
	private int timeout;

}
