package com.acgist.gateway.listener;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.dao.mapper.GatewayMapper;
import com.acgist.gateway.data.dto.GatewayDto;
import com.acgist.gateway.data.entity.GatewayEntity;
import com.acgist.gateway.notify.NotifyService;

@Configuration
public class GatewayListener {

	@Autowired
	private GatewayMapper gatewayMapper;
	@Autowired
	private NotifyService notifyService;
	
	@Bean
	public Consumer<GatewayDto> gatewayRecord() {
		return gatewayDto -> {
			final GatewayEntity gatewayEntity = new GatewayEntity();
			gatewayEntity.copy(gatewayDto);
			this.gatewayMapper.insert(gatewayEntity);
			this.notifyService.notify(gatewayEntity);
		};
	}
	
}
