package com.acgist.gateway.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acgist.gateway.config.GatewayMapping;

/**
 * 网关映射
 * 
 * @author acgist
 */
public class GatewayMappingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayMappingService.class);
	
	private final List<GatewayMapping> mapping;
	
	public GatewayMappingService(List<GatewayMapping> mapping) {
		this.mapping = mapping;
	}

	@PostConstruct
	public void init() {
		this.mapping.forEach(mapping -> LOGGER.info("网关映射：{}", mapping));
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
		final String gateway = method.toUpperCase() + ":" + path;
		for (GatewayMapping gatewayMapping : this.mapping) {
			if (gatewayMapping.getGateway().equals(gateway)) {
				return gatewayMapping;
			}
		}
		return null;
	}

}
