package com.acgist.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.gateway.pojo.dto.GatewayDto;
import com.acgist.gateway.pojo.entity.GatewayEntity;
import com.acgist.gateway.repository.GatewayRepository;

@EnableBinding(value = GatewayChannel.class)
public class GatewayListener {

	@Autowired
	private GatewayRepository gatewayRepository;
	
	@Transactional
	@StreamListener("gateway-input")
	public void gatewayInput(Message<GatewayDto> message) {
		final GatewayDto gatewayDto = message.getPayload();
		final GatewayEntity gatewayEntity = new GatewayEntity();
		gatewayEntity.copy(gatewayDto);
		this.gatewayRepository.save(gatewayEntity);
	}

}
