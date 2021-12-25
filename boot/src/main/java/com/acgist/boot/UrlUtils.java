package com.acgist.boot;

import java.net.URLDecoder;
import java.net.URLEncoder;

import com.acgist.boot.config.MusesConfig;

/**
 * URL工具
 * 
 * @author acgist
 */
public final class UrlUtils {

	private UrlUtils() {
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

}
