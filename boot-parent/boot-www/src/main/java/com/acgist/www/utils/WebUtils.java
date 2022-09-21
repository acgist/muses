package com.acgist.www.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Web工具
 * 
 * @author acgist
 */
public class WebUtils {
	
	private WebUtils() {
		
	}

	/**
	 * @param request 请求
	 * 
	 * @return 客户端的IP地址
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
	 * @param request 请求
	 * 
	 * @return 所有请求参数
	 */
	public static final MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		final Map<String, String[]> map = request.getParameterMap();
		final MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(map.size());
		map.forEach((key, values) -> {
			if (values.length > 0) {
				for (String value : values) {
					parameters.add(key, value);
				}
			}
		});
		return parameters;
	}
	
}
