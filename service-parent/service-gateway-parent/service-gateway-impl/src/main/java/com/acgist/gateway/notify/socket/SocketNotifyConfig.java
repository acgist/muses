package com.acgist.gateway.notify.socket;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.gateway.notify.NotifyConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Socket通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "notify.socket")
public class SocketNotifyConfig extends NotifyConfig {

	/**
	 * 超时时间
	 */
	private int timeout;

}
