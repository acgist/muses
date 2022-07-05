package com.acgist.cloud.config;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.util.SocketUtils;

import lombok.Getter;

/**
 * 端口配置
 * 
 * @author acgist
 */
public final class PortConfig {
	
	private PortConfig() {
	}
	
	/**
	 * 服务端口
	 */
	public static final String SERVER_PORT = "server.port";
	/**
	 * 系统端口
	 */
	public static final String SYSTEM_PORT = "system.port";
	/**
	 * 系统端口最小
	 */
	public static final String SYSTEM_PORT_MIN = "system.port.min";
	/**
	 * 系统端口最大
	 */
	public static final String SYSTEM_PORT_MAX = "system.port.max";
	
	/**
	 * 类型
	 * 
	 * @author acgist
	 */
	@Getter
	public enum Type {
		
		// 网关服务
		GATEWAY(8888),
		// OAuth2授权服务
		REST_OAUTH2(9999),
		// Web服务
		WEB(18000, 19000),
		// Rest服务
		REST(19000, 20000);
		
		// 最小端口
		private final int min;
		// 最大端口
		private final int max;
		
		private Type(int port) {
			this.min = port;
			this.max = port;
		}
		
		private Type(int min, int max) {
			this.min = min;
			this.max = max;
		}
		
	}
	
	/**
	 * 设置端口
	 * 
	 * @param type 类型类型
	 * @param args 启动参数
	 */
	public static final void buildPort(Type type, String ... args) {
		Objects.requireNonNull(type, "没有设置启动类型");
		final ApplicationArguments arguments = new DefaultApplicationArguments(args);
		// 应用端口配置
		final String serverPort = getArgument(SERVER_PORT, arguments);
		if(serverPort != null) {
			System.setProperty(SYSTEM_PORT, serverPort);
			return;
		}
		// 系统端口配置
		final String systemPort = getArgument(SYSTEM_PORT, arguments);
		if(systemPort != null) {
			System.setProperty(SYSTEM_PORT, systemPort);
			return;
		}
		// 随机端口配置
		final String systemPortMin = getArgument(SYSTEM_PORT_MIN, arguments);
		final String systemPortMax = getArgument(SYSTEM_PORT_MAX, arguments);
		final int portMin = Objects.isNull(systemPortMin) ? type.getMin() : Integer.parseInt(systemPortMin);
		final int portMax = Objects.isNull(systemPortMax) ? type.getMax() : Integer.parseInt(systemPortMax);
		System.setProperty(SYSTEM_PORT, String.valueOf(SocketUtils.findAvailableTcpPort(portMin, portMax)));
	}
	
	/**
	 * 获取启动参数
	 * 
	 * @param name 参数名称
	 * @param arguments 启动参数
	 * 
	 * @return 启动参数
	 */
	private static final String getArgument(String name, ApplicationArguments arguments) {
		final List<String> list = arguments.getOptionValues(name);
		if(Objects.isNull(list)) {
			return System.getProperty(name);
		}
		return list.stream().findFirst().orElse(System.getProperty(name));
	}
	
}
