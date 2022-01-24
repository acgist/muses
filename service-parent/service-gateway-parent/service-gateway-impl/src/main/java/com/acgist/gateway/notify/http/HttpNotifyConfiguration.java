package com.acgist.gateway.notify.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HTTP自动装配
 * 
 * @author acgist
 */
@EnableConfigurationProperties(HttpNotifyConfig.class)
@ConditionalOnProperty(value = "notify.http.enable", matchIfMissing = true, havingValue = "true")
public class HttpNotifyConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public HttpNotify httpNotify(HttpNotifyConfig config) {
		return new HttpNotify(config);
	}
	
}
