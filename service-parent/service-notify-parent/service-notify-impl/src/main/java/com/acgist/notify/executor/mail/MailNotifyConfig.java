package com.acgist.notify.executor.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.acgist.notify.executor.NotifyConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Mail通知配置
 * 
 * @author acgist
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "notify.mail")
public class MailNotifyConfig extends NotifyConfig {

	/**
	 * 超时时间
	 */
	private int timeout;
	/**
	 * 发送用户
	 */
	private String from;

}
