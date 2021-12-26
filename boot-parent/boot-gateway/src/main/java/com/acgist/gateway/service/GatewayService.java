package com.acgist.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.acgist.gateway.pojo.dto.GatewayDto;

/**
 * 网关
 * 
 * @author acgist
 */
@Service
@EnableBinding(GatewayChannel.class)
public class GatewayService {

	@Autowired
	private GatewayChannel gatewayChannel;

	/**
	 * 推送网关消息
	 * 
	 * @param gatewayDto GatewayDto
	 */
	public void push(GatewayDto gatewayDto) {
		this.gatewayChannel.channel().send(MessageBuilder.withPayload(gatewayDto).build());
	}

}
