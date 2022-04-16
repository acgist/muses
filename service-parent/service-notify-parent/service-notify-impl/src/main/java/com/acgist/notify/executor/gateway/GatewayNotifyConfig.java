package com.acgist.notify.executor.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.notify.executor.NotifyConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Gateway通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "notify.gateway")
public class GatewayNotifyConfig extends NotifyConfig {

	/**
	 * 超时时间
	 */
	private int timeout;

}
