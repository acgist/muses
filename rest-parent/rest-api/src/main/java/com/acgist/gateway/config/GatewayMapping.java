package com.acgist.gateway.config;

import com.acgist.gateway.Gateway;

/**
 * <p>网关映射</p>
 * 
 * @author acgist
 */
public final class GatewayMapping {

	/**
	 * <p>是否记录</p>
	 */
	private boolean record;
	/**
	 * <p>网关地址</p>
	 */
	private String gateway;
	/**
	 * <p>网关名称</p>
	 */
	private String gatewayName;
	/**
	 * <p>请求类型</p>
	 */
	private Class<Gateway> requestClass;

	public GatewayMapping() {
	}

	/**
	 * @param record 是否记录
	 * @param gateway 网关地址
	 * @param gatewayName 网关名称
	 * @param requestClass 请求类型
	 */
	public GatewayMapping(boolean record, String gateway, String gatewayName, Class<Gateway> requestClass) {
		this.record = record;
		this.gateway = gateway;
		this.gatewayName = gatewayName;
		this.requestClass = requestClass;
	}

	public boolean getRecord() {
		return record;
	}

	public String getGateway() {
		return gateway;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public Class<Gateway> getRequestClass() {
		return requestClass;
	}

	public void setRecord(boolean record) {
		this.record = record;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public void setRequestClass(Class<Gateway> requestClass) {
		this.requestClass = requestClass;
	}
	
}
