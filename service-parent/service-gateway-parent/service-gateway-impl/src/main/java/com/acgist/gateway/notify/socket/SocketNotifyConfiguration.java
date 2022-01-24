package com.acgist.gateway.notify.socket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Socket自动装配
 * 
 * @author acgist
 */
@EnableConfigurationProperties(SocketNotifyConfig.class)
@ConditionalOnProperty(value = "notify.socket.enable", matchIfMissing = true, havingValue = "true")
public class SocketNotifyConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SocketNotify socketNotify(SocketNotifyConfig config) {
		return new SocketNotify(config);
	}
	
}
