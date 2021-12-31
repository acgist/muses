package com.example.demo;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.KafkaProducter.Message;

@Configuration
public class KafkaConsumer {
	
	@Autowired
	private StreamBridge streamBridge;

	@Bean
	public Consumer<Message> gatewayRecord() {
		return message -> {
			System.out.println("gatewayRecord====" + message + "====" + Thread.currentThread().getId());
		};
	}
	
	@Bean
	public Consumer<Message> gatewayPush() {
		return message -> this.streamBridge.send("topic-gateway", message);
	}

}
