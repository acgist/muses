package com.acgist.gateway.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface GatewayChannel {

	@Output("gateway-output")
    MessageChannel channel();
	
}
