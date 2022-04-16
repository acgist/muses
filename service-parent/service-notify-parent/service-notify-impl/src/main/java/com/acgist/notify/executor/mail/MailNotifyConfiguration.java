package com.acgist.notify.executor.mail;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Mail自动装配
 * 
 * @author acgist
 */
@EnableConfigurationProperties(MailNotifyConfig.class)
@ConditionalOnProperty(value = "notify.mail.enabled", matchIfMissing = true, havingValue = "true")
public class MailNotifyConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MailNotify mailNotify(MailNotifyConfig config) {
		return new MailNotify(config);
	}
	
}
