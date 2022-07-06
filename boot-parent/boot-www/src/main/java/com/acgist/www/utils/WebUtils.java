package com.acgist.www.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
	
	/**
	 * 判断是否响应JSON
	 * 
	 * @param request 请求
	 * 
	 * @return 是否响应JSON
	 */
	public static final boolean responseJSON(HttpServletRequest request) {
		final String header = request.getHeader(HttpHeaders.ACCEPT);
		return header == null || header.contains(MediaType.APPLICATION_JSON_VALUE);
	}
	
	/**
	 * 判断是否响应HTML
	 * 
	 * @param request 请求
	 * 
	 * @return 是否响应HTML
	 */
	public static final boolean responseHTML(HttpServletRequest request) {
		final String header = request.getHeader(HttpHeaders.ACCEPT);
		return header != null && header.contains(MediaType.TEXT_HTML_VALUE);
	}
	
}
