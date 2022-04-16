package com.acgist.notify.executor.gateway;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Gateway自动装配
 * 
 * @author acgist
 */
@EnableConfigurationProperties(GatewayNotifyConfig.class)
@ConditionalOnProperty(value = "notify.gateway.enable", matchIfMissing = true, havingValue = "true")
public class GatewayNotifyConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public GatewayNotify gatewayNotify(GatewayNotifyConfig config) {
		return new GatewayNotify(config);
	}
	
}
