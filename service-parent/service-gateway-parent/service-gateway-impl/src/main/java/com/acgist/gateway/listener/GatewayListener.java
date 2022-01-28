package com.acgist.gateway.listener;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.acgist.gateway.notify.NotifyService;
import com.acgist.gateway.pojo.dto.GatewayDto;
import com.acgist.gateway.pojo.entity.GatewayEntity;
import com.acgist.gateway.repository.GatewayRepository;

@Configuration
public class GatewayListener {

	@Autowired
	private NotifyService notifyService;
	@Autowired
	private GatewayRepository gatewayRepository;
	
	@Bean
//	@Transactional
	public Consumer<GatewayDto> gatewayRecord() {
		return gatewayDto -> {
			final GatewayEntity gatewayEntity = new GatewayEntity();
			gatewayEntity.copy(gatewayDto);
			this.gatewayRepository.save(gatewayEntity);
			this.notifyService.notify(gatewayEntity);
		};
	}
	
}