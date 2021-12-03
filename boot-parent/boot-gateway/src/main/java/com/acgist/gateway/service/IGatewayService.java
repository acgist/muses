package com.acgist.gateway.service;

import com.acgist.gateway.pojo.dto.GatewayDto;

/**
 * 网关
 * 
 * @author acgist
 */
public interface IGatewayService {

	/**
	 * 推送网关消息
	 * 
	 * @param gatewayDto GatewayDto
	 */
	void push(GatewayDto gatewayDto);
	
}
