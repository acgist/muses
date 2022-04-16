package com.acgist.notify.gateway.listener;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.notify.executor.NotifyService;
import com.acgist.notify.gateway.dao.mapper.GatewayMapper;
import com.acgist.notify.gateway.model.dto.GatewayDto;
import com.acgist.notify.gateway.model.entity.GatewayEntity;

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
