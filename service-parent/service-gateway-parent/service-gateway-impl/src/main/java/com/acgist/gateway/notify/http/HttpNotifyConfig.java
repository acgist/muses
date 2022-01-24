package com.acgist.gateway.notify.http;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.gateway.notify.NotifyConfig;

/**
 * HTTP通知配置
 * 
 * @author acgist
 */
@ConfigurationProperties(prefix = "notify.http")
public class HttpNotifyConfig extends NotifyConfig {

	private int timeout;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
