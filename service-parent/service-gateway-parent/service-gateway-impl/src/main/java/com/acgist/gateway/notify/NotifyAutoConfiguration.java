package com.acgist.gateway.notify;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.acgist.gateway.notify.http.HttpNotifyConfiguration;
import com.acgist.gateway.notify.socket.SocketNotifyConfiguration;

/**
 * 通知自动配置
 * 
 * @author acgist
 */
@Configuration
@Import(value = {
	HttpNotifyConfiguration.class,
	SocketNotifyConfiguration.class
})
public class NotifyAutoConfiguration {

}
