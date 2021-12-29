package com.acgist.boot.config;

import org.springframework.util.SocketUtils;

/**
 * 端口配置
 * 
 * @author acgist
 */
public final class PortConfig {
	
	private PortConfig() {
	}
	
	/**
	 * 系统端口
	 */
	public static final String SYSTEM_PORT = "system.port";
	/**
	 * 网关端口
	 */
	public static final int PORT_GATEWAY = 8888;
	/**
	 * Rest-OAuth2端口
	 */
	public static final int PORT_REST_OAUTH2 = 9999;
	/**
	 * Web服务开始端口
	 */
	public static final int PORT_WEB_MIN = 18000;
	/**
	 * Rest服务开始端口
	 */
	public static final int PORT_REST_MIN = 19000;

	public static final void setGatewayPort() {
		System.setProperty(SYSTEM_PORT, String.valueOf(PORT_GATEWAY));
	}
	
	public static final void setRestOAuth2Port() {
		System.setProperty(SYSTEM_PORT, String.valueOf(PORT_REST_OAUTH2));
	}
	
	public static final void setWebPort() {
		System.setProperty(SYSTEM_PORT, String.valueOf(SocketUtils.findAvailableTcpPort(PORT_WEB_MIN)));
	}
	
	public static final void setRestPort() {
		System.setProperty(SYSTEM_PORT, String.valueOf(SocketUtils.findAvailableTcpPort(PORT_REST_MIN)));
	}
	
}
