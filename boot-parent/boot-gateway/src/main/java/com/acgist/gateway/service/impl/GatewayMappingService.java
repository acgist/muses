package com.acgist.gateway.service.impl;

import java.util.List;

import com.acgist.boot.utils.URLUtils;
import com.acgist.gateway.config.GatewayMapping;

/**
 * 网关映射
 * 
 * @author acgist
 */
public class GatewayMappingService {

	/**
	 * 网关映射
	 */
	private final List<GatewayMapping> mapping;
	
	public GatewayMappingService(List<GatewayMapping> mapping) {
		this.mapping = mapping;
	}

	/**
	 * 通过网关地址获取网关映射
	 * 
	 * @param method 请求方法
	 * @param path 网关地址
	 * 
	 * @return 网关映射
	 */
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
