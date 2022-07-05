package com.acgist.gateway.service.impl;

import java.util.List;

import com.acgist.boot.utils.URLUtils;
import com.acgist.gateway.config.GatewayMapping;
import com.acgist.gateway.service.GatewayMappingService;

public class GatewayMappingServiceImpl implements GatewayMappingService {

	/**
	 * 网关映射
	 */
	private final List<GatewayMapping> mapping;
	
	public GatewayMappingServiceImpl(List<GatewayMapping> mapping) {
		this.mapping = mapping;
	}

	@Override
	public GatewayMapping gatewayMapping(String method, String path) {
		final String authority = URLUtils.authority(method, path);
		for (GatewayMapping gatewayMapping : this.mapping) {
			if (URLUtils.match(authority, gatewayMapping.getGateway())) {
				return gatewayMapping;
			}
		}
		return null;
	}

}
