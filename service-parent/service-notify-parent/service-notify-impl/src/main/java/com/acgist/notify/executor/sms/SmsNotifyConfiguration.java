package com.acgist.notify.executor.sms;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Sms自动装配
 * 
 * @author acgist
 */
@EnableConfigurationProperties(SmsNotifyConfig.class)
@ConditionalOnProperty(value = "notify.sms.enable", matchIfMissing = true, havingValue = "true")
public class SmsNotifyConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SmsNotify smsNotify(SmsNotifyConfig config) {
		return new SmsNotify(config);
	}
	
}
