package com.acgist.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.acgist.gateway.pojo.dto.GatewayDto;

@Service
@EnableBinding(GatewayChannel.class)
public class GatewayService implements IGatewayService {

	@Autowired
	private GatewayChannel gatewayChannel;

	@Override
	public void push(GatewayDto gatewayDto) {
		this.gatewayChannel.channel().send(MessageBuilder.withPayload(gatewayDto).build());
	}

}
