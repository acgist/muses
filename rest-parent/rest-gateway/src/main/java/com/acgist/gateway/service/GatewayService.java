package com.acgist.gateway.service;

import org.springframework.stereotype.Service;

import com.acgist.gateway.pojo.dto.GatewayDto;

@Service
public class GatewayService implements IGatewayService {

	@Override
	public void push(GatewayDto gatewayDto) {
		// TODO:mq
	}

}
