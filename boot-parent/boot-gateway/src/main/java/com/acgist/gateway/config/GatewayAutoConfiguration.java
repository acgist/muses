package com.acgist.gateway.config;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.service.impl.RsaService;
import com.acgist.notify.gateway.model.dto.GatewayDto;

/**
 * 网关配置
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
	@ConditionalOnMissingBean
	public RsaService rsaService() {
		return new RsaService();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public Consumer<GatewayDto> gatewayPush() {
		return gatewayDto -> this.streamBridge.send(this.topic, gatewayDto);
	}
	
}
