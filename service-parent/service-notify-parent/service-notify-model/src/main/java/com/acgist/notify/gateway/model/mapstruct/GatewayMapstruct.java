package com.acgist.notify.gateway.model.mapstruct;

import org.mapstruct.Mapper;

import com.acgist.notify.gateway.model.dto.GatewayDto;
import com.acgist.notify.gateway.model.entity.GatewayEntity;

@Mapper(componentModel = "spring")
public interface GatewayMapstruct {

	GatewayEntity toEntity(GatewayDto dto);
	
}
