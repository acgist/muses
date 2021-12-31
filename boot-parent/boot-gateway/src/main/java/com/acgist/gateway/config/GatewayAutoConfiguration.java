package com.acgist.gateway.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.pojo.dto.GatewayDto;

/**
 * 网关
 * 
 * @author acgist
 */
@Configuration
public class GatewayAutoConfiguration {
	
	@Value("${system.topic.gateway:topic-gateway}")
	private String topic;
	
	@Autowired
	private StreamBridge streamBridge;

	@Bean
	public Consumer<GatewayDto> gatewayPush() {
		return gatewayDto -> this.streamBridge.send(this.topic, gatewayDto);
	}
	
}
