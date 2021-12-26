package com.acgist.gateway.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * TODO：使用新版配置
 * 
 * @author acgist
 */
public interface GatewayChannel {

	@Output("gateway-output")
	MessageChannel channel();
	
}
