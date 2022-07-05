package com.acgist.boot.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.acgist.boot.config.MusesConfig;

/**
 * URL工具
 * 
 * @author acgist
 */
public final class URLUtils {

	private URLUtils() {
	}

	/**
	 * URL编码
	 * 
	 * @param content 原始内容
	 * 
	 * @return 编码内容
	 */
	public static final String encode(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		return URLEncoder.encode(content, MusesConfig.CHARSET)
			// 空格编码变成加号导致加号解码变成空格
			.replace("+", "%20");
	}

	/**
	 * URL解码
	 * 
	 * @param content 编码内容
	 * 
	 * @return 原始内容
	 */
	public static final String decode(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		}
		return URLDecoder.decode(content, MusesConfig.CHARSET);
	}
	
	/**
	 * 通过请求信息获取权限路径
	 * 
	 * @param method 请求方法
	 * @param path 请求地址
	 * 
	 * @return 权限路径
	 */
	public static final String authority(String method, String path) {
		return method.toUpperCase() + ":" + path;
	}
	
	/**
	 * 判断请求权限路径是否符合权限路径
	 * 
	 * @param authority 请求权限路径
	 * @param path 权限路径
	 * 
	 * @return 是否符合
	 */
	public static final boolean match(String authority, String path) {
		return
			// 完整匹配
			authority.equals(path) ||
			// 正则匹配
			authority.matches(path);
	}
	
	/**
	 * Map转为URL参数
	 * 
	 * @param map Map
	 * 
	 * @return URL参数
	 */
	public static final String toQuery(Map<String, String> map) {
		if (MapUtils.isEmpty(map)) {
			return null;
		}
		return map.entrySet().stream()
			.filter(entry -> StringUtils.isNotEmpty(entry.getKey()) || StringUtils.isNotEmpty(entry.getValue()))
			.map(entry -> String.join("=", entry.getKey(), URLUtils.encode(entry.getValue())))
			.collect(Collectors.joining("&"));
	}

}
