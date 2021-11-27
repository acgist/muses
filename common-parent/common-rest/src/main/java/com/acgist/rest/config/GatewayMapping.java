package com.acgist.rest.config;

import com.acgist.common.JSONUtils;
import com.acgist.rest.Gateway;

/**
 * 网关映射
 * 
 * @author acgist
 */
public final class GatewayMapping {

	/**
	 * 网关名称
	 */
	private String name;
	/**
	 * 网关地址
	 */
	private String gateway;
	/**
	 * 是否记录
	 */
	private boolean record;
	/**
	 * 请求类型
	 */
	private Class<Gateway> clazz;

	public GatewayMapping() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public boolean isRecord() {
		return record;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public Class<Gateway> getClazz() {
		return clazz;
	}

	public void setClazz(Class<Gateway> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
