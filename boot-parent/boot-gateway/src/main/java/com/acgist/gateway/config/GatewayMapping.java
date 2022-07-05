package com.acgist.gateway.config;

import com.acgist.boot.model.Model;
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
public class GatewayMapping extends Model {

	private static final long serialVersionUID = 1L;
	
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

}
