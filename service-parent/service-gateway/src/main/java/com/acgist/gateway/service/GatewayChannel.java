package com.acgist.gateway.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface GatewayChannel {

	@Input("gateway-input")
	MessageChannel channel();
	
}
