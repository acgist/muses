package com.acgist.www.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Web工具
 * 
 * @author acgist
 */
public class WebUtils {
	
	private WebUtils() {
		
	}

	/**
	 * 读取客户端的IP地址
	 * 
	 * @param request 请求
	 * 
	 * @return IP地址
	 */
	public final static String clientIP(HttpServletRequest request) {
		String clientIP = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(clientIP) || "unknown".equalsIgnoreCase(clientIP)) {
			clientIP = request.getHeader("proxy-client-ip");
		}
		if (StringUtils.isEmpty(clientIP) || "unknown".equalsIgnoreCase(clientIP)) {
			clientIP = request.getRemoteAddr();
		}
		return clientIP;
	}
	
}
