package com.acgist.notify.executor.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.notify.executor.NotifyConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Sms通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "notify.sms")
public class SmsNotifyConfig extends NotifyConfig {

	/**
	 * 超时时间
	 */
	private int timeout;

}
