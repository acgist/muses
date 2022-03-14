package com.acgist.gateway.config;

import com.acgist.boot.JSONUtils;
import com.acgist.gateway.model.request.GatewayRequest;

import lombok.Getter;
import lombok.Setter;

/**
 * 网关映射
 * 
 * @author acgist
 */
@Getter
@Setter
public class GatewayMapping {

	/**
	 * 网关名称
	 */
	private String name;
	/**
	 * 网关地址
	 * 
	 * GET:/user
	 * POST:/user
	 */
	private String gateway;
	/**
	 * 是否记录
	 */
	private boolean record;
	/**
	 * 请求类型
	 */
	private Class<GatewayRequest> clazz;

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
