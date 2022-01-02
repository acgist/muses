package com.acgist.gateway.service;

import java.util.List;

import com.acgist.boot.UrlUtils;
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
		final String gateway = UrlUtils.authority(method, path);
		for (GatewayMapping gatewayMapping : this.mapping) {
			if (gatewayMapping.getGateway().equals(gateway)) {
				return gatewayMapping;
			}
		}
		return null;
	}

}
