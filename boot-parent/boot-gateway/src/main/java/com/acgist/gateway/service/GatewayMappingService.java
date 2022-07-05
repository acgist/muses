package com.acgist.gateway.service;

import com.acgist.gateway.config.GatewayMapping;

/**
 * 网关映射
 * 
 * @author acgist
 */
public interface GatewayMappingService {

	/**
	 * 通过网关地址获取网关映射
	 * 
	 * @param method 请求方法
	 * @param path 请求地址
	 * 
	 * @return 网关映射
	 */
	GatewayMapping gatewayMapping(String method, String path);
	
}
