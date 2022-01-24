package com.acgist.gateway.notify.socket;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.gateway.notify.NotifyConfig;

/**
 * Socket通知配置
 * 
 * @author acgist
 */
@ConfigurationProperties(prefix = "notify.socket")
public class SocketNotifyConfig extends NotifyConfig {

	private int timeout;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
