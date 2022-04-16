package com.acgist.notify.executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.acgist.notify.executor.gateway.GatewayNotifyConfiguration;
import com.acgist.notify.executor.mail.MailNotifyConfiguration;
import com.acgist.notify.executor.sms.SmsNotifyConfiguration;

/**
 * 通知自动配置
 * 
 * @author acgist
 */
@Configuration
@Import(value = {
	SmsNotifyConfiguration.class,
	MailNotifyConfiguration.class,
	GatewayNotifyConfiguration.class
})
public class NotifyAutoConfiguration {

}
